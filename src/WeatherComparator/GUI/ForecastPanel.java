package WeatherComparator.GUI;

import WeatherComparator.GlobalConstants;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class ForecastPanel extends JFrame implements GUIConstants, GlobalConstants
{
    private ArrayList<JList<String>> cityDayList;
    private ArrayList<JTextArea> cityForecastDetailArea;
    private ArrayList<String> cityName;
    private ArrayList<Map<String, List<String>>> cityForecastData;

    // constructor
    public ForecastPanel(ArrayList<String> cityName,
                         ArrayList<Map<String, List<String>>> cityForecastData) {
        this.cityName = cityName;
        this.cityForecastData = cityForecastData;
        StringBuilder title = new StringBuilder("Forecast Comparison: ");

        for (int i = 0; i < cityName.size(); i++) {
            if (i != cityName.size() - 1)
                title.append(cityName.get(i)).append(" and ");
            else
                title.append(cityName.get(i));
        }

        setTitle(title.toString());
        setSize(FORECAST_FRAME_DIMENSION);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // city Section
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            cityDayList.add(new JList<>(new DefaultListModel<>()));
            cityForecastDetailArea.add(new JTextArea(8,40));
            JPanel cityPanel = createCityForecastPanel(cityName, cityForecastData,
                    cityDayList, cityForecastDetailArea.get(i));
            mainPanel.add(cityPanel);

            if (i != NUMBER_OF_CITIES - 1)
                mainPanel.add(Box.createVerticalStrut(20));
        }

        // add main panel to frame
        add(mainPanel);
        populateDayLists();
    }

    private JPanel createCityForecastPanel(ArrayList<String> cityName, ArrayList<Map<String, List<String>>> forecastData,
                                           ArrayList<JList<String>> dayList, JTextArea forecastDetailsArea)
    {
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.Y_AXIS));
        cityPanel.setBorder(BorderFactory.createTitledBorder("Forecast for " + cityName));

        // city name label
        JLabel cityLabel = new JLabel("Select Date for " + cityName);
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityPanel.add(cityLabel);

        // date selection list
        ArrayList<JScrollPane> dayListScrollPane = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            dayList.get(i).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            int finalI = i;
            dayList.get(i).addListSelectionListener(e->displayForecastForSelectedDay(dayList.get(finalI), forecastDetailsArea,
                    (Map<String, List<String>>) forecastData));
            dayListScrollPane.add(new JScrollPane(dayList.get(i)));
            dayListScrollPane.get(i).setPreferredSize(FORECAST_SCROLL_PANE);
            cityPanel.add(dayListScrollPane.get(i));
        }
        // forecast details area
        forecastDetailsArea.setEditable(false);
        forecastDetailsArea.setLineWrap(true);
        forecastDetailsArea.setWrapStyleWord(true);
        JScrollPane forecastScrollPane = new JScrollPane(forecastDetailsArea);
        cityPanel.add(forecastScrollPane);

        return cityPanel;
    }

    private void populateDayLists()
    {
        assert cityDayList != null;
        assert cityForecastDetailArea != null;

        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            populateDayList(cityDayList.get(i), cityForecastData.get(i));
        }
    }

    private void populateDayList(JList<String> dayList, Map<String, List<String>> forecastData)
    {
        DefaultListModel<String> model = (DefaultListModel<String>) dayList.getModel();
        SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMMM dd");

        forecastData.keySet().forEach(date ->
        {
            try {
                Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                model.addElement(displayFormat.format(parsedDate));
            } catch (Exception e) {
                model.addElement(date);
            }
        });
    }

    private void displayForecastForSelectedDay(JList<String> dayList, JTextArea forecastArea, Map<String, List<String>> forecastData)
    {
        int selectedIndex = dayList.getSelectedIndex();
        if (selectedIndex != -1)
        {
            String selectedDate = (String) dayList.getModel().getElementAt(selectedIndex);
            forecastArea.setText("Forecast for " + selectedDate + "\n\n");
            List<String> forecasts = forecastData.get(getOriginalDateKey(selectedDate, forecastData));

            if (forecasts != null)
            {
                forecasts.forEach(forecast -> forecastArea.append(forecast + "\n"));
            }
        }
    }

    private String getOriginalDateKey(String formattedDate, Map<String, List<String>> forecastData)
    {
        for (String originalDate : forecastData.keySet())
        {
            try {
                Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(originalDate);
                String displayDate = new SimpleDateFormat("EEEE, MMMM dd").format(parsedDate);
                if (displayDate.equals(formattedDate))
                {
                    return originalDate;
                }
            } catch (Exception e)
            {
                // none
            }
        }
        return null;
    }
}
