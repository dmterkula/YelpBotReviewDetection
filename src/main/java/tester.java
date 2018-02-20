import com.sun.org.apache.xpath.internal.SourceTree;
import file_handler.ReadCommonWords;
import ngram.NGramCalculator;
import ngram.NGramWriter;
import preprocess.StringProcessor;

import java.io.IOException;
import java.util.List;

public class tester {

    public static void main(String[] args) throws IOException {
        //ReviewReader reviewReader = new ReviewReader("3000-N-split1.csv");
        //reviewReader.readFile();


        //UserDictionary userDictionary = new UserDictionary("filteredReviews.csv");
        //ReadUserDictionary rud =  new ReadUserDictionary("userDictionary.txt");

        //WriteTrainSetId wtsY = new WriteTrainSetId("Train Set Y.csv", "Train Set Y.txt");
        // WriteTrainSetId wtsN = new WriteTrainSetId("Train Set N.csv", "Train Set N.txt");

        //ReadTrainSetId rts = new ReadTrainSetId("Train Set N.txt");
        //HashSet<String> ids = rts.getIds();
        //System.out.println(ids.size());


        NGramWriter nGramWriter = new NGramWriter(4, "filteredReviews.csv", "filteredGrams.txt",
                "nonFilteredGrams.txt", "Train Set Y.txt", "Train Set N.txt");
        nGramWriter.splitIntoNGram();

        //1136009 with process
        //1181485 without process



    }
}
