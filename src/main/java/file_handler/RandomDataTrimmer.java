package file_handler;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RandomDataTrimmer {

    private int recordsToKeep;
    private int filteredRecordsCount;
    private int nonFilteredRecordsCount;

    private List<String []> allFilteredRows;
    private List<String []> allNonFilteredRows;

    private List<String []> trimmedFilteredRows;
    private List<String []> trimmedNonFilteredRows;

    private static String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Data\\yelpResData\\";
    private String absPath = "";
    List<Integer> randomIndicies =  new ArrayList<>();

    private HashSet<String> testSetIds = new HashSet<>();

    private static String outfileFiltered = "trimmedTrainSetFiltered.csv";
    private static String outfileNonFiltered = "trimmedTrainSetNotFiltered.csv";
    private static String testSetY = "Train Set Y.txt";
    private static String testSetN = "Train Set N.txt";

    public RandomDataTrimmer(int numRecords, int recordsToKeep, String fileName) {
        this.recordsToKeep = recordsToKeep;
        allFilteredRows = new ArrayList<>();
        allNonFilteredRows = new ArrayList<>();
        trimmedFilteredRows = new ArrayList<>();
        trimmedNonFilteredRows = new ArrayList<>();
        filteredRecordsCount = 0;
        nonFilteredRecordsCount = 0;
        absPath = location + fileName;

        readTestSets();
    }

    private void readTestSets(){
        ReadTrainSetId readTrainSetId = new ReadTrainSetId(testSetY);
        testSetIds.addAll(readTrainSetId.getIds());
        ReadTrainSetId readTrainSetId1 =  new ReadTrainSetId(testSetN);
        testSetIds.addAll(readTrainSetId1.getIds());
    }




    public void readFile() throws IOException {
       // while (nonFilteredRecordsCount != recordsToKeep && filteredRecordsCount != recordsToKeep) {// read file
            CSVReader reader = new CSVReader(new FileReader(absPath));
            String[] row = null;
            int rowCounter = 0;
            int randomIndexCounter = 0;
            //boolean foundValidRow = false;
            while ((row = reader.readNext()) != null && allFilteredRows.size() + allNonFilteredRows.size() <= 350000) { //&&!foundValidRow) {
                rowCounter++;
                // if (rowCounter == randomIndicies.get(randomIndexCounter)) { // pull this row
                if (row.length == 10) { // it is a valid row, with review in a single cell and is formatted correctly
                    if (!testSetIds.contains(row[1])) { // if not a test data point
                        if (row[8].contains("N")) {
                            allNonFilteredRows.add(row);
                            nonFilteredRecordsCount++;
                            //foundValidRow = true;
                        } else if (row[8].contains("Y")) {
                            allFilteredRows.add(row);
                            filteredRecordsCount++;
                            //foundValidRow = true;
                        }
                    }
                    System.out.println(rowCounter);
                }
            }
           // }

            //randomIndexCounter++;
            reader.close();
        }
    //}


    public void selectRandomRows(){
        Collections.shuffle(allNonFilteredRows);
        for(int i = 0; i < recordsToKeep; i++){
            trimmedNonFilteredRows.add(allNonFilteredRows.get(i));
        }

        Collections.shuffle(allFilteredRows);
        for(int i = 0; i < recordsToKeep; i++){
            trimmedFilteredRows.add(allFilteredRows.get(i));
        }
    }



    public void writeTrimmedSetsToFile() throws IOException{
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outfileFiltered));
        csvWriter.writeAll(trimmedFilteredRows);
        csvWriter.close();

        CSVWriter csvWriter1 = new CSVWriter((new FileWriter(outfileNonFiltered)));
        csvWriter1.writeAll(trimmedNonFilteredRows);
        csvWriter1.close();
    }

}
