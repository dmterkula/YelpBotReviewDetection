package ngram;

import com.opencsv.CSVReader;
import file_handler.ReadTrainSetId;
import preprocess.StringProcessor;

import java.io.*;
import java.util.*;

public class NGramWriter {

    private int nGram;
    private static String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Research\\Project\\src\\main\\resources\\";
    private String absPath;

    private String outFileNameFiltered;
    private String outFileNameNonFiltered;

    private ArrayList<String> filteredReviewText;
    private ArrayList<String> nonFilteredReviewText;

    private HashMap<String, Integer> filteredNGramDictionary;
    private HashMap<String, Integer> nonFilteredNGramDictionary;

    private HashSet<String> testSet;

    private int[] filteredFrequencyDistribution;
    private int[] nonFilteredFrequencyDistribution;

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

        filteredFrequencyDistribution = new int[501];
        nonFilteredFrequencyDistribution = new int[501];

        for(int i = 0; i < filteredFrequencyDistribution.length; i++){
            filteredFrequencyDistribution[i] = 0;
            nonFilteredFrequencyDistribution[i] = 0;
        }

    }

    public void readTestSet(String testSet1, String testSet2) {
        ReadTrainSetId readTrainSetId = new ReadTrainSetId(testSet1);
        testSet.addAll(readTrainSetId.getIds());
        ReadTrainSetId readTrainSetId1 = new ReadTrainSetId(testSet2);
        testSet.addAll(readTrainSetId1.getIds());
    }

    public void splitIntoNGram() throws Exception {
        CSVReader reader = new CSVReader(new FileReader(absPath));
        String[] row = null;
        int counter = 0;
        while ((row = reader.readNext()) != null) {
            System.out.println(counter);
            if (row.length == 10) { // it is a valid row, with review in a single cell and is formatted correctly
                if (row[8].contains("N")) { // then this is not a filtered review
                    nonFilteredReviewText.add(row[3]);
                } else  { // then this is a filtered review
                    filteredReviewText.add(row[3]);
                }
                counter++;
            }
        }
        reader.close();


        createFilteredNGramDictionary(filteredReviewText);
        //createNonFilteredNGramDictionary(nonFilteredReviewText);
        writeGramsToFile();

    }

    public void writeGramsToFile(){
        writeToFile(filteredNGramDictionary, outFileNameFiltered);
        //writeToFile(nonFilteredNGramDictionary, outFileNameNonFiltered);
    }

    private void createFilteredNGramDictionary(ArrayList<String> textList) throws Exception{

        StringProcessor stringProcessor = new StringProcessor();
        int counter = 0;
        for(String review: textList) {
            NGramCalculator calculator = new NGramCalculator(nGram);
            System.out.println(counter);
           // System.out.println("Size: " + filteredNGramDictionary.size());
            //Thread.sleep(500);
            counter ++;
            if (review.length() >= nGram) { // can make atleast 1 valid gram of nGram length
                review = stringProcessor.process(review);
                List<String> grams = calculator.calculateGrams(review);
                for (String gram : grams) {
                    if (!gram.isEmpty() && !gram.equals("")) { // if not left with empty string
                       // System.out.println(gram);
                        if (filteredNGramDictionary.containsKey(gram)) {
                            filteredNGramDictionary.put(gram, filteredNGramDictionary.get(gram) + 1);
                        } else {
                            filteredNGramDictionary.put(gram, 1);
                        }

                    }
                }
            }
            calculator = null;
        }

        System.out.println(filteredNGramDictionary.size() +  " f dictionary size");
    }

    private void createNonFilteredNGramDictionary(ArrayList<String> textList){
        StringProcessor stringProcessor = new StringProcessor();
        int counter = 0;
        for(String review: textList) {
            NGramCalculator calculator = new NGramCalculator(nGram);
            System.out.println(counter);
            counter ++;
            if (review.length() >= nGram) {
                review = stringProcessor.process(review);

                List<String> grams = calculator.calculateGrams(review);
                for (String gram : grams) {

                    if (nonFilteredNGramDictionary.containsKey(gram)) {
                        nonFilteredNGramDictionary.put(gram, nonFilteredNGramDictionary.get(gram) + 1);
                    } else {
                        nonFilteredNGramDictionary.put(gram, 1);
                    }

                }
                calculator = null;
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

    public void assessModel(){

        // non filtered
        Iterator it = nonFilteredNGramDictionary.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            int value = (int)pair.getValue();
            if(value < nonFilteredFrequencyDistribution.length-1){
                nonFilteredFrequencyDistribution[value] = nonFilteredFrequencyDistribution[value] + 1;
            }
            else{
                nonFilteredFrequencyDistribution[nonFilteredFrequencyDistribution.length-1] = nonFilteredFrequencyDistribution[nonFilteredFrequencyDistribution.length-1] + 1;
            }
        }


        Iterator itr = filteredNGramDictionary.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            int value = (int)pair.getValue();
            if(value < filteredFrequencyDistribution.length-1){
                filteredFrequencyDistribution[value] = filteredFrequencyDistribution[value] + 1;
            }
            else{
                filteredFrequencyDistribution[filteredFrequencyDistribution.length-1] = filteredFrequencyDistribution[filteredFrequencyDistribution.length-1] + 1;
            }
        }

    }

    public void printTables(){

        System.out.println("Non Filtered n gram distribution, n = " + nGram);
        System.out.println("------------------------------------------------");
        for(int i = 0; i < nonFilteredFrequencyDistribution.length; i++){
            System.out.println(i + ":    " +  nonFilteredFrequencyDistribution[i]);
        }
        System.out.println();
        System.out.println();

        System.out.println("Filtered n gram distribution, n = " + nGram);
        System.out.println("------------------------------------------------");
        for(int i = 0; i < filteredFrequencyDistribution.length; i++){
            System.out.println(i + ":    " +  filteredFrequencyDistribution[i]);
        }


    }


}