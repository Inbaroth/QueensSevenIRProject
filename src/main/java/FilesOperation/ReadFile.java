package FilesOperation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ReadFile extends Thread{

    File folder;
    Parse parser;

    private static int counter = 0;



    /**
     *
     * @param path
     */
    public ReadFile(String path, boolean isStemming) {
        folder = new File(path);
        parser = new Parse(isStemming);
        listFilesForFolder(folder);
    }

    private void listFilesForFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
                Document document = null;
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
                counter++;
                if (counter == 50)
                    counter = 0;
            }
        }
    }


    public Parse getParser() {
        return parser;
    }
}
