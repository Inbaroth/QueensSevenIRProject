package FilesOperation;

import java.io.*;
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
                try {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line;
                    String docText = "";
                    String docId = "";
                    line = br.readLine();
                    while (line != null){
                        if (line.contains("<DOCNO>")){
                            String [] splitedDocNumber = line.split("<DOCNO>|\\</DOCNO>");
                            docId = splitedDocNumber[1].substring(1,splitedDocNumber[1].length() - 1);
                        }
                        if (!line.equals("</DOC>")){
                            docText += line;
                            line = br.readLine();
                        }
                        else{
                            docText += line;
                            parser.parsing(docId,docText,fileEntry.getName());
                            docText = "";
                            line = br.readLine();
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
