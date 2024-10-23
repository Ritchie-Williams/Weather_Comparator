import java.util.Scanner;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WeatherComparison weatherComparison = new WeatherComparison();

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

        scanner.close();
    }
}



