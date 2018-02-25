package ngram;

import com.opencsv.CSVReader;
import file_handler.ReadTrainSetId;
import preprocess.StringProcessor;

import java.io.*;
import java.util.*;

public class NGramWriter {

    private int nGram;
    private static String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Data\\yelpResData\\";
    private String absPath;
    private String fileName;
    private String outFileNameFiltered;
    private String outFileNameNonFiltered;

    private ArrayList<String> filteredReviewText;
    private ArrayList<String> nonFilteredReviewText;

    private HashMap<String, Integer> filteredNGramDictionary;
    private HashMap<String, Integer> nonFilteredNGramDictionary;

    private HashSet<String> testSet;

    public NGramWriter(int nGramSize, String fileName, String outFileNameFiltered, String outFileNameNonFiltered, String testSet1,
                       String testSet2) {
        this.nGram = nGramSize;
        absPath = location + fileName;
        this.outFileNameFiltered = outFileNameFiltered;
        this.outFileNameNonFiltered = outFileNameNonFiltered;
        filteredReviewText = new ArrayList<>();
        nonFilteredReviewText = new ArrayList<>();
        filteredNGramDictionary = new HashMap<>(10000000);
        nonFilteredNGramDictionary = new HashMap<>(10000000);
        testSet = new HashSet<>();
        readTestSet(testSet1, testSet2);

    }

    public void readTestSet(String testSet1, String testSet2) {
        ReadTrainSetId readTrainSetId = new ReadTrainSetId(testSet1);
        testSet.addAll(readTrainSetId.getIds());
        ReadTrainSetId readTrainSetId1 = new ReadTrainSetId(testSet2);
        testSet.addAll(readTrainSetId1.getIds());
    }

    public void splitIntoNGram() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(absPath));
        String[] row = null;
        int counter = 0;
        while ((row = reader.readNext()) != null &&counter <= 50000 ) {
            if (counter == 0) { // skip header
                counter++;
            } else { // process as normal
                if(counter % 50000 == 0){
                    createFilteredNGramDictionary(filteredReviewText);
                    createNonFilteredNGramDictionary(nonFilteredReviewText);
                    filteredReviewText.removeAll(filteredReviewText);
                    nonFilteredReviewText.removeAll(nonFilteredReviewText);

                }
                if (row.length == 10) { // it is a valid row, with review in a single cell and is formatted correctly
                    String reviewId = row[1];
                    if (!testSet.contains(reviewId)) { // if this review is not in either test set, then keep processing

                        if (row[8].contains("N")) { // then this is not a filtered review
                            nonFilteredReviewText.add(row[3]);
                        } else if (row[8].contains("Y")) { // then this is a filtered review
                            filteredReviewText.add(row[3]);
                        }
                        counter ++;
                    }

                }

                System.out.println(counter);
            }

        }
        //writeGramsToFile();

    }

    public void writeGramsToFile(){
        writeToFile(filteredNGramDictionary, outFileNameFiltered);
        writeToFile(nonFilteredNGramDictionary, outFileNameNonFiltered);
    }

    private void createFilteredNGramDictionary(ArrayList<String> textList){

        StringProcessor stringProcessor = new StringProcessor();
        NGramCalculator calculator = new NGramCalculator(nGram);
        int counter = 0;
        for(String review: textList) {
            System.out.println(counter);
            counter ++;
            if (review.length() >= nGram) {
                review = stringProcessor.process(review);
                List<String> grams = calculator.calculateGrams(review);
                for (String gram : grams) {
                    if (!gram.isEmpty() && !gram.equals("")) { // if not left with empty string
                        if (filteredNGramDictionary.containsKey(gram)) {

                            filteredNGramDictionary.put(gram, filteredNGramDictionary.get(gram) + 1);
                        } else {
                            filteredNGramDictionary.put(gram, 1);
                        }
                    }
                }
            }
        }

        System.out.println(filteredNGramDictionary.size() +  " f dictionary size");
    }

    private void createNonFilteredNGramDictionary(ArrayList<String> textList){
        StringProcessor stringProcessor = new StringProcessor();
        int counter = 0;
        for(String review: textList) {
            System.out.println(counter);
            counter ++;
            if (review.length() >= nGram) {
                review = stringProcessor.process(review);
                NGramCalculator calculator = new NGramCalculator(nGram);
                List<String> grams = calculator.calculateGrams(review);
                for (String gram : grams) {

                    if (nonFilteredNGramDictionary.containsKey(gram)) {
                        nonFilteredNGramDictionary.put(gram, nonFilteredNGramDictionary.get(gram) + 1);
                    } else {
                        nonFilteredNGramDictionary.put(gram, 1);
                    }

                }
            }
        }
        System.out.println(nonFilteredNGramDictionary.size() +  " nf dictionary size");
    }

    public void writeToFile(HashMap<String, Integer> nGramDictionary, String outFileName) {
        Iterator it = nGramDictionary.entrySet().iterator();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(outFileName);
            bw = new BufferedWriter(fw);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                bw.write(pair.getKey() + " " + pair.getValue());
                bw.newLine();
                it.remove(); // avoids a ConcurrentModificationException
            }
        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }
}