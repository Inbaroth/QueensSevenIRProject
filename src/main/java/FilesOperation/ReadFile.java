package FilesOperation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;

public class ReadFile{

    File folder;
    Parse parser;

    public static int counter = -1;


    /**
     *
     * @param pathToParse
     */
    public ReadFile(String pathToParse,String pathToSaveIndex,  boolean isStemming) {
        folder = new File(pathToParse);
        parser = new Parse(pathToSaveIndex, isStemming);
        listFilesForFolder(folder);
        parser.notifyDone();
    }

    private void listFilesForFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
                Document document = null;
                counter++;
                try {
                    document = Jsoup.parse(new String (Files.readAllBytes(fileEntry.toPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = document.getElementsByTag("DOC");
                for (Element e: elements) {
                    String docText = e.toString();
                    String docId = e.getElementsByTag("DOCNO").text();
                    parser.parsing(docId,docText,fileEntry.getName(),counter);
                }
            }
        }
    }


    public Parse getParser() {
        return parser;
    }


}
