package WeatherComparator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONObject;

public class WeatherComparison {
    private final APIData apiData;
    private final String[] API_URL = new String[]{"&appid=", "&units=metric"};
    private final static int MAIN_CONDITION = 2;

    private String cityName;
    private JSONObject cityWeather;
    private double temperature;
    private double windSpeed;
    private int humidity;
    private int airQuality;
    private String sunsetTime;
    private String condition;

    // constructors
    // Add apiData to instance variable
    public WeatherComparison(APIData apiData) { this.apiData = apiData;}

    public WeatherComparison(String cityName, APIData apiData) {
        this.apiData = apiData;
        setWeatherData(cityName);
    }
    // create JSONObject
    public void setWeatherData(String cityName) {
        this.cityName = cityName;
        getWeatherData(this.cityName);
    }

    // Helper method for the constructor to get weather data for a city stored as JSONObject cityWeather
    private void getWeatherData(String cityName) {
        try {
            // Encodes the city name to handle spaces and special characters
            String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            String urlString = apiData.WEATHER_API_URL() + encodedCityName + API_URL[0] + apiData.API_KEY() + API_URL[1];
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

            cityWeather = new JSONObject(content.toString());
            getWantedValues(cityWeather); // calling the helper method from here prevents cityWeather being null but confuses flow
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving data for " + cityName + ": " + e.getMessage());
        }
    }

    // helper function to get relevant data
    private void getWantedValues(JSONObject cityWeather) {
        temperature = cityWeather.getJSONObject("main").getDouble("temp");
        windSpeed = cityWeather.getJSONObject("wind").getDouble("speed");
        humidity = cityWeather.getJSONObject("main").getInt("humidity");
        airQuality = cityWeather.getInt("visibility");  // Approximate visibility as air quality
        sunsetTime = new java.text.SimpleDateFormat("HH:mm")
                .format(new java.util.Date(cityWeather.getJSONObject("sys").getLong("sunset") * 1000L));
        condition = cityWeather.getJSONArray("weather").getJSONObject(0)
                .getString("description"); // ex. 'rain', 'overcast clouds', etc
    }

    // getters
    public double getTemperature() { return temperature; }
    public double getWindSpeed() { return windSpeed; }
    public int getHumidity() { return humidity; }
    public int getAirQuality() { return airQuality; }
    public String getSunsetTime() { return sunsetTime; }
    public String getCityName() { return cityName; }
    public String getCondition() { return condition; }
    public ArrayList<Object> getConditions() {
        ArrayList<Object> conditions = new ArrayList<>();
        // update variables
        getWantedValues(cityWeather);
        // add to array list
        conditions.add("\nWeather Data for " + getCityName() + ":");
        conditions.add("Currently " + getCondition());
        conditions.add("Temperature: " + getTemperature() + " °C");
        conditions.add("Wind Speed: " + getWindSpeed() + " m/s");
        conditions.add("Precipitation: " + getHumidity() + " %");
        conditions.add("Air Quality (Visibility): " + getAirQuality() + " meters");
        conditions.add("Sunset Time: " + getSunsetTime());
        // return
        return conditions;
    }

    // Method to display relevant weather data from a weatherComparison object
    public void displayWeatherComparison() {
        System.out.println("\nWeather Data for " + cityName + ":");
        System.out.println("Temperature: " + temperature + " °C");
        System.out.println("Wind Speed: " + windSpeed + " m/s");
        System.out.println("Precipitation: " + humidity + " %");
        System.out.println("Air Quality (Visibility): " + airQuality + " meters");
        System.out.println("Sunset Time: " + sunsetTime);
    }
}