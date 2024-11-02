package WeatherComparator.GUI;

/*
    Main for the GUI
 */


import WeatherComparator.APIData;
import WeatherComparator.Parser;

import javax.swing.*;
import java.io.IOException;

public class GUIMain extends JPanel implements GUIConstants {
    JPanel WeatherAppPanel;
    private final Parser parser;
    private APIData apiData;

    public GUIMain() {
        // Get API data path and parse
        parser = new Parser();
        String apiDataPath = JOptionPane.showInputDialog("Enter the path to the API Data file:");
        if (apiDataPath == null || apiDataPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No file path provided. Exiting.");
            System.exit(3);
        }

        // try to create apiData from given path
        try {
            apiData = parser.parseAPIData(apiDataPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading API data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(3);
        }

        // create panels
        WeatherAppPanel = new WeatherAppPanel(apiData);

        // add all panels to panel in the sky
        add(WeatherAppPanel);
    }

    public static void guiMain() {
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
