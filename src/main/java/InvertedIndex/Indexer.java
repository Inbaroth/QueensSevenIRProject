package InvertedIndex;

import FilesOperation.DocumentDetails;
import com.google.code.externalsorting.ExternalSort;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


public class Indexer {
    private static int counter = 0;
    private Vector<File> filesList;
    private File posting;
    private static int mergeCounter = 0;

    /**
     * Constructor
     */
    public Indexer() {
        // vector list for the posting files
        // keep five posting files max
        this.filesList = new Vector<>();
    }

    /**
     * This function create a new posting file for the given term map
     *
     * @param termsMap
     */
    public void buildIndex(Map<String, HashMap<DocumentDetails, Integer>> termsMap) {
        Map<String, HashMap<DocumentDetails, Integer>> map = new TreeMap<>(termsMap);
        String path = "src/main/resources/posting" + counter + ".txt";
        File posting = new File(path);
        try {
            posting.createNewFile();
            this.filesList.add(posting);
            FileWriter writer = new FileWriter(posting);
            for (Map.Entry<String, HashMap<DocumentDetails, Integer>> entry : map.entrySet()) {
                // write the term to the posting file
                writer.write("<" + entry.getKey() + ">");
                //writer.write("\n");
                for (Map.Entry<DocumentDetails, Integer> doc : entry.getValue().entrySet()) {
                    writer.write("<DOC>");
                    //writer.write("\n");
                    // write doc detailed for each document in the format:
                    // DocId, file name, tf, max tf, city name,
                    DocumentDetails documentDetailes = doc.getKey();
                    writer.write(documentDetailes.getDocId() + " ");
                    writer.write(documentDetailes.getFileName() + " ");
                    writer.write(documentDetailes.getTermFrequency() + " ");
                    writer.write(documentDetailes.getMaxTermFrequency() + " ");
                    writer.write(documentDetailes.getCityName() + " ");
                    //writer.write("\n");
                    writer.write("</DOC>");
                }
                //writer.write("\n");
                writer.write("</" + entry.getKey() + ">");
                writer.write("\n");
            }
            writer.close();
            counter++;
            if (counter % 5 == 0) {
                mergePostingFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method merge five posting files which created by buildIndex() function in every call
     */
    private void mergePostingFile() {
        // create a new merge posting file which will be contains all the files in the filesList field
        String path = "src/main/resources/mergePosting" + mergeCounter + ".txt";
        this.posting = new File(path);
        try {
            posting.createNewFile();
            mergeCounter++;
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
            ExternalSort.mergeSortedFiles(this.filesList, this.posting, cmp, Charset.defaultCharset(), false);
            // reset the vector list
            this.filesList = new Vector<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
