package file_handler;

import com.opencsv.CSVReader;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class UserDictionary {
    private static String outFile = "userDictionary.txt";
    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Data\\yelpResData\\";
    private String absPath;
    private HashMap<String, Integer> idAndReviewCount;

    public UserDictionary(String fileName) throws IOException {
        absPath = location + fileName;
        idAndReviewCount = new HashMap<>();
        populateDictionary();
        writeDictionaryToFile();
    }

    public void populateDictionary() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(absPath));
        String[] row = null;
        int counter = 0;
        while ((row = csvReader.readNext()) != null) {
            if (counter == 0) { // skip header
                counter++;
            }
            else {
                String id = row[2];
                if (counter == 1) { // print first id;
                    System.out.println("id test: " + id);
                    counter++;
                }
                if (idAndReviewCount.containsKey(id)) {
                    idAndReviewCount.put(id, idAndReviewCount.get(id) + 1);
                } else {
                    if(!id.contains(" ") && id.length() > 4) {
                        idAndReviewCount.put(id, 1);
                    }
                }
            }



        }
        System.out.println(idAndReviewCount.size());
        csvReader.close();
    }







        /*try {
            FileInputStream fileInputStream = new FileInputStream(absPath);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            for(Row row: worksheet){
                String id = row.getCell(3).getStringCellValue();
                if(!idAndReviewCount.containsKey(id)){
                    idAndReviewCount.put(id, 1);
                }
                else{ // increment value in (k,v) pair
                    idAndReviewCount.put(id, idAndReviewCount.get(id) +1 );
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    //}

    public void writeDictionaryToFile(){
        Iterator it = idAndReviewCount.entrySet().iterator();
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(outFile);
            bw = new BufferedWriter(fw);

            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                String line = pair.getKey() + " " + pair.getValue();
                bw.write(line);
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
