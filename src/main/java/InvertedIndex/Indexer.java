package InvertedIndex;

import FilesOperation.DocumentDetails;
import FilesOperation.Parse;
import com.google.code.externalsorting.ExternalSort;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Indexer {
    private AtomicInteger counter = new AtomicInteger(0);
    private String pathToSaveIndex;
    private Vector<File> filesListNumbers;
    private Vector<File> filesListLowerCase;
    private Vector<File> filesListUpperCase;
    private File folder;
    private HashMap<String,TermDetails> dictionary;
    private HashMap<String, ArrayList<CityDetails>> citiesIndex;
    File postingNumbers;
    File postingLower;
    File postingUpper;

    /**
     *
     * @param pathToSaveIndex
     */
    public Indexer(String pathToSaveIndex) {
        this.pathToSaveIndex = pathToSaveIndex;
        this.folder = new File(pathToSaveIndex);
        this.filesListLowerCase = new Vector<>();
        this.filesListUpperCase = new Vector<>();
        this.filesListNumbers = new Vector<>();
        this.dictionary = new HashMap<>();
        this.citiesIndex = new HashMap<>();
    }

    /**
     * This function create a new posting file for the given term map
     *
     * //@param termsMap
     */
    public synchronized void  buildIndex(ConcurrentHashMap<String, ConcurrentHashMap<DocumentDetails, Integer>> termsMap) {
        ConcurrentSkipListMap<String, ConcurrentHashMap<DocumentDetails, Integer>> map = new ConcurrentSkipListMap<>(termsMap);
        String path1 = this.pathToSaveIndex + "/posting" + counter.getAndIncrement() + "numbers.txt";
        String path2 = this.pathToSaveIndex + "/posting" + counter.getAndIncrement() + "upperCase.txt";
        String path3 = this.pathToSaveIndex + "/posting" + counter.getAndIncrement() + "lowerCase.txt";
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
            for (Map.Entry<String, ConcurrentHashMap<DocumentDetails, Integer>> entry : map.entrySet()) {
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
    private void writeToPostingFile(FileWriter writer,String key, Map.Entry<String, ConcurrentHashMap<DocumentDetails, Integer>> entry) {
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
                if (documentDetails.getCityName() != null){
                    CityDetails cityDetails = new CityDetails(documentDetails.getCityName(), documentDetails.getDocId());
                    //getCityDetails(documentDetails.getCityName(),cityDetails);
                    cityDetails.setListOfPositions(documentDetails.getListOfPositions());
                    if (!citiesIndex.containsKey(documentDetails.getCityName())){
                        ArrayList <CityDetails> list = new ArrayList();
                        list.add(cityDetails);
                        citiesIndex.put(documentDetails.getCityName(),list);
                    }
                    else{
                        citiesIndex.get(documentDetails.getCityName()).add(cityDetails);
                    }
                }
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
        Parse.threadPoolExecutor.shutdown();
        while (Parse.threadPoolExecutor.getActiveCount() != 0);
        //Parse.threadPoolExecutor.awaitTermination(Long.MAX_VALUE,TimeUnit.NANOSECONDS);
        // create a new merge posting file which will be contains all the files in the filesList field
        String pathForNumbers = this.pathToSaveIndex + "/mergePostingNumbersTemp.txt";
        String pathForLowerCase = this.pathToSaveIndex + "/mergePostingLowerCaseTemp.txt";
        String pathForUpperCase = this.pathToSaveIndex + "/mergePostingUpperCaseTemp.txt";
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
            Thread thread1 = new Thread(new RunnableExternalSort(this.filesListNumbers,this.postingNumbers,cmp));
            Thread thread2 = new Thread(new RunnableExternalSort(this.filesListNumbers,this.postingNumbers,cmp));
            Thread thread3 = new Thread(new RunnableExternalSort(this.filesListNumbers,this.postingNumbers,cmp));
            thread1.start();
            thread2.start();
            thread3.start();
            thread1.join();
            thread2.join();
            thread3.join();
/*            ExternalSort.mergeSortedFiles(this.filesListNumbers, this.postingNumbers, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListLowerCase, this.postingLower, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListUpperCase, this.postingUpper, cmp, Charset.defaultCharset(), false);*/
            Vector <File> postingFiles = new Vector();
            Vector <String> postingFilesNames = new Vector<>();
            postingFiles.add(this.postingLower);
            postingFilesNames.add(this.pathToSaveIndex + "/mergePostingLowerCase.txt");
            postingFiles.add(this.postingUpper);
            postingFilesNames.add(this.pathToSaveIndex + "/mergePostingUpperCase.txt");
            postingFiles.add(this.postingNumbers);
            postingFilesNames.add(this.pathToSaveIndex + "/mergePostingNumbers.txt");
            Vector <File> newPostingFiles = mergeDuplicateLines(postingFiles,postingFilesNames);
            this.postingLower = newPostingFiles.remove(0);
            this.postingUpper = newPostingFiles.remove(0);
            this.postingNumbers = newPostingFiles.remove(0);
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    dividePostingFileNumbers();
                }
            });
            t1.start();
            checkUpperCase();
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    dividePostingFile();
                }
            });
            t2.start();
            t1.join();
            t2.join();
            createDictionaryFile();
            createCitiesIndexFile();
            // deleteTempFiles();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private void createCitiesIndexFile() {
        try {
            File cityPosting = new File(pathToSaveIndex + "/cityPosting.txt");
            File cityDictionary = new File(pathToSaveIndex + "/cityDictionary.txt");
            cityPosting.createNewFile();
            cityDictionary.createNewFile();
            FileWriter postingWriter = new FileWriter(cityPosting);
            FileWriter dictionaryWriter = new FileWriter(cityDictionary);
            int lineNumber = 0;
            for (Map.Entry<String, ArrayList<CityDetails>> entry : citiesIndex.entrySet()) {
                dictionaryWriter.write(entry.getKey() + " " + lineNumber);
                for (CityDetails cityDetails : entry.getValue()) {
                    postingWriter.write("<DOC>");
                    postingWriter.write(cityDetails.getDocId() + "," + cityDetails.getCountryName() + "," + cityDetails.getCurrency() + "," + cityDetails.getPopulationSize());
                    for (Integer position : cityDetails.getListOfPositions()) {
                        postingWriter.write(position + " ");
                    }
                }
                postingWriter.write("</DOC>");
                postingWriter.write("\n");
                dictionaryWriter.write("\n");
            }
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
            File newLower = new File(this.pathToSaveIndex + "/mergePostingLower.txt");
            File newUpper = new File(this.pathToSaveIndex + "/mergePostingUpper.txt");
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
            File posting = new File (this.pathToSaveIndex + "/postingA.txt");
            File newLower = new File(this.pathToSaveIndex + "/mergePostingLower.txt");
            File newUpper = new File(this.pathToSaveIndex + "/mergePostingUpper.txt");
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
                posting = new File (this.pathToSaveIndex + "/posting" + c + ".txt");
                writer = new FileWriter(posting);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void dividePostingFileNumbers(){

        try{
            char c = '0';
            int lineNumber = 1;
            File posting = new File (this.pathToSaveIndex + "/posting0.txt");
            File numbers = new File(this.pathToSaveIndex + "/mergePostingNumbers.txt");
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
                posting = new File (this.pathToSaveIndex + "/posting" + c + ".txt");
                writer = new FileWriter(posting);
            }

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
            File dictionaryFile = new File(pathToSaveIndex + "/Dictionary.txt");
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
        for(File file: folder.listFiles())
            if (!file.isDirectory())
                file.delete();
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
            File dictionaryFile = new File(pathToSaveIndex + "/Dictionary.txt");
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
     * @param cityName
     * @return
     */
    private void getCityDetails(String cityName, CityDetails cityDetails){
        String api = "http://getcitydetails.geobytes.com/GetCityDetails?fqcn=";
        URL url;
        try {
            url = new URL(api + cityName);
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line = br.readLine();
            String currency = line.substring(line.indexOf("\"geobytescurrencycode\":")+ 24,line.indexOf("geobytestitle")-3);
            String countryName = line.substring(line.indexOf("\"geobytescountry\":")+ 19,line.indexOf("geobytesregionlocation")-3);
            String populationSize = line.substring(line.indexOf("\"geobytespopulation\":")+ 22,line.indexOf("geobytesnationalityplural")-3);
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
            populationSize = temp.toString();
            cityDetails.setCountryName(countryName);
            cityDetails.setCurrency(currency);
            cityDetails.setPopulationSize(populationSize);


        } catch (Exception e) {
        }
    }

    private StringBuilder parsePopulation(String tmpNum){
        return null;
    }

    private class RunnableExternalSort implements Runnable{

        Vector<File> filesList;
        File posting;
        Comparator cmp;

        public RunnableExternalSort(Vector<File> filesList, File posting, Comparator cmp) {
            this.filesList = filesList;
            this.posting = posting;
            this.cmp = cmp;
        }

        @Override
        public void run() {
            try {
                ExternalSort.mergeSortedFiles(this.filesList, this.posting, cmp, Charset.defaultCharset(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
