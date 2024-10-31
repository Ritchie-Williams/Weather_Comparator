import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class WeatherComparison {
    APIData apiData;

    // Add apiData to instance variable
    WeatherComparison(APIData apiData) { this.apiData = apiData; }

    // Method to get weather data for a city
    public JSONObject getWeatherData(String cityName) {
        try {
            // Encodes the city name to handle spaces and special characters
            String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            String urlString = apiData.WEATHER_API_URL() + encodedCityName + "&appid=" + apiData.API_KEY() + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            return new JSONObject(content.toString());
        } catch (Exception e) {
            System.out.println("Error retrieving data for " + cityName + ": " + e.getMessage());
            return null;
        }
    }

    // Method to display relevant weather data from a JSONObject
    public void displayWeatherComparison(String cityName, JSONObject weatherData) {
        if (weatherData != null) {
            double temperature = weatherData.getJSONObject("main").getDouble("temp");
            double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
            int humidity = weatherData.getJSONObject("main").getInt("humidity");
            int airQuality = weatherData.getInt("visibility");  // Approximate visibility as air quality
            String sunsetTime = new java.text.SimpleDateFormat("HH:mm")
                    .format(new java.util.Date(weatherData.getJSONObject("sys").getLong("sunset") * 1000L));

            System.out.println("\nWeather Data for " + cityName + ":");
            System.out.println("Temperature: " + temperature + " °C");
            System.out.println("Wind Speed: " + windSpeed + " m/s");
            System.out.println("Precipitation: " + humidity + " %");
            System.out.println("Air Quality (Visibility): " + airQuality + " meters");
            System.out.println("Sunset Time: " + sunsetTime);
        } else {
            System.out.println("No weather data available for " + cityName);
        }
    }
}
