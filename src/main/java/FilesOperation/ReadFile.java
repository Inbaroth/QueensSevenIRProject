package FilesOperation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    File folder;
    Parse parser;
    String filesDirectories;


    public ReadFile(String path) {
        folder = new File(path);
        parser = new Parse();
        listFilesForFolder(folder);
    }

    private void listFilesForFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                Document document = null;
                try {
                    document = Jsoup.parse(new String (Files.readAllBytes(fileEntry.toPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = document.getElementsByTag("DOC");
                for (Element e: elements) {
                    String docText = e.text();
                    String docId = e.getElementsByTag("DOCNO").text();
                    parser.parsing(docId,docText,fileEntry.getName());
                }

            }
        }
    }
}
