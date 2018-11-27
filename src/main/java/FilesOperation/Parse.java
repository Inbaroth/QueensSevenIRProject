package FilesOperation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.text.BreakIterator;
import java.util.*;

public class Parse {

    // String - term
    Map <String, HashMap<String,Integer>> termsMap;
    HashSet<String> stopWords;
    HashMap<String,String> monthDictionary;
    HashMap<String,String> numbersDictionary;
    HashMap<String,String> pricesDictionary;
    HashSet<String> precentSet;
    HashSet<String> dollarSet;

    /**
     * Constructor
     */
    public Parse() {
        this.termsMap = new HashMap<>();
        this.stopWords = new HashSet<>();
        // create and fill month dictionary
        this.monthDictionary = new HashMap<>();
        this.numbersDictionary = new HashMap<>();
        this.pricesDictionary = new HashMap<>();
        this.precentSet = new HashSet<>();
        this.dollarSet = new HashSet<>();
        fillMonthDictionary();

        // load stop words into dictionary
        File file = new File("src/main/resources/stop words.txt");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line = bf.readLine();
            while (line != null){
                stopWords.add(line);
                line = bf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void fillMonthDictionary() {
        monthDictionary.put("JANUARY", "01");
        monthDictionary.put("January", "01");
        monthDictionary.put("Jan", "01");
        monthDictionary.put("FEBRUARY", "02");
        monthDictionary.put("February", "02");
        monthDictionary.put("Feb", "02");
        monthDictionary.put("MARCH", "03");
        monthDictionary.put("March", "03");
        monthDictionary.put("Mar", "03");
        monthDictionary.put("APRIL", "04");
        monthDictionary.put("April", "04");
        monthDictionary.put("Apr", "04");
        monthDictionary.put("MAY", "05");
        monthDictionary.put("May", "05");
        monthDictionary.put("JUNE", "06");
        monthDictionary.put("June", "06");
        monthDictionary.put("JULY", "07");
        monthDictionary.put("July", "07");
        monthDictionary.put("AUGUST", "08");
        monthDictionary.put("August", "08");
        monthDictionary.put("Aug", "08");
        monthDictionary.put("SEPTEMBER", "09");
        monthDictionary.put("September", "09");
        monthDictionary.put("Sept", "09");
        monthDictionary.put("OCTOBER", "10");
        monthDictionary.put("October", "10");
        monthDictionary.put("Oct", "10");
        monthDictionary.put("NOVEMBER", "11");
        monthDictionary.put("November", "11");
        monthDictionary.put("Nov", "11");
        monthDictionary.put("DECEMBER", "12");
        monthDictionary.put("December", "12");
        monthDictionary.put("Dec", "12");
        // fill numbers dictionary
        numbersDictionary.put("Thousand", "K");
        numbersDictionary.put("Thousand,", "K");
        numbersDictionary.put("Million", "M");
        numbersDictionary.put("Million,", "M");
        numbersDictionary.put("Billion", "B");
        numbersDictionary.put("Billion,", "B");
        numbersDictionary.put("Trillion", "00B");
        numbersDictionary.put("Trillion,", "00B");
        // fill prices dictionary
        pricesDictionary.put("million", " M Dollars");
        pricesDictionary.put("m", " M Dollars");
        pricesDictionary.put("Billion", "000 M Dollars");
        pricesDictionary.put("billion", "000 M Dollars");
        pricesDictionary.put("bn", "000 M Dollars");
        pricesDictionary.put("trillion", "000000 M Dollars");
        // fill percent set
        precentSet.add("percent");
        precentSet.add("Percent");
        precentSet.add("Percentage");
        precentSet.add("percents");
        precentSet.add("percentages");
        precentSet.add("Percents");
        precentSet.add("Percentages");
        //Dollars|Dollar|dollar|dollars
        dollarSet.add("Dollars");
        dollarSet.add("Dollar");
        dollarSet.add("dollar");
        dollarSet.add("dollars");
        dollarSet.add("Dollars,");
        dollarSet.add("Dollar,");
        dollarSet.add("dollar,");
        dollarSet.add("dollars,");



    }


    public void parsing(String docId, String docText, String fileName){
        //values , key + value of DocumentsHashMap
        String docNameFileName = docId + "_" + fileName;
        //take only text between <Text> and </Text>
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        //String cityName = StringUtils.substringBetween("p=\"104\"\">","</f>");
        String cityName = StringUtils.substringBetween(docText, "<f p=\"104\">\n ","</f> \n");
        if (cityName != null){
            cityName = cityName.trim();
            updateTerm(cityName.toUpperCase(),docNameFileName);
        }

        Document document = Jsoup.parse(docText);
        String text = document.getElementsByTag("TEXT").text();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String s = text.substring(start,end);
            parsingLine(docId,s,docNameFileName);
        }

    }

    private void parsingLine(String docId, String docLine, String docNameFileName){
        String punctuations = ",.";
        //split all words by whitespace into array of words
        //docLine = docLine.replaceAll("[():?!]","");
        //Check if function splitByDelimiter is better
        //String [] wordsInDoc = docLine.trim().split("\\s+");
        String[] wordsInDoc = splitByDelimiter(docLine, ' ');
        //if more than one line, contains "" at wordInDoc[wordInDoc.length -1]
        //find where is the dot closing the sentence and remove it
        if (wordsInDoc.length == 1) {
            if (wordsInDoc[0].equals("") || wordsInDoc[0].equals("."))
                return;
            int indexOfDot = wordsInDoc[0].indexOf('.');
            if (indexOfDot != -1)
                wordsInDoc[0] = wordsInDoc[0].substring(0, indexOfDot);
        } else {
            if (wordsInDoc[wordsInDoc.length - 1].length() < 1) {
                wordsInDoc[wordsInDoc.length - 2] = wordsInDoc[wordsInDoc.length - 2].substring(0, wordsInDoc[wordsInDoc.length - 2].length() - 1);
            } else {
                wordsInDoc[wordsInDoc.length - 1] = wordsInDoc[wordsInDoc.length - 1].substring(0, wordsInDoc[wordsInDoc.length - 1].length() - 1);
            }
        }
        for (int i = 0; i < wordsInDoc.length; i++) {

            if (wordsInDoc[i].length() == 0 || punctuations.contains(wordsInDoc[i]))
                continue;

            if (stopWords.contains(wordsInDoc[i]) && !wordsInDoc[i].equals("between"))
                continue;

            // if wordsInDoc[i] start with "
            if (wordsInDoc[i].indexOf("\"") == 0) {
                wordsInDoc[i] = wordsInDoc[i].substring(1);
                if (wordsInDoc[i].length() == 0)
                    continue;
            }

            //check if the word in the array ends with ','
            boolean isPair = true;
            if (wordsInDoc[i].endsWith(",")) {
                isPair = false;
                wordsInDoc[i] = wordsInDoc[i].substring(0, wordsInDoc[i].length() - 1);
                if (wordsInDoc[i].endsWith(","))
                    continue;
            }
            //if it is NOT a tag , doesn't starts with '<'
            //if(!((wordsInDoc[i].charAt(0)== '<') || wordsInDoc[i].charAt(wordsInDoc[i].length()-1) == '>')){
            if (!(wordsInDoc[i].contains("<"))) {

                Vector<StringBuilder> numberTerm = new Vector<>();

                //if a term kind: B(/b)etween number and number like: Between 10 and 12
                if (i + 1 < wordsInDoc.length && wordsInDoc[i].equalsIgnoreCase("between") && isInteger(wordsInDoc[i + 1],false)) {
                    int advanceI = i;
                    boolean fraction = false;
                    //Between 1 and 3 || Between 1/3 and 3/4 || Between 1 and 3.5 ,saved at dic as 1-3.5
                    if (i + 3 < wordsInDoc.length && wordsInDoc[i + 2].equalsIgnoreCase("and") && isInteger(wordsInDoc[i + 3],false)) {
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 3]));
                        advanceI = i + 3;
                        //Between 1 and 3 1/2
                        if (i + 4 < wordsInDoc.length && isInteger(wordsInDoc[i + 4],false)) {
                            numberTerm.add(new StringBuilder(wordsInDoc[i + 4]));
                            fraction = true;
                            advanceI = advanceI + 1;
                        }
                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        term1.append("-" + term2);
                        updateTerm(term1.toString(), docNameFileName);
                        i = advanceI;
                        continue;
                    }
                    //Between 1 1/2 and number || Between 1 Million and number
                    if (i + 4 < wordsInDoc.length && (isInteger(wordsInDoc[i + 2],false) || numbersDictionary.containsKey(wordsInDoc[i + 2])) && wordsInDoc[i + 3].equalsIgnoreCase("and") && isInteger(wordsInDoc[i + 4],false)) {
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 2]));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, isInteger(wordsInDoc[i + 2],false));
                        numberTerm.clear();
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 4]));
                        advanceI = i + 4;
                        //Between 1 1/2 and 1 3/4
                        if (i + 5 < wordsInDoc.length && isInteger(wordsInDoc[i + 5],false)) {
                            numberTerm.add(new StringBuilder(wordsInDoc[i + 5]));
                            fraction = true;
                            advanceI = advanceI + 1;
                        }
                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, fraction);
                        numberTerm.clear();
                        term1.append("-" + term2);
                        updateTerm(term1.toString(), docNameFileName);
                        i = advanceI;
                        continue;
                    }
                }

                //first Month Name then Number
                if (monthDictionary.containsKey(wordsInDoc[i])) {
                    //insert to dic also Month name by itself
                    updateTerm(wordsInDoc[i], docNameFileName);
                    if (i + 1 < wordsInDoc.length && isPair) {
                        wordsInDoc[i + 1] = removeLastComma(wordsInDoc[i + 1]);
                        if (i + 1 < wordsInDoc.length && isInteger(wordsInDoc[i + 1],true)) {
                            //change to date format: MM-DD and insert to dic
                            StringBuilder date = convertToDateFormat(wordsInDoc[i], wordsInDoc[i + 1]);
                            if (date != null)
                                updateTerm(date.toString(), docNameFileName);
                            i = i + 1;
                            continue;
                        }
                    }
                }
                //first Number then Month Name
                if (i + 1 < wordsInDoc.length && isInteger(wordsInDoc[i],true)) {
                    wordsInDoc[i + 1] = wordsInDoc[i + 1].replaceAll(",", "");
                    if (monthDictionary.containsKey(wordsInDoc[i + 1])) {
                        //insert to dic month name alone
                        updateTerm(wordsInDoc[i + 1], docNameFileName);
                        //change date like 14 May to 05-14
                        StringBuilder date = convertToDateFormat(wordsInDoc[i + 1], wordsInDoc[i]);
                        if (date != null)
                            updateTerm(date.toString(), docNameFileName);
                        i = i + 1;
                        continue;
                    }
                }

                //check if it is an Integer like: 0,0 | 0% | $0 | 0.0 | 0/0
                boolean twoNumsCells = false;
                if (wordsInDoc[i].startsWith("$") || wordsInDoc[i].endsWith("%") || isInteger(wordsInDoc[i],false)) {
                    //if the cell i+1 also part of the number
                    if (isPair && i + 1 < wordsInDoc.length) {
                        wordsInDoc[i + 1] = removeLastComma(wordsInDoc[i + 1]);
                        //handle term like: 2 1/2-2 3/4 | 2 Million-4 Million
                        int makafIndex = wordsInDoc[i + 1].indexOf('-');
                        if (makafIndex != -1) {
                            int advanceI = i + 1;
                            String beforeMakaf = wordsInDoc[i + 1].substring(0, makafIndex);
                            String afterMakaf = wordsInDoc[i + 1].substring(makafIndex + 1);
                            //million-22
                            //1/2-22 || 1/2-3/4
                            String numberMakaf = "[0-9]*[0-9/]+[0-9]";
                            if (numbersDictionary.containsKey(beforeMakaf) || beforeMakaf.matches(numberMakaf)) {
                                numberTerm.add(new StringBuilder(wordsInDoc[i]));
                                numberTerm.add(new StringBuilder(beforeMakaf));
                                StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, beforeMakaf.contains("/"));
                                numberTerm.clear();
                                if (isInteger(afterMakaf,false)) {
                                    numberTerm.add(new StringBuilder(afterMakaf));
                                    if (i + 2 < wordsInDoc.length && (numbersDictionary.containsKey(wordsInDoc[i + 2]) || wordsInDoc[i + 2].matches(numberMakaf))) {
                                        numberTerm.add(new StringBuilder(wordsInDoc[i + 2]));
                                        StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, wordsInDoc[i + 2].contains("/"));
                                        numberTerm.clear();
                                        term1.append("-" + term2.toString());
                                        updateTerm(term1.toString(), docNameFileName);
                                        advanceI = advanceI + 1;
                                    } else {
                                        StringBuilder afterMN = parseNumbers(false, docNameFileName, numberTerm, false);
                                        term1.append("-" + afterMN);
                                        updateTerm(term1.toString(), docNameFileName);
                                    }

                                } else {
                                    term1.append("-" + afterMakaf);
                                    updateTerm(term1.toString(), docNameFileName);
                                }
                                i = advanceI;
                                continue;

                            }
                        }
                        if (isInteger(wordsInDoc[i + 1],false)) {
                            twoNumsCells = true;
                        }
                    }

                    StringBuilder numTerm = new StringBuilder(wordsInDoc[i]);

                    boolean isDollar = false;

                    if (wordsInDoc[i].charAt(0) == '$') {
                        isDollar = true;
                        twoNumsCells = false;
                        numTerm.delete(0, 1);
                        numberTerm.add(numTerm);
                    }
                    //$price million/billion/trillion
                    if (i + 1 < wordsInDoc.length && isDollar && pricesDictionary.containsKey(wordsInDoc[i + 1])) {
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        updateTerm(wordsInDoc[i + 1].toLowerCase(), docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true, docNameFileName, numberTerm, twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(), docNameFileName);
                        i = i + 1;
                        continue;
                    }

                    //$price
                    if (isDollar) {
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true, docNameFileName, numberTerm, twoNumsCells);
                        updateTerm(finalTerm.toString(), docNameFileName);
                        numberTerm.clear();
                        continue;
                    }

                    //price Dollars/Dollar/dollar/dollars
                    if (i + 1 < wordsInDoc.length && !isDollar && dollarSet.contains(wordsInDoc[i + 1])) {
                        numberTerm.add(numTerm);
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        //update dic with Dollars/Dollar/dollar/dollars
                        updateTerm(wordsInDoc[i + 1].toLowerCase(), docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true, docNameFileName, numberTerm, twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(), docNameFileName);
                        i = i + 1;
                        continue;
                    }

                    //price m/bn/fraction Dollars/Dollar/dollar/dollars
                    if (i + 2 < wordsInDoc.length && !isDollar && dollarSet.contains(wordsInDoc[i + 2])) {
                        if (pricesDictionary.containsKey(wordsInDoc[i + 1]) || twoNumsCells) {
                            numberTerm.add(numTerm);
                            numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                            //update dic with Dollars/Dollar/dollar/dollars
                            updateTerm(wordsInDoc[i + 1].toLowerCase(), docNameFileName);
                            //call to func which deal with prices
                            StringBuilder finalTerm = parseNumbers(true, docNameFileName, numberTerm, twoNumsCells);
                            numberTerm.clear();
                            updateTerm(finalTerm.toString(), docNameFileName);
                            i = i + 2;
                            continue;
                        }
                    }

                    //price million/billion/trillion U.S. Dollars/Dollar/dollar/dollars
                    else if (i + 3 < wordsInDoc.length && !isDollar && pricesDictionary.containsKey(wordsInDoc[i + 1]) && wordsInDoc[i + 2].equals("U.S.") && pricesDictionary.containsKey(wordsInDoc[i + 3])) {
                        numberTerm.add(numTerm);
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                        //update dic with Dollars/Dollar/dollar/dollars
                        updateTerm(wordsInDoc[i + 1].toLowerCase(), docNameFileName);
                        //call to func which deal with prices
                        StringBuilder finalTerm = parseNumbers(true, docNameFileName, numberTerm, twoNumsCells);
                        numberTerm.clear();
                        updateTerm(finalTerm.toString(), docNameFileName);
                        i = i + 3;
                        continue;

                    }

                    int cellNum = i;
                    //if like 22 3/4 marge to one cell
                    if (twoNumsCells) {
                        cellNum = i + 1;
                        numTerm.append(" " + wordsInDoc[i + 1]);
                    }

                    //if ends with '%'
                    if (wordsInDoc[cellNum].charAt(wordsInDoc[cellNum].length() - 1) == '%') {
                        String numTermStr = numTerm.toString();
                        updateTerm(numTermStr, docNameFileName);
                        if (twoNumsCells)
                            i = i + 1;
                        continue;
                    }

                    //if appears percent / percentage / Percent / Percentage /  percents / percentages / Percents / Percentages at the cell after
                    if (cellNum + 1 < wordsInDoc.length && precentSet.contains(wordsInDoc[cellNum + 1])) {
                        if (twoNumsCells) {
                            i = i + 1;
                        }
                        numTerm.append("%");
                        updateTerm(numTerm.toString(), docNameFileName);
                        updateTerm(wordsInDoc[cellNum + 1], docNameFileName);

                        i = i + 1;
                        continue;
                    }

                    numberTerm.add(new StringBuilder(wordsInDoc[i]));
                    if (twoNumsCells || (i + 1 < wordsInDoc.length && numbersDictionary.containsKey(wordsInDoc[i + 1]))) {
                        numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                    }
                    StringBuilder finalTerm = parseNumbers(false, docNameFileName, numberTerm, twoNumsCells);
                    numberTerm.clear();
                    updateTerm(finalTerm.toString(), docNameFileName);
                    if (twoNumsCells)
                        i = i + 1;
                    continue;
                }

                //handle Word-Word || Word-Word-Word
                int indxMakaf = wordsInDoc[i].indexOf('-');
                if (indxMakaf > 0 && !StringUtils.isNumeric(wordsInDoc[i])) {
                    if (!StringUtils.isNumeric(wordsInDoc[i].substring(indxMakaf + 1))) {
                        updateTerm(wordsInDoc[i], docNameFileName);
                    }
                }

                //handle number-number like 2,200-2,400 || 120-120.5 or number-word like: 2-kids, 2.5-people
                int makafIndex = wordsInDoc[i].indexOf('-');
                if (makafIndex != -1) {
                    String beforeMakaf = wordsInDoc[i].substring(0, makafIndex);
                    String afterMakaf = wordsInDoc[i].substring(makafIndex + 1);
                    //handle number-number like: 1/2-3/4 || 2-2.5
                    if (isInteger(beforeMakaf,false)) {
                        numberTerm.add(new StringBuilder(beforeMakaf));
                        StringBuilder term1 = parseNumbers(false, docNameFileName, numberTerm, false);
                        numberTerm.clear();
                        if (isInteger(afterMakaf,false)) {
                            numberTerm.add(new StringBuilder(afterMakaf));
                            //1-1 1/2 || 1-2 Million
                            if (i + 1 < wordsInDoc.length && (isInteger(wordsInDoc[i + 1],false) || numbersDictionary.containsKey(wordsInDoc[i + 1]))) {
                                numberTerm.add(new StringBuilder(wordsInDoc[i + 1]));
                                twoNumsCells = true;
                                i = i + 1;
                            }
                            StringBuilder term2 = parseNumbers(false, docNameFileName, numberTerm, twoNumsCells);
                            term1.append("-" + term2);
                            numberTerm.clear();
                            updateTerm(term1.toString(), docNameFileName);
                            continue;
                        }
                        //number-word like 2-girls
                        else {
                            term1.append("-" + afterMakaf);
                            updateTerm(term1.toString(), docNameFileName);
                            continue;
                        }
                    }


                }

                wordsInDoc[i] = wordsInDoc[i].replaceAll("[^a-zA-Z]", "");
                if (wordsInDoc[i].length() == 0)
                    continue;
                // the word doesn't exist in the dictionary starting with LOWER and UPPER
                if (!termsMap.containsKey(wordsInDoc[i].toUpperCase()) && !termsMap.containsKey(wordsInDoc[i].toLowerCase())) {
                    if (Character.isUpperCase(wordsInDoc[i].charAt(0)))
                        updateTerm(wordsInDoc[i].toUpperCase(), docNameFileName);
                    else
                        updateTerm(wordsInDoc[i], docNameFileName);
                    continue;
                }
                //WORD starts with UPPER and exist in dic with LOWER, update dic with LOWER CASE of WORD
                if (Character.isUpperCase(wordsInDoc[i].charAt(0)) && termsMap.containsKey(wordsInDoc[i].toLowerCase())) {
                    updateTerm(wordsInDoc[i].toLowerCase(), docNameFileName);
                    continue;
                }

                //WORD starts with LOWER and exist in dic with UPPER, update dic with LOWER CASE of WORD and update key to LOWER
                if (!Character.isUpperCase(wordsInDoc[i].charAt(0)) && termsMap.containsKey(wordsInDoc[i].toUpperCase())) {
                    termsMap.put(wordsInDoc[i], termsMap.get(wordsInDoc[i].toUpperCase()));
                    int currentNumOfAppearance = 0;
                    if (termsMap.get(wordsInDoc[i]).containsKey(docNameFileName))
                        currentNumOfAppearance = (termsMap.get(wordsInDoc[i])).get(docNameFileName);
                    (termsMap.get(wordsInDoc[i])).put(docNameFileName, currentNumOfAppearance + 1);
                    termsMap.remove(wordsInDoc[i].toUpperCase());
                    continue;
                }
                //covers case of starts only with UPPER
                updateTerm(wordsInDoc[i].toUpperCase(), docNameFileName);
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
        String month = monthDictionary.get(monthName);
        //        // year
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
            HashMap<String,Integer> documentsHashMap = new HashMap<>();
            documentsHashMap.put(docNameFileName, 1);
            termsMap.put(key,documentsHashMap);
            //key exist , update value.
        } else {
            int currentNumOfAppearance = 0;
            if (termsMap.get(key).containsKey(docNameFileName))
                currentNumOfAppearance = (termsMap.get(key)).get(docNameFileName);
            (termsMap.get(key)).put(docNameFileName,currentNumOfAppearance+1);
        }
    }


    /**
     *
     * @param str
     * @return
     */
    private StringBuilder allCharactersZero(StringBuilder str) {
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
        if(word.contains(",") && word.charAt(word.length()-1)== ',') {
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
                if (isPrice) {
                    finalTerm.append(numberTerm.elementAt(0)).append(pricesDictionary.get(numberTerm.elementAt(1).toString()));
                }
                else{
                    finalTerm.append(numberTerm.elementAt(0)).append(numbersDictionary.get(numberTerm.elementAt(1).toString()));
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

            //handle decimal fraction
            int dotIndex = tmpNum.indexOf('.');
            //1.5 11.5 111.5
            if(dotIndex!=-1 && dotIndex<4)
                return new StringBuilder(tmpNum);

            //if smaller than million
            if (!isPrice && tmpNum.length() < 5){
                finalTerm.append(numberTerm.elementAt(0));
                break;
            }
            if (tmpNum.length() < 7) {
                if(isPrice)
                    finalTerm.append(numberTerm.elementAt(0)).append(" Dollars");
                else{
                    dotIndex = tmpNum.indexOf(".");
                    if (tmpNum.contains(".") && dotIndex < 5){
                        finalTerm.append(tmpNum);
                    }
                    else{
                        tmpNum = tmpNum.replaceAll("\\." , "");
                        finalTerm.append(tmpNum);
                        if (dotIndex == -1)
                            finalTerm.insert(finalTerm.length()-3,".");
                        else
                            finalTerm.insert(dotIndex-3,".");
                        finalTerm = allCharactersZero(finalTerm);
                        finalTerm.append("K");
                    }

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


    public String[] splitByDelimiter( String line, char delimiter) {

        if(line.equals(" ")) {
            String[] result = {""};
            return result;
        }
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter, 0); // first substring

        while (j >= 0) {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring




        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }



    public Map<String, HashMap<String,Integer>> getTermsMap() {
        return termsMap;
    }

    /**
     *
     * @param str
     * @param isDate
     * @return
     */
    public boolean isInteger(String str, boolean isDate) {
        boolean ans = false;
        int counter = 0;
        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return false;
        }
        int i = 0;
        char c = str.charAt(i);
        char lastChar = str.charAt(str.length()-1);
        if (c < '0' || c > '9' || lastChar < '0' || lastChar >'9')
            return false;
        else
            ans = true;
        i = 1;
        if (isDate) {
            for (; i < str.length() - 1; i++) {
                c = str.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            ans = true;
        }
        else{
            int indexOfBackslash = str.indexOf('/');
            if (indexOfBackslash != -1) {
                indexOfBackslash = str.indexOf('/', indexOfBackslash + 1);
                if (indexOfBackslash != -1)
                    return false;
            }
            int indexOfDot = str.indexOf('.');
            if (indexOfDot != -1){
                indexOfDot = str.indexOf('.', indexOfDot + 1);
                if (indexOfDot != -1)
                    return false;
            }

            for (; i < str.length() - 1; i++) {
                c = str.charAt(i);
                if (c == '/' || c == '.' || c == ',' || c >= '0' || c <= '9') {
                    ans = true;
                }
                else if(c < '0' || c > '9'){
                    return false;
                }
            }
        }
        return ans;
    }


}


