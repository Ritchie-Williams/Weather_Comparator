package WeatherComparator.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import WeatherComparator.APIData;
import WeatherComparator.GlobalConstants;
import WeatherComparator.WeatherComparison;

public class WeatherAppPanel extends JPanel implements GlobalConstants {
    private final ArrayList<WeatherComparison> weatherComparison;
    private final ArrayList<JTextField> cityFields;
    private final JTextArea resultArea;

    public WeatherAppPanel(APIData apiData) {
        // weather comparison
        weatherComparison = new ArrayList<>();
        while (weatherComparison.size() != NUMBER_OF_CITIES) {
            weatherComparison.add(new WeatherComparison(apiData));
        }
        // Setting layout
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Cities to Compare"));

        // City fields
        cityFields = new ArrayList<>();
        while (cityFields.size() != NUMBER_OF_CITIES) {
            cityFields.add(new JTextField());
        }

        // Labels for cities
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            inputPanel.add(new JLabel("City #" + (i + 1) + ":"));
            inputPanel.add(cityFields.get(i));
        }

        // Compare Button
        JButton compareButton = new JButton("Compare");
        compareButton.addActionListener(new CompareButtonListener());
        inputPanel.add(compareButton);

        // Reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFields());
        inputPanel.add(resetButton);

        // Result Area
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Weather Comparison Results"));

        // Add components to the panel
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Clears input fields and results
    private void resetFields() {
        for (JTextField cityField : cityFields) {
            cityField.setText("");
        }
        resultArea.setText("");
    }

    // Method to display weather data in the JTextArea instead of the terminal
    private void displayWeatherInTextArea(WeatherComparison weatherComparison) {
        resultArea.append("\nWeather Data for " + weatherComparison.getCityName() + ":\n");
        resultArea.append("Temperature: " + weatherComparison.getTemperature() + " °C\n");
        resultArea.append("Wind Speed: " + weatherComparison.getWindSpeed() + " m/s\n");
        resultArea.append("Humidity: " + weatherComparison.getHumidity() + " %\n");
        resultArea.append("Air Quality (Visibility): " + weatherComparison.getAirQuality()+ " meters\n");
        resultArea.append("Sunset Time: " + weatherComparison.getSunsetTime() + "\n");

        resultArea.append("\n"); // Spacer between city data
    }

    // Listener for Compare Button
    private class CompareButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            ArrayList<String> cities = new ArrayList<>();

            for (JTextField cityField : cityFields) {
                cities.add(cityField.getText());
            }

            // Clear previous results before appending new data
            StringBuilder result_text = new StringBuilder();

            result_text.append("Comparing Weather Data for ");
            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                if (i != NUMBER_OF_CITIES - 1) {
                    result_text.append(cityFields.get(i).getText()).append(" and ");
                }
                else {
                    result_text.append(cityFields.get(i).getText()).append(":\n");
                }
            }
            resultArea.setText(result_text.toString());

            // Retrieve and display weather data for both cities
            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                weatherComparison.get(i).setWeatherData(cities.get(i));
            }

            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                displayWeatherInTextArea(weatherComparison.get(i));
            }
        }
    }
}