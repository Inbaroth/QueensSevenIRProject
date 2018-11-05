package FilesOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test{

    public static void main(String[] args) {
//        String m = "<DOCNO> FBIS3-51 </DOCNO>";
//        String [] s = m.split("<DOCNO>|\\</DOCNO>");
//        String n = s[1].substring(1);
//        System.out.println(n);
        File file = new File("C:\\Users\\איתן אביטן\\Downloads\\corpus");
        listFilesForFolder(file);
    }

    private static void listFilesForFolder(File folder) {
        Parse parser = new Parse();
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
                            System.out.println(docText);
                            parser.parsing(docId,docText,fileEntry.getName());
                            docText = "";
                            line = br.readLine();
                            break;
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
    }

