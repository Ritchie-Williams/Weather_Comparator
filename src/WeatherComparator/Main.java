package WeatherComparator;

import javax.swing.*;
import java.io.IOException;

public class Main {
    // assume apiData is here for user
    private final static String apiDataPath = "./apiData.txt";

    public static void main(String[] args) {
        Parser parser = new Parser();
        APIData apiData;
        try {
            apiData = parser.parseAPIData(apiDataPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading API data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


    }
}
