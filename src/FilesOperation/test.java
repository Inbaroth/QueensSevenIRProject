package FilesOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class test{

    public static void main(String[] args) {
        File file = new File("C:\\Users\\איתן אביטן\\Downloads\\corpus");
        listFilesForFolder(file);
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
