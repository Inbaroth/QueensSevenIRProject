package FilesOperation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Parse {

    // String - term
    Map <String, DocumentsHashMap> termsMap;

    public Parse() {
        this.termsMap = new TreeMap<>();
    }


    public void parsing( String docId, String docText, String fileName){
        //values , key + value of DocumentsHashMap
        String docNameFileName = docId + "_" + fileName;
        //take only text between <Text> and </Text>
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        Document document = Jsoup.parse(docText);
        String text = document.getElementsByTag("TEXT").text();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            parsingLine(docId,text.substring(start,end),docNameFileName);
        }
    }

    private void parsingLine(String docId, String docLine, String docNameFileName){

        //split all words by whitespace into array of words
        String [] wordsInDoc = docLine.trim().split("\\s+");
        wordsInDoc[wordsInDoc.length-1] = wordsInDoc[wordsInDoc.length-1].substring(0,wordsInDoc[wordsInDoc.length-1].length()-1);
        for (int i = 0; i < wordsInDoc.length; i++) {

            //check if it's a tag symbol city: <F P=104>
            if( (i+1)<wordsInDoc.length && wordsInDoc[i].equals("<F") && wordsInDoc[i+1].equals("P=104>")){
                String cityName = wordsInDoc[i+2];
                updateTerm(cityName, docNameFileName);
                continue;
            }

            //check if the word in the array ends with ','
            boolean isPair = true;
            if(wordsInDoc[i].charAt(wordsInDoc[i].length()-1)== ',') {
                isPair = false;
                wordsInDoc[i] = wordsInDoc[i].substring(0,wordsInDoc[i].length()-1);
            }
            //if it is NOT a tag , doesn't starts with '<'
            if(!((wordsInDoc[i].charAt(0)== '<') || wordsInDoc[i].charAt(wordsInDoc[i].length()-1) == '>')){

                //regex for terms like: 0,0 | 0% | $0 | 0.0 | 0/0
                String numbersRegex = "[$]*[0-9]+[0-9,]*[0-9.]*[0-9/]*[0-9]*[0-9%]*";
                String numberSize = "(Thousand|Million|Billion|Trillion)";
                Vector<StringBuilder> numberTerm = new Vector<>();

                //if a term kind: Between number and number like: Between 10 and 12
                if(i+1<wordsInDoc.length && wordsInDoc[i].equalsIgnoreCase("between") && wordsInDoc[i+1].matches(numbersRegex)){
                    int advanceI = i;
                    boolean fraction = false;
                    //Between 1 and 3 || Between 1/3 and 3/4 || Between 1 and 3.5 ,saved at dic as 1-3
                    if(i+3<wordsInDoc.length && wordsInDoc[i+2].equalsIgnoreCase("and") && wordsInDoc[i+3].matches(numbersRegex)){
                        numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        numberTerm.add(new StringBuilder(wordsInDoc[i+3]));
                        advanceI = i+3;
                        //Between 1 and 3 1/2
                        if(i+4<wordsInDoc.length && wordsInDoc[i+4].matches(numbersRegex)){
                            numberTerm.add(new StringBuilder(wordsInDoc[i+4]));
                            fraction =true;
                            advanceI = advanceI +1;
                        }
                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        term1.append("-" + term2);
                        updateTerm(term1.toString(),docNameFileName);
                        i = advanceI;
                        continue;
                    }
                    //Between 1 1/2 and number || Between 1 Million and number
                    if(i+4<wordsInDoc.length && (wordsInDoc[i+2].matches(numbersRegex) || wordsInDoc[i+2].matches(numberSize)) && wordsInDoc[i+3].equalsIgnoreCase("and") && wordsInDoc[i+4].matches(numbersRegex)){
                        numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                        numberTerm.add(new StringBuilder(wordsInDoc[i+2]));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, wordsInDoc[i+2].matches(numbersRegex));
                        numberTerm.clear();
                        numberTerm.add(new StringBuilder(wordsInDoc[i+4]));
                        advanceI = i+4;
                        //Between 1 1/2 and 1 3/4
                        if(i+5<wordsInDoc.length && wordsInDoc[i+5].matches(numbersRegex)){
                            numberTerm.add(new StringBuilder(wordsInDoc[i+5]));
                            fraction=true;
                            advanceI = advanceI +1;
                        }
                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        term1.append("-" + term2);
                        updateTerm(term1.toString(),docNameFileName);
                        i = advanceI;
                        continue;
                    }
                }


                //if it's a date
                String monthRegex = "(January|February|March|April|May|June|July|August|September|October|November|December|" +
                        "JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER" +
                        "Jan|Feb|Mar|Apr|June|July|Aug|Sept|Oct|Nov|Dec)";
                String numRegex = "[0-9]+";

                //first Month Name then Number
                if(wordsInDoc[i].matches(monthRegex)){
                    //insert to dic also Month name by itself
                    updateTerm(wordsInDoc[i], docNameFileName);
                    if(isPair) {
                        wordsInDoc[i+1] = wordsInDoc[i+1].replaceAll(",", "");
                        if (i + 1 < wordsInDoc.length && wordsInDoc[i + 1].matches(numRegex)) {
                            //change to date format: MM-DD and insert to dic
                            StringBuilder date = convertToDateFormat(wordsInDoc[i], wordsInDoc[i + 1]);
                            updateTerm(date.toString(), docNameFileName);
                            i = i + 1;
                            continue;
                        }
                    }
                }
                //first Number the Month Name
                if(i+1<wordsInDoc.length && wordsInDoc[i].matches(numRegex)){
                    wordsInDoc[i+1] = wordsInDoc[i+1].replaceAll(",","");
                    if(wordsInDoc[i+1].matches(monthRegex)){
                        //insert to dic month name alone
                        updateTerm(wordsInDoc[i+1], docNameFileName);
                        //change date like 14 May to 05-14
                        StringBuilder date = convertToDateFormat(wordsInDoc[i+1], wordsInDoc[i]);
                        updateTerm(date.toString(),docNameFileName );
                        i = i+1;
                        continue;
                    }
                }

                //check if it is an Integer like: 0,0 | 0% | $0 | 0.0 | 0/0
                boolean twoNumsCells = false;
                if(wordsInDoc[i].matches(numbersRegex)){
                    //if the cell i+1 also part of the number
                    if(isPair && i+1<wordsInDoc.length){
                        wordsInDoc[i + 1] = removeLastComma(wordsInDoc[i+1]);
                        //handle term like: 2 1/2-2 3/4 | 2 Million-4 Million
                        int makafIndex =wordsInDoc[i+1].indexOf('-');
                        if(makafIndex!=-1) {
                            int advanceI = i+1;
                            String beforeMakaf = wordsInDoc[i+1].substring(0, makafIndex);
                            String afterMakaf = wordsInDoc[i+1].substring(makafIndex+1);
                            //million-22
                            //1/2-22 || 1/2-3/4
                            String numberMakaf = "[0-9]*[0-9/]+[0-9]";
                            if (beforeMakaf.matches(numberSize) || beforeMakaf.matches(numberMakaf)) {
                                numberTerm.add(new StringBuilder(wordsInDoc[i]));
                                numberTerm.add(new StringBuilder(beforeMakaf));
                                StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, beforeMakaf.contains("/"));
                                numberTerm.clear();
                                if (afterMakaf.matches(numbersRegex)) {
                                    numberTerm.add(new StringBuilder(afterMakaf));
                                    if (i+2<wordsInDoc.length && (wordsInDoc[i+2].matches(numberSize) || wordsInDoc[i+2].matches(numberMakaf))) {
                                        numberTerm.add(new StringBuilder(wordsInDoc[i + 2]));
                                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, wordsInDoc[i + 2].contains("/"));
                                        numberTerm.clear();
                                        term1.append("-" + term2.toString());
                                        updateTerm(term1.toString(), docNameFileName);
                                        advanceI = advanceI+1;
                                    }else{
                                        StringBuilder afterMN = parseNumbers(false, docNameFileName,numberTerm,false);
                                        term1.append("-" + afterMN);
                                        updateTerm(term1.toString(), docNameFileName);
                                    }

                                }else {
                                    term1.append("-" + afterMakaf);
                                    updateTerm(term1.toString(), docNameFileName);
                                }
                                i = advanceI;
                                continue;

                            }
                        }
                        if (wordsInDoc[i + 1].matches(numbersRegex)) {
                            twoNumsCells = true;
                        }
                    }

                    StringBuilder numTerm = new StringBuilder( wordsInDoc[i]);

                    boolean isDollar = false;

                    if(wordsInDoc[i].charAt(0)=='$' ) {
                        isDollar = true;
                        numTerm.delete(0,1);
                        numberTerm.add(numTerm);
                    }
                    //$price million/billion/trillion
                    if(i+1<wordsInDoc.length&& isDollar && wordsInDoc[i+1].matches("(million|billion|trillion)[,]*")) {
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        updateTerm(wordsInDoc[i+1].toLowerCase(),docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true,docNameFileName,numberTerm,twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(),docNameFileName);
                        i = i+1;
                        continue;
                    }

                    //$price
                    if(isDollar){
                        //call to func which deal with prices
                        parseNumbers(true,docNameFileName,numberTerm,twoNumsCells);
                        numberTerm.clear();
                        continue;
                    }

                    //price Dollars/Dollar/dollar/dollars
                    if (i+1<wordsInDoc.length&&!isDollar && wordsInDoc[i+1].matches("(Dollars|Dollar|dollar|dollars)[,]*")){
                        numberTerm.add(numTerm);
                        numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                        //update dic with Dollars/Dollar/dollar/dollars
                        updateTerm(wordsInDoc[i+1].toLowerCase(),docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true,docNameFileName,numberTerm,twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(),docNameFileName);
                        i = i+1;
                        continue;
                    }

                    //price m/bn/fraction Dollars/Dollar/dollar/dollars
                    if (i+2<wordsInDoc.length&& !isDollar && wordsInDoc[i+2].matches("(Dollars|Dollar|dollar|dollars)[,]*")){
                        if(wordsInDoc[i+1].matches("(m|bn)") || twoNumsCells) {
                            numberTerm.add(numTerm);
                            numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                            //update dic with Dollars/Dollar/dollar/dollars
                            updateTerm(wordsInDoc[i+1].toLowerCase(),docNameFileName);
                            //call to func which deal with prices
                            StringBuilder finalTerm = parseNumbers(true,docNameFileName,numberTerm,twoNumsCells);
                            numberTerm.clear();
                            updateTerm(finalTerm.toString(),docNameFileName);
                            i = i+2;
                            continue;
                        }
                    }

                    //price million/billion/trillion U.S. Dollars/Dollar/dollar/dollars
                    else if (i+1<wordsInDoc.length && !isDollar && wordsInDoc[i+1].matches("(million|billion|trillion)[,]*") && wordsInDoc[i+2].equals("U.S.") && wordsInDoc[i+3].matches("(Dollars|Dollar|dollar|dollars)")){
                        numberTerm.add(numTerm);
                        numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                        //update dic with Dollars/Dollar/dollar/dollars
                        updateTerm(wordsInDoc[i+1].toLowerCase(),docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true,docNameFileName,numberTerm,twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(),docNameFileName);
                        i = i+3;
                        continue;

                    }

                    int cellNum = i;
                    //if like 22 3/4 marge to one cell
                    if(twoNumsCells) {
                        cellNum = i + 1;
                        numTerm.append( " "+wordsInDoc[i+1]);
                    }

                    //if ends with '%'
                    if(wordsInDoc[cellNum].charAt(wordsInDoc[cellNum].length()-1) == '%' ) {
                        String numTermStr = numTerm.toString();
                        updateTerm(numTermStr,docNameFileName );
                        if (twoNumsCells)
                            i = i + 1;
                        continue;
                    }

                    //if appears percent / percentage / Percent / Percentage /  percents / percentages / Percents / Percentages at the cell after
                    String percentRegex = "(percent|percentage|Percent|Percentage|percents|percentages|Percents|Percentages)";
                    if(wordsInDoc[cellNum+1].matches(percentRegex)){
                        if(twoNumsCells) {
                            i = i+1;
                        }
                        numTerm.append("%");
                        updateTerm(numTerm.toString(),docNameFileName );
                        updateTerm(wordsInDoc[cellNum+1],docNameFileName);

                        i = i+1;
                        continue;
                    }

                    numberTerm.add(new StringBuilder(wordsInDoc[i]));
                    if(twoNumsCells || wordsInDoc[i+1].matches(numberSize)){
                        numberTerm.add(new StringBuilder(wordsInDoc[i+1]));
                    }
                    StringBuilder finalTerm = parseNumbers(false,docNameFileName,numberTerm,twoNumsCells);
                    numberTerm.clear();
                    updateTerm(finalTerm.toString(),docNameFileName);
                    if (twoNumsCells)
                        i = i+1;
                    continue;
                }

                //handle Word-Word || Word-Word-Word
                int indxMakaf = wordsInDoc[i].indexOf('-');
                if(indxMakaf!=-1 && !StringUtils.isNumeric(wordsInDoc[i])){
                    if(!StringUtils.isNumeric(wordsInDoc[i].substring(indxMakaf+1))){
                        updateTerm(wordsInDoc[i],docNameFileName);
                    }
                }

                //handle number-number like 2,200-2,400 || 120-120.5 or number-word like: 2-kids, 2.5-people
                int makafIndex =wordsInDoc[i].indexOf('-');
                if(makafIndex!=-1) {
                    String beforeMakaf = wordsInDoc[i].substring(0, makafIndex);
                    String afterMakaf = wordsInDoc[i].substring(makafIndex + 1);
                    //handle number-number like: 1/2-3/4 || 2-2.5
                    if(beforeMakaf.matches(numbersRegex)){
                        numberTerm.add(new StringBuilder(beforeMakaf));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, false);
                        numberTerm.clear();
                        if(afterMakaf.matches(numbersRegex)) {
                            numberTerm.add(new StringBuilder(afterMakaf));
                            //1-1 1/2 || 1-2 Million
                            if (i + 1 < wordsInDoc.length && wordsInDoc[i + 1].matches(numbersRegex) || wordsInDoc[i + 1].matches(numberSize)) {
                                numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                                twoNumsCells = true;
                                i= i+1;
                            }
                            StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, twoNumsCells);
                            term1.append("-" + term2);
                            numberTerm.clear();
                            updateTerm(term1.toString(), docNameFileName);
                            continue;
                        }
                        //number-word like 2-girls
                        else{
                            term1.append("-"+ afterMakaf);
                            updateTerm(term1.toString(), docNameFileName);
                            continue;
                        }
                    }


                }

                // the word doesn't exist in the dictionary starting with LOWER and UPPER
                if(!termsMap.containsKey(wordsInDoc[i]) && !termsMap.containsKey(wordsInDoc[i].toLowerCase())){
                    updateTerm(wordsInDoc[i],docNameFileName);
                    continue;
                }
                //WORD starts with UPPER and exist in dic with LOWER, update dic with LOWER CASE of WORD
                if(Character.isUpperCase(wordsInDoc[i].charAt(0)) && termsMap.containsKey(wordsInDoc[i].toLowerCase())){
                    updateTerm(wordsInDoc[i].toLowerCase(),docNameFileName);
                    continue;
                }

                //WORD starts with LOWER and exist in dic with UPPER, update dic with LOWER CASE of WORD and update key to LOWER
                //change a word which starts with lower case to same word now starts with upper case
                StringBuilder startWithUpp=new StringBuilder("" + Character.toUpperCase(wordsInDoc[i].charAt(0))).append(new StringBuilder(wordsInDoc[i].substring(1)));
                if(!Character.isUpperCase(wordsInDoc[i].charAt(0)) && termsMap.containsKey(startWithUpp.toString())){
                    termsMap.put(wordsInDoc[i], termsMap.get(startWithUpp.toString()));
                    int currentNumOfAppearance = (termsMap.get(wordsInDoc[i])).get(docNameFileName);
                    (termsMap.get(wordsInDoc[i])).put(docNameFileName,currentNumOfAppearance+1);
                    termsMap.remove(startWithUpp.toString());
                    continue;
                }
                //covers case of starts only with UPPER
                updateTerm(wordsInDoc[i],docNameFileName);
            }

        }
    }

    /**
     * gets date like: May 15 , Jun 1994 convert to: 05-15 , 1994-01
     * @param monthName variations of month name
     * @param number
     * @return date in format: num-num
     */
    private StringBuilder convertToDateFormat(String monthName , String number) {
        StringBuilder date = new StringBuilder();
        int num = Integer.valueOf(number);
        String month = "";
        while (true) {
            String monthRegerx = "(JANUARY|January|Jan)";
            if (monthName.matches(monthRegerx)) {
                month = "01";
                break;
            }
            monthRegerx = "(FEBRUARY|February|Feb)";
            if (monthName.matches(monthRegerx)) {
                month = "02";
                break;
            }
            monthRegerx = "(MARCH|March|Mar)";
            if (monthName.matches(monthRegerx)) {
                month = "03";
                break;

            }
            monthRegerx = "(APRIL|April|Apr)";
            if (monthName.matches(monthRegerx)) {
                month = "04";
                break;
            }
            monthRegerx = "(MAY|May)";
            if (monthName.matches(monthRegerx)) {
                month = "05";
                break;
            }
            monthRegerx = "(JUNE|June)";
            if (monthName.matches(monthRegerx)) {
                month = "06";
                break;
            }
            monthRegerx = "(JULY|July)";
            if (monthName.matches(monthRegerx)) {
                month = "07";
                break;
            }
            monthRegerx = "(AUGUST|August|Aug)";
            if (monthName.matches(monthRegerx)) {
                month = "08";
                break;
            }
            monthRegerx = "(SEPTEMBER|September|Sept)";
            if (monthName.matches(monthRegerx)) {
                month = "09";
                break;
            }
            monthRegerx = "(OCTOBER|October|Oct)";
            if (monthName.matches(monthRegerx)) {
                month = "10";
                break;
            }
            monthRegerx = "(NOVEMBER|November|Nov)";
            if (monthName.matches(monthRegerx)) {
                month = "11";
                break;
            }
            monthRegerx = "(DECEMBER|December|Dec)";
            if (monthName.matches(monthRegerx)) {
                month = "12";
                break;
            }
        }
        // year
        if (num > 31){
            date.append(number + "-" + month);
            return date;
        }
        // day between 0 and 10
        else if (num > 0 && num < 10){
            StringBuilder smallNum = new StringBuilder("0");
            smallNum.append(number);
            date.append(month + "-" + smallNum.toString());
            return date;
        }
        else if (num >= 10){
            date.append(month + "-" + number);
            return date;
        }
        return null;
    }

    /**
     * create term if not exist, update the dictionary of docs
     * @param key - the name of city under tag <F P=104>
     * @param docNameFileName - name of document where tag is under, name of file where doc is under
     */
    private void updateTerm(String key, String docNameFileName ){
        //check if term not exist in dictionary , add key to TreeMapDic and create it's dic of docs
        if(!termsMap.containsKey(key)){
            DocumentsHashMap documentsHashMap = new DocumentsHashMap();
            documentsHashMap.put(docNameFileName, 1);
            termsMap.put(key.toUpperCase(),documentsHashMap);
            //key exist , update value.
        } else {
            int currentNumOfAppearance = (termsMap.get(key)).get(docNameFileName);
            (termsMap.get(key)).put(docNameFileName,currentNumOfAppearance+1);
        }
    }


    /**
     *
     * @param str
     * @return
     */
    private StringBuilder allCharactersZero(StringBuilder str)
    {
        boolean ans = true;
        int dotIndex = str.indexOf(".");
        String temp = str.toString();
        StringBuilder strTmp = str.delete(0,dotIndex + 1);
        for (int i = 1; i < strTmp.length(); i++) {
            if (strTmp.charAt(i) != '0') {
                ans = false;
                break;
            }
        }
        if (ans){
            str.replace(0,str.length(),temp);
            str.delete(dotIndex,str.length());
            return str;
        }
        return str.replace(0,str.length(),temp);
    }



    /**
     * if the word ends with comma, returns the word without the comma
     * else returns the word
     * for example:word= 1,000,000, returns: 1,000,000
     * @param word
     * @return
     */
    private String removeLastComma(String word){
        if(word.charAt(word.length()-1)== ',') {
            word= word.substring(0,word.length()-1);
        }
        return word;
    }

    /**
     * handle numbers and prices
     * @param isPrice - if the new term is a price definition or just a number
     * @param docNameFileName
     * @param numberTerm - vector which contains words after split
     * @param twoNumsCells - if the number is : number fraction
     */
    private StringBuilder parseNumbers(boolean isPrice, String docNameFileName , Vector<StringBuilder> numberTerm, boolean twoNumsCells ){
        int priceTermSize = numberTerm.size();
        StringBuilder finalTerm =  new StringBuilder();
        while(true) {
            if (!twoNumsCells && priceTermSize >= 2) {
                if (numberTerm.elementAt(1).toString().matches("(m|million|Million)[,]*")) {
                    //$Price million/m || Price million/m Dollars/U.S. dollars
                    if(isPrice)
                        finalTerm.append(numberTerm.elementAt(0)).append(" M Dollars");
                        //number Million
                    else
                        finalTerm.append(numberTerm.elementAt(0)).append("M");
                } else if (numberTerm.elementAt(1).toString().matches("(bn|billion|Billion)[,]*")) {
                    //$Price billion/bn || Price billion/bn Dollars/U.S. dollars
                    if (isPrice)
                        finalTerm.append(numberTerm.elementAt(0)).append("000 M Dollars");
                        //number Billion
                    else
                        finalTerm.append(numberTerm.elementAt(0)).append("B");
                } else if (numberTerm.elementAt(1).toString().matches("(trillion|Trillion)[,]*")) {
                    //$Price trillion || Price trillion Dollars/U.S. dollars
                    if(isPrice)
                        finalTerm.append(numberTerm.elementAt(0)).append("000000 M Dollars");
                        //number Trillion
                    else
                        finalTerm.append(numberTerm.elementAt(0)).append("000B");
                    //number Thousand
                } else if(numberTerm.elementAt(1).toString().matches("(Thousand)[,]*")){
                    if(!isPrice)
                        finalTerm.append(numberTerm.elementAt(0)).append("K");
                }
                break;
            }
            //handle number fraction
            else if(twoNumsCells){
                if(isPrice)
                    finalTerm.append(numberTerm.elementAt(0)).append(" " + numberTerm.elementAt(1)).append(" Dollars");
                else
                    finalTerm.append(numberTerm.elementAt(0)).append(" " + numberTerm.elementAt(1));
                break;
            }

            String tmpNum = numberTerm.elementAt(0).toString();
            tmpNum = tmpNum.replaceAll("," , "");
            //if smaller than million
            if (!isPrice && tmpNum.length() < 5){
                finalTerm.append(numberTerm.elementAt(0));
                break;
            }
            if (tmpNum.length() < 7) {
                if(isPrice)
                    finalTerm.append(numberTerm.elementAt(0)).append(" Dollars");
                else{
                    tmpNum = tmpNum.replaceAll("\\." , "");
                    finalTerm.append(tmpNum);
                    finalTerm.insert(finalTerm.length()-3,".");
                    finalTerm = allCharactersZero(finalTerm);
                    finalTerm.append("K");
                }
                break;
            }

            else {
                if(isPrice) {
                    finalTerm.append(tmpNum);
                    finalTerm.insert(finalTerm.length() - 6, ".");
                    finalTerm = allCharactersZero(finalTerm);
                    finalTerm.append(" M Dollars");
                    break;
                }
                else{
                    // if between million and billion
                    if(tmpNum.length() >= 7 && tmpNum.length() < 10){
                        finalTerm.append(tmpNum);
                        finalTerm.insert(finalTerm.length()-6,".");
                        finalTerm = allCharactersZero(finalTerm);
                        finalTerm.append("M");
                    }
                    // if between billion and trillion
                    else if(tmpNum.length() >= 10 && tmpNum.length() < 13){
                        finalTerm.append(tmpNum);
                        finalTerm.insert(finalTerm.length()-6,".");
                        finalTerm = allCharactersZero(finalTerm);
                        finalTerm.append("B");
                    }
                }
                break;
            }
        }
        return finalTerm;
    }

    public Map<String, DocumentsHashMap> getTermsMap() {
        return termsMap;
    }


}
