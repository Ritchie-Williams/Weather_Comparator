package WeatherComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main implements ErrorCodes, GlobalConstants {
    // assume apiData is here for user
    private final static String apiDataPath = "./apiData.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();
        APIData apiData = null;

        // try to get api data and refuse to continue if unsuccessful
        try {
            apiData = parser.parseAPIData(apiDataPath);
        } catch (IOException e) { // maybe potentially outputing the API key is a bad idea...
            System.out.println("Error: check API data.");
            System.exit(API_DATA_INVALID);
        }
        if (apiData == null) {
            System.out.println("Error: check API data.");
            System.exit(API_DATA_INVALID);
        }

        ArrayList<WeatherComparison> weatherComparators = new ArrayList<>();

        while(weatherComparators.size() != NUMBER_OF_CITIES) {
            System.out.println("Enter the name of the city you would like to compare weather: ");

            if (scanner.hasNextLine()) {
                WeatherComparison weatherComparison = new WeatherComparison(apiData);
                weatherComparison.setWeatherData(scanner.nextLine());
                weatherComparators.add(weatherComparison);
            }
        }

        for (WeatherComparison weatherComparison : weatherComparators) {
            weatherComparison.displayWeatherComparison();
        }

        //ForecastComparison forecastComparison = new ForecastComparison(apiData);
    }
}