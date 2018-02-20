package file_handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class ReadTrainSetId {

    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Research\\Project\\src\\main\\resources\\";
    private String absPath;
    private HashSet<String> ids;

    public ReadTrainSetId(String fileName){
        absPath = location + fileName;
        ids = new HashSet<>();
        readFromTextFile();
    }

    public void readFromTextFile(){
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(absPath);
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                ids.add(currentLine);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public HashSet<String> getIds(){
        return ids;
    }

}
