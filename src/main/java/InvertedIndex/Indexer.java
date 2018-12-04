package InvertedIndex;

import FilesOperation.DocumentDetails;
import com.google.code.externalsorting.ExternalSort;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Indexer {
    private AtomicInteger counter = new AtomicInteger(0);
    private String pathToSaveIndex;
    private Vector<File> filesListNumbers;
    private Vector<File> filesListLowerCase;
    private Vector<File> filesListUpperCase;
    private File folder;
    private HashMap<String,TermDetails> dictionary;
    private HashMap<String, ArrayList<String>> citiesIndex;
    private HashMap <String,String> citiesDetails;
    File postingNumbers;
    File postingLower;
    File postingUpper;

    /**
     *
     * @param pathToSaveIndex
     */
    public Indexer(String pathToSaveIndex, boolean isStemming) {
        this.pathToSaveIndex = pathToSaveIndex;
        if (isStemming)
            this.folder = new File(pathToSaveIndex + "/PostingStemming");
        else
            this.folder = new File(pathToSaveIndex + "/Posting");
        this.folder.mkdir();
        this.filesListLowerCase = new Vector<>();
        this.filesListUpperCase = new Vector<>();
        this.filesListNumbers = new Vector<>();
        this.dictionary = new HashMap<>();
        this.citiesIndex = new HashMap<>();
        this.citiesDetails = new HashMap<>();
    }

    /**
     * This function create a new posting file for the given term map
     *
     * //@param termsMap
     */
    public void  buildIndex(HashMap<String, HashMap<DocumentDetails, Integer>> termsMap) {
        TreeMap<String, HashMap<DocumentDetails, Integer>> map = new TreeMap<>(termsMap);
        String path1 = this.folder.getPath() + "/posting" + counter.getAndIncrement() + "numbers.txt";
        String path2 = this.folder.getPath() + "/posting" + counter.getAndIncrement() + "upperCase.txt";
        String path3 = this.folder.getPath() + "/posting" + counter.getAndIncrement() + "lowerCase.txt";
        File posting1 = new File(path1);
        File posting2 = new File(path2);
        File posting3 = new File(path3);
        try {
            posting1.createNewFile();
            posting2.createNewFile();
            posting3.createNewFile();
            this.filesListNumbers.add(posting1);
            this.filesListUpperCase.add(posting2);
            this.filesListLowerCase.add(posting3);
            FileWriter writer1 = new FileWriter(posting1);
            FileWriter writer2 = new FileWriter(posting2);
            FileWriter writer3 = new FileWriter(posting3);
            for (Map.Entry<String, HashMap<DocumentDetails, Integer>> entry : map.entrySet()) {
                // write the term to the posting file
                String key = entry.getKey();
                if (key.length() == 0) continue;
                if (Character.isUpperCase(key.charAt(0))) {
                    writeToPostingFile(writer2,key,entry);
                    continue;
                }
                if (Character.isLowerCase(key.charAt(0))) {
                    writeToPostingFile(writer3,key,entry);
                } else {
                    writeToPostingFile(writer1,key,entry);
                }
            }
            writer1.close();
            writer2.close();
            writer3.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param writer
     * @param key
     * @param entry
     */
    private void writeToPostingFile(FileWriter writer,String key, Map.Entry<String, HashMap<DocumentDetails, Integer>> entry) {
        try {
            writer.write(key + " ");
            for (Map.Entry<DocumentDetails, Integer> doc : entry.getValue().entrySet()) {
                writer.write("<DOC>");
                // write doc detailed for each document in the format:
                // DocId, file name, tf, max tf, number of uniq words city name, language
                DocumentDetails documentDetails = doc.getKey();
                writer.write(documentDetails.getDocId() + " ");
                writer.write(documentDetails.getFileName() + " ");
                writer.write(doc.getValue() + " ");
                writer.write(documentDetails.getMaxTermFrequency() + " ");
                writer.write(documentDetails.getNumberOfDistinctWords() + " ");
                writer.write(documentDetails.getCityName() + " ");
                writer.write(documentDetails.getLanguage() + " ");
                writer.write("</DOC>");
                if (!dictionary.containsKey(key)){
                    TermDetails termDetails = new TermDetails();
                    termDetails.setNumberOfApperance(doc.getValue());
                    termDetails.setDocumentFrequency(entry.getValue().size());
                    dictionary.put(key,termDetails);
                }
                else{
                    int numberOfAppearance = dictionary.get(key).getNumberOfAppearance();
                    int df = dictionary.get(key).getDocumentFrequency();
                    dictionary.get(key).setNumberOfApperance(numberOfAppearance + doc.getValue());
                    dictionary.get(key).setDocumentFrequency(entry.getValue().size() + df);
                }
/*                if (documentDetails.getCityName() != null){
                    if (!citiesIndex.containsKey(documentDetails.getCityName().toUpperCase())){
                        ArrayList <String> list = new ArrayList();
                        String listOfPositions = "";
                        for (Integer position: documentDetails.getListOfPositions()) {
                            listOfPositions += position + " ";

                        }
                        list.add(documentDetails.getDocId() + "," + listOfPositions);
                        citiesIndex.put(documentDetails.getCityName().toUpperCase(),list);
                    }
                    else{
                        String listOfPositions = "";
                        for (Integer position: documentDetails.getListOfPositions()) {
                            listOfPositions += position + " ";

                        }
                        citiesIndex.get(documentDetails.getCityName().toUpperCase()).add(documentDetails.getDocId() + "," + listOfPositions);
                    }
                }*/
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method merge five posting files which created by buildIndex() function in every call
     */
    public void mergePostingFile() {
        try {
        // create a new merge posting file which will be contains all the files in the filesList field
        // divided by numbers, small letters, big letters.
        String pathForNumbers = this.folder.toString() + "/mergePostingNumbersTemp.txt";
        String pathForLowerCase = this.folder.toString() + "/mergePostingLowerCaseTemp.txt";
        String pathForUpperCase = this.folder.toString() + "/mergePostingUpperCaseTemp.txt";
        this.postingNumbers = new File(pathForNumbers);
        this.postingLower = new File(pathForLowerCase);
        this.postingUpper = new File(pathForUpperCase);
            postingNumbers.createNewFile();
            postingLower.createNewFile();
            postingUpper.createNewFile();

            Comparator<String> cmp = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            };
            ExternalSort.mergeSortedFiles(this.filesListNumbers, this.postingNumbers, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListLowerCase, this.postingLower, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListUpperCase, this.postingUpper, cmp, Charset.defaultCharset(), false);
            Vector <File> postingFiles = new Vector();
            Vector <String> postingFilesNames = new Vector<>();
            postingFiles.add(this.postingLower);
            postingFilesNames.add(this.folder.toString() + "/mergePostingLowerCase.txt");
            postingFiles.add(this.postingUpper);
            postingFilesNames.add(this.folder.toString() + "/mergePostingUpperCase.txt");
            postingFiles.add(this.postingNumbers);
            postingFilesNames.add(this.folder.toString() + "/mergePostingNumbers.txt");
            Vector <File> newPostingFiles = mergeDuplicateLines(postingFiles,postingFilesNames);
            this.postingLower = newPostingFiles.remove(0);
            this.postingUpper = newPostingFiles.remove(0);
            this.postingNumbers = newPostingFiles.remove(0);
            dividePostingFileNumbers();
            checkUpperCase();
            dividePostingFile();
            createDictionaryFile();
            //createCitiesIndexFile();
            // deleteTempFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private void createCitiesIndexFile() {
        getCityDetails();
        try {
            File cityPosting = new File(this.folder.getPath() + "/cityPosting.txt");
            File cityDictionary = new File(this.folder.getPath() + "/cityDictionary.txt");
            cityPosting.createNewFile();
            cityDictionary.createNewFile();
            FileWriter postingWriter = new FileWriter(cityPosting);
            FileWriter dictionaryWriter = new FileWriter(cityDictionary);
            postingWriter.flush();
            dictionaryWriter.flush();
            int lineNumber = 1;
            for (Map.Entry<String, ArrayList<String>> entry : citiesIndex.entrySet()) {
                String cityDetails = this.citiesDetails.get(entry.getKey());
                dictionaryWriter.write(entry.getKey() + "," + cityDetails + "," + lineNumber);
                for (String docDetails : entry.getValue()) {
                    String [] split = docDetails.split(",");
                    postingWriter.write("<DOC>");
                    postingWriter.write(split[0] + ",");
                    if (split.length > 1)
                        postingWriter.write(split[1]);
                }
                postingWriter.write("</DOC>");
                postingWriter.write("\n");
                lineNumber++;
                dictionaryWriter.write("\n");
            }
            postingWriter.close();
            dictionaryWriter.close();
        } catch (Exception e) {}



    }

    /**
     *
     * @param postingFiles
     * @param postingFilesNames
     * @return
     */
    private Vector<File> mergeDuplicateLines(Vector<File> postingFiles, Vector<String> postingFilesNames) {
        Vector<File> newPostingFiles = new Vector<>();
        try {
            for (File file: postingFiles) {
                File newMergeFile = new File(postingFilesNames.remove(0));
                newPostingFiles.add(newMergeFile);
                FileWriter writer = new FileWriter(newMergeFile);
                writer.flush();
                BufferedReader bf = new BufferedReader(new FileReader(file));
                String firstLine = bf.readLine();
                String secondLine = bf.readLine();
                while (firstLine != null){
                    writer.write(firstLine);
                    String [] firstLineSplit = firstLine.split(" ");
                    if (secondLine == null)
                        break;
                    String [] secondLineSplit = secondLine.split(" ");
                    while (secondLine != null && firstLineSplit[0].equals(secondLineSplit[0])){
                        writer.write(secondLine.substring(secondLineSplit[0].length()));
                        secondLine = bf.readLine();
                        secondLineSplit = secondLine.split(" ");
                    }
                    writer.write("\n");
                    firstLine = secondLine;
                    secondLine = bf.readLine();
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPostingFiles;

    }


    /**
     *
     */
    private void checkUpperCase() {

        try {
            File newLower = new File(this.folder.toString() + "/mergePostingLower.txt");
            File newUpper = new File(this.folder.toString() + "/mergePostingUpper.txt");
            newLower.createNewFile();
            newUpper.createNewFile();
            FileWriter writerLower = new FileWriter(newLower);
            FileWriter writerUpper = new FileWriter(newUpper);
            BufferedReader bfUpper = new BufferedReader(new FileReader(postingUpper));
            BufferedReader bfLower = new BufferedReader(new FileReader(postingLower));
            String lineUpper = bfUpper.readLine();
            String lineLower = bfLower.readLine();

            while (lineLower != null && lineUpper != null){
                String [] lineUpperSplit = lineUpper.split(" ");
                String [] lineLowerSplit = lineLower.split(" ");
                // if line in upperCase file is bigger than line in lowerCase file, read the next upper line
                if (lineUpperSplit[0].toLowerCase().compareTo(lineLowerSplit[0]) < 0){
                    writerUpper.write(lineUpper);
                    //writerLower.write(lineLower);
                    lineUpper = bfUpper.readLine();
                    //lineLower = bfLower.readLine();
                    writerUpper.write("\n");

                    continue;
                }
                // if line in upperCase file is smaller than line in lowerCase file, read the next lower line
                if (lineUpperSplit[0].toLowerCase().compareTo(lineLowerSplit[0]) > 0){
                    writerLower.write(lineLower);
//                    writerUpper.write(lineUpper);
                    lineLower = bfLower.readLine();
//                    lineUpper = bfUpper.readLine();
                    writerLower.write("\n");
                    continue;
                }
                // if the term exist also in upper case, copy the content of the line to the new lower words posting file
                if (lineUpperSplit[0].toLowerCase().equals(lineLowerSplit[0])){
                    lineUpper = lineUpper.substring(lineUpperSplit[0].length());
                    writerLower.write(lineLower);
                    writerLower.write(lineUpper);
                    int numberOfAppearanceUpper = dictionary.get(lineUpperSplit[0]).getNumberOfAppearance();
                    int numberOfAppearanceLower = dictionary.get(lineLowerSplit[0]).getNumberOfAppearance();
                    dictionary.get(lineLowerSplit[0]).setNumberOfApperance(numberOfAppearanceLower + numberOfAppearanceUpper);
                    dictionary.remove(lineUpperSplit[0]);
                    writerLower.write("\n");
                }

                //writerLower.write("\n");
                //writerUpper.write("\n");
                lineUpper = bfUpper.readLine();
                lineLower = bfLower.readLine();
            }
            writerLower.close();
            writerUpper.close();

            this.postingLower = newLower;
            this.postingUpper = newUpper;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dividePostingFile(){

        try{
            char c = 'A';
            int lineNumber = 1;
            File posting = new File (this.folder.toString() + "/postingA.txt");
            File newLower = new File(this.folder.toString() + "/mergePostingLower.txt");
            File newUpper = new File(this.folder.toString() + "/mergePostingUpper.txt");
            BufferedReader bfUpper = new BufferedReader(new FileReader(newUpper));
            BufferedReader bfLower = new BufferedReader(new FileReader(newLower));
            FileWriter writer = new FileWriter(posting);
            String lineUpper = bfUpper.readLine();
            String lineLower = bfLower.readLine();

            while (true){
                while (lineUpper != null && lineUpper.charAt(0) == Character.toUpperCase(c)){
                    writer.write(lineUpper.substring(lineUpper.indexOf("<")).trim());
                    writer.write("\n");
                    String key = lineUpper.substring(0,lineUpper.indexOf("<") - 1);
                    dictionary.get(key).setRowNumber(lineNumber);
                    lineNumber++;
                    lineUpper = bfUpper.readLine();
                }
                while (lineLower != null && lineLower.charAt(0) == c){
                    writer.write(lineLower.substring(lineUpper.indexOf("<")).trim());
                    writer.write("\n");
                    String key = lineLower.substring(0,lineLower.indexOf("<") - 1);
                    dictionary.get(key).setRowNumber(lineNumber);
                    lineNumber++;
                    lineLower = bfLower.readLine();
                }
                c++;
                lineNumber = 1;
                if (c == '[')
                    break;
                posting = new File (this.folder.toString() + "/posting" + c + ".txt");
                writer = new FileWriter(posting);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
    }


    }

    private void dividePostingFileNumbers(){

        try{
            char c = '0';
            int lineNumber = 1;
            File posting = new File (this.folder.toString() + "/posting0.txt");
            File numbers = new File(this.folder.toString() + "/mergePostingNumbers.txt");
            BufferedReader bf = new BufferedReader(new FileReader(numbers));
            FileWriter writer = new FileWriter(posting);
            String line = bf.readLine();

            while (true){
                while (line != null && !Character.isDigit(line.charAt(0))){
                    line = bf.readLine();
                }
                while (line != null && line.charAt(0) == c){
                    writer.write(line.substring(line.indexOf("<")).trim());
                    writer.write("\n");
                    String key = line.substring(0,line.indexOf("<") - 1);
                    dictionary.get(key).setRowNumber(lineNumber);
                    lineNumber++;
                    line = bf.readLine();
                }
                c++;
                lineNumber = 1;
                if (c == ':')
                    break;
                posting = new File (this.folder.toString() + "/posting" + c + ".txt");
                writer = new FileWriter(posting);
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void deleteTempFiles(){
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if (fileEntry.getName().charAt(0) == 'm'){
                    try {
                        Files.delete(fileEntry.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void createDictionaryFile(){
        try{
            File dictionaryFile = new File(this.folder.toString() + "/Dictionary.txt");
            dictionaryFile.createNewFile();
            FileWriter fileWriter = new FileWriter(dictionaryFile);
            fileWriter.write(dictionaryToText());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void reset(){
        folder.delete();
/*        for(File file: folder.listFiles())
            if (!file.isDirectory())
                file.delete();*/
    }

    /**
     *
     * @return
     */
    public String toString(){
        StringBuilder ans = new StringBuilder(" ");
        TreeMap<String,TermDetails> map = new TreeMap<>(dictionary);
        for (Map.Entry<String,TermDetails> entry: map.entrySet()){
            ans.append(entry.getKey() + ", " + entry.getValue().getNumberOfAppearance() + "\n");
        }
        return ans.toString();
    }

    /**
     *
     * @return
     */
    public String dictionaryToText(){
        StringBuilder ans = new StringBuilder(" ");
        for (Map.Entry<String,TermDetails> entry: dictionary.entrySet()){
            // term,number of appearance,line number
            ans.append(entry.getKey() + "," + entry.getValue().getNumberOfAppearance() + "," + entry.getValue().getRowNumber() +  "\n");
        }
        return ans.toString();
    }

    public HashMap<String, TermDetails> getDictionary() {
        return dictionary;
    }

    /**
     *
     */
    public void setDictionary() {
        try{
            File dictionaryFile = new File(this.folder.toString() + "/Dictionary.txt");
            BufferedReader bf = new BufferedReader(new FileReader(dictionaryFile));
            String line = bf.readLine();
            while (line != null){
                String [] splitLine = splitByDelimiter(line, ',');
                TermDetails termDetails = new TermDetails();
                termDetails.setNumberOfApperance(Integer.valueOf(splitLine[1]));
                termDetails.setRowNumber(Integer.valueOf(splitLine[2]));
                dictionary.put(splitLine[0],termDetails);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param line
     * @param delimiter
     * @return
     */
    public String[] splitByDelimiter( String line, char delimiter) {
        if(line.equals(" ")) {
            String[] result = {""};
            return result;
        }
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter, 0); // first substring

        while (j >= 0) {
            String word = line.substring(i,j);
            word = word.trim();
            temp[wordCount++] = word;
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }


    /**
     *
     */
    private void getCityDetails (){
        try {
            String urlString = "https://restcountries.eu/rest/v2/all?fields=name;population;currencies;capital";
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String detailsOfCity = content.toString();
            int BracketCount = 0;
            List<String> JsonItems = new ArrayList<>();
            StringBuilder Json = new StringBuilder();
            int currentCharIndex = 0;
            for(char c:detailsOfCity.toCharArray())
            {
                if (currentCharIndex == 0 || currentCharIndex == detailsOfCity.length()-1){
                    currentCharIndex++;
                    continue;
                }
                if (c == '{')
                    ++BracketCount;
                else if (c == '}')
                    --BracketCount;
                Json.append(c);

                if (BracketCount == 0 && c != ' ')
                {
                    if (!Json.toString().equals(","))JsonItems.add(Json.toString());
                    Json = new StringBuilder();
                }
                currentCharIndex++;
            }
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i<JsonItems.size(); i++){
                CityDetails city = objectMapper.readValue(JsonItems.get(i), CityDetails.class);
                if (citiesIndex.containsKey(city.capital.toUpperCase())){
                    //citiesIndex.get(city.capital).
                    citiesDetails.put(city.capital.toUpperCase(),city.name + "," + city.currencies[2] + "," + parsePopulation(city.population));
                }
            }
            con.disconnect();
        } catch (Exception e){}
    }

    /**
     *
     * @param populationSize
     * @return
     */
    private String parsePopulation(String populationSize){
        StringBuilder temp = new StringBuilder(populationSize);
        if (populationSize.length() >= 4 && populationSize.length() < 7){
            temp.insert(populationSize.length() - 3, ".");
            temp.delete(populationSize.indexOf(".") + 3, populationSize.length());
            temp.append("K");
        }
        else if (populationSize.length() >=7 && populationSize.length() < 9){
            temp.insert(populationSize.length() - 6,".");
            temp.delete(populationSize.indexOf(".") + 3, populationSize.length());
            temp.append("M");

        }
        else if (populationSize.length() >=10){
            temp.insert(populationSize.length() - 9,".");
            temp.delete(populationSize.indexOf(".") + 3, populationSize.length());
            temp.append("B");

        }
        return temp.toString();
    }

    /**
     *
     * @return
     */
    public HashMap<String, ArrayList<String>> getCitiesIndex() {
        return citiesIndex;
    }

}
