package WeatherComparator;
/*
    Parses given API text file for data
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private final static int NUMBER_OF_API_FEATURES = 3;
    private final static String COMMENT_SYMBOL = "#";
    private final static String API_DELIMITER = ",";


    // parse API data from a given text file and return as an WeatherComparator.APIData record
    /*
        COMMENTS: A LINE BEGINNING WITH '#'
        PUT API KEY AND API URL IN SAME LINE DELIMITED BY A COMMA
     */
    public APIData parseAPIData(String APIDataPath) throws IOException {
        // put content of file into string
        String contents = Files.readString(Path.of(APIDataPath),
                StandardCharsets.UTF_8);
        // seperate by newlines
        List<String> lines = List.of(contents.split("\n"));

        ArrayList<String> apiData = lines.stream()
                .filter(line-> !line.startsWith(COMMENT_SYMBOL)) // remove all comments
                .flatMap(s->Arrays.stream(s.split(API_DELIMITER))) // split based on delimiter
                .collect(Collectors.toCollection(ArrayList::new)); // to arraylist

        // error check to see if api key/url are there
        if (apiData.size() != NUMBER_OF_API_FEATURES)
            throw new IOException("apiData must have " + NUMBER_OF_API_FEATURES + " features");

        // return newly created WeatherComparator.APIData record
        return (new APIData(apiData.get(0), apiData.get(1), apiData.get(2)));
    }
}