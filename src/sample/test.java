package sample;

import java.io.File;

public class test {

    public static void main(String[] args) {
        final File f = new File("C:\\Users\\איתן אביטן\\Downloads\\corpus\\corpus");
        listFilesForFolder(f);
    }

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
