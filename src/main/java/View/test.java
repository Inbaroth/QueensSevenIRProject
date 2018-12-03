package View;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class test {

    public static void main(String[] args) {
//        //final File f = new File("C:\\Users\\איתן אביטן\\Downloads\\corpus\\corpus");
//        //listFilesForFolder(f);
//        String sss = "";
//        System.out.println(sss.length());
//        String str = "This is String 1.22. split by StringTokenizer. created by me.";
//        StringTokenizer st = new StringTokenizer(str);
//        String string = "Inbar Mor Gal Dor";
//        int x = string.indexOf(' ');
//        int y = string.indexOf(' ', x+1);
//        System.out.println(x + " " +y);
//
//
//        System.out.println("---- Split by space ------");
//        while (st.hasMoreElements()) {
//           // System.out.println(st.nextElement());
//
//        }
//
//        System.out.println("---- Split by comma '.' ------");
//        StringTokenizer st2 = new StringTokenizer(str, "\\.");
//
//        while (st2.hasMoreElements()) {
//           // System.out.println(st2.nextElement());
        //     }

        StringBuilder s = new StringBuilder("1.2222");
        s.delete(s.indexOf(".") + 3,s.length());
        System.out.println(s);
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

    public static String[] split(String line, char delimiter)
    {
        if(line.equals(" ")) {
            String[] result = {""};
            return result;
        }
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter, 0); // first substring

        while (j >= 0)
        {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }
}
