package WeatherComparator.GUI;
/*
    Add weather stats for a given city in its own panel
 */

import WeatherComparator.WeatherComparison;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CityPanel extends JPanel implements GUIConstants {
    JLabel cityName;
    ArrayList<JLabel> weather_stats;
    ImageIcon condition_image;

    // variables for image
    // convert every API's condition to its image name
    private static final String IMAGE_URL = "https://openweathermap.org/img/wn/";
    private static final String SIZE_MODIFIER = "@2x";
    Map<String, String> API_CONDITION_TO_IMG = new HashMap<>(Map.of(
            "clear sky", "01",
            "few clouds", "02",
            "scattered clouds", "03",
            "broken clouds", "04",
            "shower rain", "09",
            "rain", "10",
            "thunderstorm", "11",
            "snow", "13",
            "mist", "50"
    ));
    Calendar current_time;
    String day_or_night; // boolean value: 'd' for day and 'n' for night

    public CityPanel(WeatherComparison weatherComparison) {
        // set day or night bool
        // Currently not working?? LocalDateTime is probably easier....
        current_time = Calendar.getInstance();
        Calendar eight_pm = Calendar.getInstance();
        eight_pm.set(Calendar.HOUR, (12 + 8));

        // set to d by default
        day_or_night = "d";
        if (current_time.after(eight_pm)) {
            day_or_night = "n";
        }

        // set up image
        String image_name = null;

        for (String condition : API_CONDITION_TO_IMG.keySet()) {
            if (weatherComparison.getCondition().matches(condition)) {
                image_name = API_CONDITION_TO_IMG.get(condition);
                break;
            }
        }
        if (image_name == null) {
            throw new IllegalArgumentException("Error: condition " + weatherComparison.getCondition() + " could not be found");
        }

        // create new image
        try {
            condition_image = new ImageIcon(new URL(IMAGE_URL + image_name + day_or_night + SIZE_MODIFIER + ".png"));
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Error: Image URL " + IMAGE_URL + " could not be found");
        }
        JLabel imageLabel = new JLabel(condition_image);

        // city name
        cityName = new JLabel(weatherComparison.getCityName(), SwingConstants.CENTER);
        cityName.setFont(CITY_FONT);

        // add all weather stats
        weather_stats = new ArrayList<>();

        for (Object condition : weatherComparison.getConditions()) {
            // ignore city name (already boldly displayed)
            if (!condition.toString().matches(weatherComparison.getCityName())) {
                JLabel stat = new JLabel(condition.toString());
                stat.setFont(WEATHER_STATS_FONT);
                weather_stats.add(stat);
            }
        }

        // add to panel
        add(imageLabel, BorderLayout.PAGE_START);
        add(cityName, BorderLayout.NORTH);
        for (JLabel stat : weather_stats) {
            add(stat, BorderLayout.CENTER);
        }

        setBackground(CITY_BACKGROUND_COLOR);
        setVisible(true);
    }
}
