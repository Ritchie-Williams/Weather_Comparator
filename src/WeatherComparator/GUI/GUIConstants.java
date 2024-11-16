package WeatherComparator.GUI;

/*
    Holds all constants for the GUI implementation
 */

import WeatherComparator.ErrorCodes;
import WeatherComparator.GlobalConstants;

import java.awt.*;

// every GUI class will get error codes as well
public interface GUIConstants extends ErrorCodes {
    /*
        GUI MAIN
     */

    /*
        WEATHER APP PANEL
     */
    /*
        FORECAST PANEL
     */
    Dimension FORECAST_FRAME_DIMENSION = new Dimension(700, 800);
    Dimension FORECAST_SCROLL_PANE = new Dimension(150, 100);

    /*
        CITY PANEL
     */
    Font CITY_FONT = new Font("Helvetica", Font.ITALIC, 30);
    Font WEATHER_STATS_FONT = new Font("Helvetica", Font.PLAIN, 20);
    Color CITY_BACKGROUND_COLOR = new Color(50,150, 200);
}
