package file_handler;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class WriteTrainSetId {

    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Data\\yelpResData\\";
    private String absPath;
    private HashSet<String> ids;
    private String outFile;

    public WriteTrainSetId(String fileName, String outFile) throws IOException{
        absPath =  location +  fileName;
        ids = new HashSet<>();
        this.outFile = outFile;
       readFromCsv();
       writeToTextFile(outFile);
    }

    public void readFromCsv() throws IOException{
        CSVReader csvReader = new CSVReader(new FileReader(absPath));
        String[] row = null;
        int counter = 0;
        while ((row = csvReader.readNext()) != null) {
                String id = row[1];
                System.out.println(id);
               ids.add(id);
            }
        csvReader.close();
    }

    public void writeToTextFile(String outFile){
        BufferedWriter bw = null;
        FileWriter fw = null;
        Iterator iterator =  ids.iterator();
        try {
            fw = new FileWriter(outFile);
            bw = new BufferedWriter(fw);
            while (iterator.hasNext()) {
                String line = (String)iterator.next();
                bw.write(line);
                bw.newLine();
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
