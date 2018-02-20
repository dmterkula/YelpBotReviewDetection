package file_handler;

import java.io.*;
import java.util.HashMap;

public class ReadUserDictionary {
    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Research\\Project\\src\\main\\resources\\";
    private String absPath;
    private HashMap<String, Integer> userDictionary;

    public ReadUserDictionary(String fileName) {
        userDictionary = new HashMap<>();
        absPath = location + fileName;
        readDictionary();
        System.out.println(userDictionary.size());
    }

    public void readDictionary() {
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fr = new FileReader(absPath);
                br = new BufferedReader(fr);
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    String[] line = currentLine.split(" ");
                    System.out.print(line[0] + " " + line[1]);
                    System.out.println();
                    userDictionary.put(line[0], Integer.parseInt(line[1]));
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
}
