package file_handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReviewReader {
    private String location = "C:\\Users\\David\\Desktop\\Xavier\\2017-2018\\Spring Semester 2018\\Senior Project\\Data\\yelpResData\\";
    private String absPath = "";
    private ArrayList<String []> theFile;
    public ReviewReader(String fileName){
        absPath = location+fileName;
        theFile =  new ArrayList<String[]>();
        readFile();
    }

    public void readFile(){
        try (Stream<String> stream = Files.lines(Paths.get(absPath))) {
            List<String> lines = stream.collect(Collectors.toList());
            for(String line: lines){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
