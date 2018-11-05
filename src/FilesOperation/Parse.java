package FilesOperation;

import com.sun.deploy.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class Parse {

    // String - term
    Map <String, DocumentsHashMap> termsMap;

    public Parse() {
        this.termsMap = new TreeMap<>();
    }


    public void parsing( String docId, String docText, String fileName){

        //values , key + value of DocumentsHashMap
        String docNameFileName = docId + "_" + fileName;
        int numOfWordAppearance = 0;



        //split all words by whitespace into array of words
        String [] wordsInDoc = docText.trim().split("\\s+");

        for( String str : wordsInDoc){
            //check if it's a tag or
        }



    }



}
