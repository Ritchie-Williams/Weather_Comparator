import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastComparison
{
    APIData apiData;

    // Adds apiData to instance variable
    ForecastComparison(APIData apiData)
    { this.apiData = apiData;}

    // func groups the forecast by day --> user can choose by day --> ForecastPanel
    public Map<String, List<String>> groupForecastByDay(JSONObject forecastData)
    {
        Map<String, List<String>> dailyForecasts = new HashMap<>();
        JSONArray forecastList = forecastData.getJSONArray("list");

        for (int i = 0; i < forecastList.length(); i++)
        {
            JSONObject forecast = forecastList.getJSONObject(i);
            String dateTime = forecast.getString("dt_txt");
            String date = dateTime.split(" ")[0]; // get the date

            // format details
            String details = formatForecastDetails(forecast);

            dailyForecasts.putIfAbsent(date, new ArrayList<>());
            dailyForecasts.get(date).add(details);
        }
        return dailyForecasts;
    }

    private String formatForecastDetails(JSONObject forecast)
    {
        double temp = forecast.getJSONObject("main").getDouble("temp");
        double windSpeed = forecast.getJSONObject("wind").getDouble("speed");
        int humidity = forecast.getJSONObject("main").getInt("humidity");
        String description = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
        String time = forecast.getString("dt_txt");

        return String.format("Time: %s | Temp: %.1f°C | Wind: %.1f m/s | Humidity: %d%% | %s",
                time, temp, windSpeed, humidity, description);
    }

    //Gets forecast data for a city
    public JSONObject getForecast(String city)
    {
    try {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = apiData.FORECAST_API_URL() + encodedCity + "&appid=" + apiData.API_KEY() + "&units=metric";
        URL forecastUrl = new URL(url);
        //URL forecastUrl = new URL("https://api.openweathermap.org/data/2.5/forecast?q=conway&appid=1f6c71e9ca73a112616461c4835200e3&units=metric");
        HttpURLConnection connection = (HttpURLConnection) forecastUrl.openConnection();
        connection.setRequestMethod("GET");

        // error check---------------------------------------------------------------
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Error: Received HTTP response code " + responseCode);
            return null;
        }

        //store response from api into a string
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = input.readLine()) != null) {
            content.append(inputLine);
        }

        //close stream and disconnect api
        input.close();
        connection.disconnect();

        //return string as a JSONObject
        System.out.println("API Response: " + content.toString()); //---------------------------
        return new JSONObject(content.toString());
            } catch (Exception e) {
        System.out.println("Error gathering data for " + city + ": " + e.getMessage()); //error handling
                return null;
        }
    }

    //Displays forecasted data to console
    public void displayForecast(String city, JSONObject forecastData){
        if (forecastData == null)
        {
            System.out.println(city + " forecast data not available"); //error handling
        }
        else
        {
            // Access weather forecast list in JSONObject
            JSONArray forecastList = forecastData.getJSONArray("list");
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);

                // Data from "main" in JSONObject
                JSONObject main = forecast.getJSONObject("main");
                double temp = main.getDouble("temp");
                double windSpeed = forecast.getJSONObject("wind").getDouble("speed");
                int humidity = main.getInt("humidity");
                int airQuality = forecast.getInt("visibility");

                // Weather description
                JSONObject weather = forecast.getJSONArray("weather").getJSONObject(0);
                String description = weather.getString("description");

                // Date and time
                String dateTime = forecast.getString("dt_txt");

                //display forecast
                System.out.println("\nForecast Data for " + city + ":");
                System.out.println("Date and Time: " + dateTime);
                System.out.println("Temperature: " + temp + " C");
                System.out.println("Wind Speed: " + windSpeed + " m/s");
                System.out.println("Precipitation: " + humidity + "%");
                System.out.println("Air Quality (Visibility): " + airQuality + " meters");
                System.out.println("Weather: " + description);
            }
        }
    }
}
