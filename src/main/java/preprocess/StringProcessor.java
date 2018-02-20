package preprocess;

import file_handler.ReadCommonWords;

import java.util.ArrayList;
import java.util.List;

public class StringProcessor {

    private static String commonWordsFile = "CommonWords.txt";
    public StringProcessor(){

    }

    private String removePunctuation(String text){
        String s = text.replaceAll("\\p{Punct}","");
        return s;
    }

    private String convertToLowercase(String text){
        String s = text.toLowerCase();
        return s;
    }


    public String removeExtraSpaces(String text){
        //String s = text.replaceAll(" +", " ");
        String s = text.trim().replaceAll(" +", " ");
        return s;
    }

    public String removeHiddenCharacters(String text){
        String truncated = text.replaceAll("\\p{Cntrl}", "");
        StringBuilder stringBuilder = new StringBuilder(text);
        List<Integer> deleteIndicies =  new ArrayList<>();
        char [] charArray = text.toCharArray();
        for(int i = 0; i< charArray.length; i++) {
            int ascii = (int) charArray[i];
            if (ascii < 32 || ascii > 126) { // add i to delete index
                deleteIndicies.add(i);
            }
        }
        int deleteCounter = 0;
        for(Integer index: deleteIndicies){
            stringBuilder.deleteCharAt(index - deleteCounter);
            deleteCounter ++;
        }

       truncated = stringBuilder.toString();
        return truncated;
    }


    public String removeCommonWords(String text){
        List<String> commonWords = new ReadCommonWords(commonWordsFile).getCommonWords();
        for(String s: commonWords){
            String word = " " + s + " ";
            if(text.contains(word)){
                text = text.replaceAll(word, " ");
            }
        }
        String[] words = text.split(" +");
        if(commonWords.contains(words[0])){ // if first word is a common word
            //System.out.println(text);
            //System.out.println(text.length());
            if(words.length == 1){ // if you have removed all words in string bc all common, then we want to remove this entry, give empty string
                text = "";
            }
            else{
                text =  text.substring(words[0].length()+1); // remove first word and following space
            }


        }
        else if(commonWords.contains(words[words.length-1])){ // if last word is common word
            text = text.substring(0, text.length() - words[words.length-1].length()); // remove last word and preceding space
        }
        return text;
    }

    public String process(String text){
        text = convertToLowercase(text);
        text = removePunctuation(text);
        text = removeExtraSpaces(text);
        text = removeHiddenCharacters(text);
        text = removeCommonWords(text);
        return text;
    }





}
