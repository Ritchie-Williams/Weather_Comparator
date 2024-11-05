package WeatherComparator.GUI;

/*
    Main for the GUI
 */


import WeatherComparator.APIData;
import WeatherComparator.Parser;
import WeatherComparator.WeatherComparison;

import javax.swing.*;
import java.io.IOException;

public class GUIMain extends JPanel implements GUIConstants {
    JPanel WeatherAppPanel;
    JPanel cityPanel;
    private final Parser parser;
    private APIData apiData;

    public GUIMain() {
        // Get API data path and parse
        parser = new Parser();
        String apiDataPath = JOptionPane.showInputDialog("Enter the path to the API Data file:");
        if (apiDataPath == null || apiDataPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No file path provided. Exiting.");
            System.exit(FILE_PATH_INVALID);
        }

        // try to create apiData from given path
        try {
            apiData = parser.parseAPIData(apiDataPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading API data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(API_DATA_INVALID);
        }

        // create panels
        WeatherAppPanel = new WeatherAppPanel(apiData);

        JFrame my_frame = new JFrame();
        WeatherComparison little_rock = new WeatherComparison(apiData);
        little_rock.setWeatherData("Little Rock");
        cityPanel = new CityPanel(little_rock);
        my_frame.add(cityPanel);
        my_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        my_frame.pack();
        my_frame.setLocationRelativeTo(null);
        my_frame.setVisible(true);

        // add all panels to panel in the sky
        add(WeatherAppPanel);
    }

    public static void main() {
        JFrame frame = new JFrame();

        // set up frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Weather Comparator");

        // creates all panels and adds the extended panel
        frame.getContentPane().add(new GUIMain());

        // finish setting up
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
