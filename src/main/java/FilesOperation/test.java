package FilesOperation;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test{

    public static void main(String[] args) {
       String line = "<TEXT> for $112,000,000,000 company, Vacation4U is preerty bad, on 14 MAY, They spent 2 trillion, While 320 bn Dollars waisted on drugs. </TEXT>";
       Parse p = new Parse();

       p.parsing("1",line,"inbar");

        Map<String, DocumentsHashMap> map = p.getTermsMap();

        for (Map.Entry<String,DocumentsHashMap> entry:map.entrySet()) {
            System.out.println(entry.getKey()+ ": " + entry.getValue());

        }
        }



    }

