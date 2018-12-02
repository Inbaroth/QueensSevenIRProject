package InvertedIndex;

import FilesOperation.DocumentDetails;
import com.google.code.externalsorting.ExternalSort;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Indexer {
    private AtomicInteger counter = new AtomicInteger(0);
    private String pathToSaveIndex;
    private Vector<File> filesListNumbers;
    private Vector<File> filesListLowerCase;
    private Vector<File> filesListUpperCase;
    public HashSet<String> terms;
    File postingNumbers;
    File postingLower;
    File postingUpper;

    /**
     * Constructor
     */
    public Indexer(String pathToSaveIndex) {
        this.pathToSaveIndex = pathToSaveIndex;
        this.filesListLowerCase = new Vector<>();
        this.filesListUpperCase = new Vector<>();
        this.filesListNumbers = new Vector<>();
        this.terms = new HashSet<>();
    }

    /**
     * This function create a new posting file for the given term map
     *
     * @param termsMap
     */
    public synchronized void  buildIndex(HashMap<String, HashMap<DocumentDetails, Integer>> termsMap) {
        Map<String, HashMap<DocumentDetails, Integer>> map = new TreeMap<>(termsMap);
        String path1 = "src/main/resources/posting" + counter.getAndIncrement() + "numbers.txt";
        String path2 = "src/main/resources/posting" + counter.getAndIncrement() + "upperCase.txt";
        String path3 = "src/main/resources/posting" + counter.getAndIncrement() + "lowerCase.txt";
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
                terms.add(key);
                if (Character.isUpperCase(key.charAt(0))) {
                    writeToPostingFile(writer2,key,entry);
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

    private void writeToPostingFile(FileWriter writer,String key, Map.Entry<String, HashMap<DocumentDetails, Integer>> entry) {
        try {
            writer.write(key + " ");
            for (Map.Entry<DocumentDetails, Integer> doc : entry.getValue().entrySet()) {
                writer.write("<DOC>");
                //writer.write("\n");
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
                //writer.write("\n");
                writer.write("</DOC>");
            }
            //writer.write("\n");
            //writer.write("</" + entry.getKey() + ">");
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method merge five posting files which created by buildIndex() function in every call
     */
    public void mergePostingFile() {
        // create a new merge posting file which will be contains all the files in the filesList field
        String pathForNumbers = "src/main/resources/mergePostingNumbersTemp.txt";
        String pathForLowerCase = "src/main/resources/mergePostingLowerCaseTemp.txt";
        String pathForUpperCase = "src/main/resources/mergePostingUpperCaseTemp.txt";
        this.postingNumbers = new File(pathForNumbers);
        this.postingLower = new File(pathForLowerCase);
        this.postingUpper = new File(pathForUpperCase);
        try {
            postingNumbers.createNewFile();
            postingLower.createNewFile();
            postingUpper.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        try {
            ExternalSort.mergeSortedFiles(this.filesListNumbers, this.postingNumbers, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListLowerCase, this.postingLower, cmp, Charset.defaultCharset(), false);
            ExternalSort.mergeSortedFiles(this.filesListUpperCase, this.postingUpper, cmp, Charset.defaultCharset(), false);
            Vector <File> postingFiles = new Vector();
            Vector <String> postingFilesNames = new Vector<>();
            postingFiles.add(this.postingLower);
            postingFilesNames.add( "src/main/resources/mergePostingLowerCase.txt");
            postingFiles.add(this.postingUpper);
            postingFilesNames.add( "src/main/resources/mergePostingUpperCase.txt");
            postingFiles.add(this.postingNumbers);
            postingFilesNames.add( "src/main/resources/mergePostingNumbers.txt");
            mergeDuplicateLines(postingFiles,postingFilesNames);
            checkUpperCase();
            dividePostingFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkUpperCase();
    }

    private void mergeDuplicateLines(Vector<File> postingFiles, Vector<String> postingFilesNames) {
        try {
            for (File file: postingFiles) {
                File newMergeFile = new File(postingFilesNames.remove(0));
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
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    /**
     *
     */
    private void checkUpperCase() {

        try {
            File newLower = new File("src/main/resources/MergePostingLower.txt");
            File newUpper = new File("src/main/resources/MergePostingUpper.txt");
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
                // write the line to the new lower words posting file
                writerLower.write(lineLower);
                // if the term exist also in upper case, copy the content of the line to the new lower words posting file
                if (lineUpperSplit[0].toLowerCase().equals(lineLowerSplit[0])){
                    lineUpper = lineUpper.substring(lineUpperSplit[0].length());
                    writerLower.write(lineUpper);
                }
                else{
                    writerUpper.write(lineUpper);
                    writerUpper.write("\n");
                }
                writerLower.write("\n");
                lineUpper = bfUpper.readLine();
                lineLower = bfLower.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dividePostingFile(){

        try{
            char c = 'A';
            File posting = new File ("src/main/resources/postingA.txt");
            File newLower = new File("src/main/resources/MergePostingLowerCase.txt");
            File newUpper = new File("src/main/resources/MergePostingUpperCase.txt");
            BufferedReader bfUpper = new BufferedReader(new FileReader(newLower));
            BufferedReader bfLower = new BufferedReader(new FileReader(newUpper));
            FileWriter writer = new FileWriter(posting);

            String lineUpper = bfUpper.readLine();
            String lineLower = bfLower.readLine();

            while (true){
                while (lineUpper != null && lineUpper.charAt(0) == Character.toUpperCase(c)){
                    writer.write(lineUpper);
                    writer.write("\n");
                    lineUpper = bfUpper.readLine();
                }
                while (lineLower != null && lineLower.charAt(0) == c){
                    writer.write(lineLower);
                    writer.write("\n");
                    lineLower = bfLower.readLine();
                }
                c++;
                if (c == '[')
                    break;
                posting = new File ("src/main/resources/posting" + c + ".txt");
                writer = new FileWriter(posting);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
