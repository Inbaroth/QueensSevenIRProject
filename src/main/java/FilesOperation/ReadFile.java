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
        List<String> allFiles = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
                System.out.println(fileEntry.getName());
            } else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line;
                    String file = "";
                    line = br.readLine();
                    while (line != null){
                        if (!line.equals("</Docs>")){
                            file += line;
                            line = br.readLine();
                        }
                        else{

                            file = "";
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
