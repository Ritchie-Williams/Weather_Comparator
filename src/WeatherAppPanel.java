import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;

public class WeatherAppPanel extends JPanel
{
    private final WeatherComparison weatherComparison;
    private final ForecastComparison forecastComparison;
    private final JTextField city1Field;
    private final JTextField city2Field;
    private final JTextArea resultArea;
    private final JButton compareButton;
    private final JButton resetButton;
    private final JButton forecastButton;

    public WeatherAppPanel(APIData apiData) {
        this.weatherComparison = new WeatherComparison(apiData);
        this.forecastComparison = new ForecastComparison(apiData);

        // Setting layout
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Cities to Compare"));

        // City fields
        city1Field = new JTextField();
        city2Field = new JTextField();

        // Labels for cities
        inputPanel.add(new JLabel("First City:"));
        inputPanel.add(city1Field);
        inputPanel.add(new JLabel("Second City:"));
        inputPanel.add(city2Field);

        // Compare Button
        compareButton = new JButton("Compare");
        compareButton.addActionListener(new CompareButtonListener());
        inputPanel.add(compareButton);

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFields());
        inputPanel.add(resetButton);

        // forecast button
        forecastButton = new JButton("View Forecast");
        forecastButton.setVisible(false);  // hidden initially
        //forecastButton.addActionListener(new ForecastButtonListener());
        forecastButton.setBackground(new Color(230, 190, 255)); // light purple
        forecastButton.setBorder(new LineBorder(new Color(255, 200, 120), 2)); // light orange
        forecastButton.setOpaque(true);
        forecastButton.setFocusPainted(false);
        add(forecastButton, BorderLayout.SOUTH);


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
        city1Field.setText("");
        city2Field.setText("");
        resultArea.setText("");
    }

    // Method to display weather data in the JTextArea instead of the terminal
    private void displayWeatherInTextArea(String cityName, JSONObject weatherData) {
        if (weatherData != null) {
            double temperature = weatherData.getJSONObject("main").getDouble("temp");
            double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
            int humidity = weatherData.getJSONObject("main").getInt("humidity");
            int airQuality = weatherData.getInt("visibility");
            String sunsetTime = new java.text.SimpleDateFormat("HH:mm")
                    .format(new java.util.Date(weatherData.getJSONObject("sys").getLong("sunset") * 1000L));

            resultArea.append("\nWeather Data for " + cityName + ":\n");
            resultArea.append("Temperature: " + temperature + " °C\n");
            resultArea.append("Wind Speed: " + windSpeed + " m/s\n");
            resultArea.append("Humidity: " + humidity + " %\n");
            resultArea.append("Air Quality (Visibility): " + airQuality + " meters\n");
            resultArea.append("Sunset Time: " + sunsetTime + "\n");
        } else {
            resultArea.append("No weather data available for " + cityName + "\n");
        }
        resultArea.append("\n"); // Spacer between city data
    }

    // Listener for Compare Button
    private class CompareButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String city1 = city1Field.getText().trim();
            String city2 = city2Field.getText().trim();

            if (city1.isEmpty() || city2.isEmpty()) {
                JOptionPane.showMessageDialog(WeatherAppPanel.this, "Please enter both city names.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Clear previous results before appending new data
            resultArea.setText("Comparing Weather Data for " + city1 + " and " + city2 + ":\n");

            // Retrieve and display weather data for both cities
            JSONObject weatherData1 = weatherComparison.getWeatherData(city1);
            JSONObject weatherData2 = weatherComparison.getWeatherData(city2);

            displayWeatherInTextArea(city1, weatherData1);
            displayWeatherInTextArea(city2, weatherData2);

            // show forecast button
            forecastButton.setVisible(true);
        }
    }
}
