import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Get API data path and parse
        String apiDataPath = JOptionPane.showInputDialog("Enter the path to the API Data file:");
        if (apiDataPath == null || apiDataPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No file path provided. Exiting.");
            System.exit(0);
        }

        Parser parser = new Parser();
        APIData apiData;
        try {
            apiData = parser.parseAPIData(apiDataPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading API data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create and show the WeatherAppPanel
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Weather Comparison App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new WeatherAppPanel(apiData));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
