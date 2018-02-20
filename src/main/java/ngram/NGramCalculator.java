package ngram;

import preprocess.StringProcessor;

import java.util.ArrayList;
import java.util.List;

public class NGramCalculator {

    private int nGramSize;
    private List<String> grams;

    public NGramCalculator(int nGramSize) {
        this.nGramSize = nGramSize;
        grams = new ArrayList<>();
    }

    public List<String> calculateGrams(String text) {
        String[] words = text.split(" +");
        for (int i = 0; i < words.length - (nGramSize -1); i++) { // <= to capture grams of size nGram
            String gram = "";
            for (int j = 0; j < nGramSize; j++) {
                String nextWord = words[i+j];
                //nextWord=nextWord.trim();
                gram += nextWord;
                if (j != nGramSize - 1) { // dont add space on last word
                    gram += " "; // add space to separate words
                }
            }

            grams.add(gram);
        }
        return grams;
    }

    public int getnGramSize() {
        return nGramSize;
    }

    public void setnGramSize(int nGramSize) {
        this.nGramSize = nGramSize;
    }

    public List<String> getGrams() {
        return grams;
    }

}
