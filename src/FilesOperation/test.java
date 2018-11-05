package FilesOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class test{

    public static void main(String[] args) {
       // File file = new File("C:\\Users\\איתן אביטן\\Downloads\\corpus");
        //listFilesForFolder(file);

        String str = "this is my string";
        String str2 = " this is my  string ";
        String str3 = "<Text>this is my  string</Text>";

        String [] sample  = str3.split("<Text>|\\</Text>");


        String [] smple = str2.trim().split("\\s+");
        for(String strx : sample){
        //System.out.println(strx);
        }


        String s = str3.substring(str3.indexOf("<Text>"), str3.indexOf("</Text>"));
        System.out.println(s);






    }

    private static void listFilesForFolder(File folder) {
        List<String> allFiles = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("D:" + fileEntry.getName());
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
