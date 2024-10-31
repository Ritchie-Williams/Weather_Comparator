import java.io.IOException;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {
    // input the location of the text file with your api data
    // (comments are allowed with # and api key/url should be seperated by commas with no space)
    private final static String API_FILE = "APIData.txt";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();

        APIData apiData = null;

        try {
            apiData = parser.parseAPIData(API_FILE);
        }
        catch (IOException e) {
            System.out.println("Error: check path to API file (" + API_FILE + ") ...\n"
                    + e.getMessage());
        }

        WeatherComparison weatherComparison = new WeatherComparison(apiData);
        ForecastComparison forecastComparison = new ForecastComparison(apiData);

        // Gets the city names from user
        System.out.print("Enter the first city name: ");
        String city1 = scanner.nextLine();

        System.out.print("Enter the second city name: ");
        String city2 = scanner.nextLine();

        // Fetches weather data for both cities
        JSONObject weatherDataCity1 = weatherComparison.getWeatherData(city1);
        JSONObject weatherDataCity2 = weatherComparison.getWeatherData(city2);

        // Displays and compare weather data
        weatherComparison.displayWeatherComparison(city1, weatherDataCity1);
        weatherComparison.displayWeatherComparison(city2, weatherDataCity2);

        // Fetches forecast data for both cities
        JSONObject forecastDataCity1 = forecastComparison.getForecast(city1);
        JSONObject forecastDataCity2 = forecastComparison.getForecast(city2);

        // Displays forecast data
        forecastComparison.displayForecast(city1, forecastDataCity1);
        forecastComparison.displayForecast(city2, forecastDataCity2);

        scanner.close();
    }
}



