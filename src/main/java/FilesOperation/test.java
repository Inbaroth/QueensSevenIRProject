package FilesOperation;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test{

    public static void main(String[] args) {
       String line = "<TEXT> for 112 Million company, Vacation4U 2 3/4% is preety bad, on 14 MAY, They spent 55 Trillion, While 320 bn Dollars waisted on drugs, BAD Step-By-Step, 2-4 times. From 10 1/2-11,000,122. Between 12 1/2 and 4,000,123. </TEXT>";
       String line1 = "<TEXT> 12.555 kids. </TEXT>";
       Parse p = new Parse();

       p.parsing("1",line1,"inbar");

        Map<String, DocumentsHashMap> map = p.getTermsMap();

        for (Map.Entry<String,DocumentsHashMap> entry:map.entrySet()) {
            System.out.println(entry.getKey()+ ": " + entry.getValue());

        }





        }



    }

