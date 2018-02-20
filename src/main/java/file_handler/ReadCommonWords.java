package file_handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCommonWords {
    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Research\\Project\\src\\main\\resources\\";
    private String absPath;
    private List<String> commonWords;

    public ReadCommonWords(String fileName) {
        commonWords = new ArrayList<>();
        absPath = location + fileName;
        readWords();
    }

    public void readWords() {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(absPath);
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                commonWords.add(currentLine);
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

    public List<String> getCommonWords() {
        return commonWords;
    }
}
