package FilesOperation;

import java.io.*;

public class ReadFile {

    File folder;
    Parse parser;


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
                    String file = "";
                    line = br.readLine();
                    while (line != null){
                        if (!line.equals("</Docs>")){
                            file += line;
                            line = br.readLine();
                        }
                        else{
                            parser.parser("",file);
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
