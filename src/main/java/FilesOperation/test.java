package FilesOperation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.text.BreakIterator;
import java.util.*;

public class test{

    public static void main(String[] args) {

        //String docNameFileName = docId + "_" + fileName;
        //take only text between <Text> and </Text>

       String line = "<TEXT> for 112 million company-c, 2, o95 Vacation4U 2 3/4% is preerty bad, on 14 MAY, They spent 2 Trillion, While 320 bn Dollars waisted on drugs. </TEXT>";
       String line1 = "<TEXT> 1/2 million for. </TEXT>";
       Parse p = new Parse();

       p.parsing("1",line1,"inbar");

        //Map<String, DocumentsHashMap> map = p.getTermsMap();
        Map<String, Integer> map = new TreeMap<>();
        map.put("Mor",1);
        map.put("Inbar", map.get("Mor"));
        map.remove("Mor");


        for (Map.Entry<String,Integer> entry:map.entrySet()) {
            System.out.println(entry.getKey()+ ": " + entry.getValue());

        }

//        for (Map.Entry<String,DocumentsHashMap> entry:map.entrySet()) {
//            System.out.println(entry.getKey()+ ": " + entry.getValue());
//
//        }





        }



    }

