import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ForecastComparison {
    APIData apiData;

    // Add apiData to instance variable
    ForecastComparison(APIData apiData) { this.apiData = apiData;}

    //Gets forecast data for a city
    public JSONObject getForecast(String city) {
    try {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = apiData.API_URL() + encodedCity + "&appid=" + apiData.API_KEY() + "&units=metric";
        URL forecastUrl = new URL(url);

        //api request
        HttpURLConnection connection = (HttpURLConnection) forecastUrl.openConnection();
        connection.setRequestMethod("GET");

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
        return new JSONObject(content.toString());
            } catch (Exception e) {
        System.out.println("Error gathering data for " + city + ": " + e.getMessage()); //error handling
                return null;
        }
    }

    //Displays forecasted data to console
    public void displayForecast(String city, JSONObject forecastData){
        if (forecastData == null){
            System.out.println(city + " forecast data not available"); //error handling
        } else {
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
