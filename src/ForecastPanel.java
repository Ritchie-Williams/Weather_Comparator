import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class ForecastPanel extends JFrame
{
    private JList<String> city1DayList;
    private JList<String> city2DayList;
    private JTextArea city1ForecastDetailsArea;
    private JTextArea city2ForecastDetailsArea;
    private String city1Name;
    private String city2Name;
    private Map<String, List<String>> city1ForecastData;
    private Map<String, List<String>> city2ForecastData;

    public ForecastPanel(String city1Name, Map<String, List<String>> city1ForecastData,
                         String city2Name, Map<String, List<String>> city2ForecastData)
    {
        this.city1Name = city1Name;
        this.city1ForecastData = city1ForecastData;
        this.city2Name = city2Name;
        this.city2ForecastData = city2ForecastData;

        setTitle("Forecast Comparison: " + city1Name + " and " + city2Name);
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // city 1 Section
        city1DayList = new JList<>(new DefaultListModel<>());
        city1ForecastDetailsArea = new JTextArea(8, 40);
        JPanel city1Panel = createCityForecastPanel(city1Name, city1ForecastData, city1DayList, city1ForecastDetailsArea);
        mainPanel.add(city1Panel);
        mainPanel.add(Box.createVerticalStrut(20)); // Space between sections

        // city 2 Section
        city2DayList = new JList<>(new DefaultListModel<>());
        city2ForecastDetailsArea = new JTextArea(8, 40);
        JPanel city2Panel = createCityForecastPanel(city2Name, city2ForecastData, city2DayList, city2ForecastDetailsArea);
        mainPanel.add(city2Panel);

        // add main panel to frame
        add(mainPanel);
        populateDayLists();
    }

    private JPanel createCityForecastPanel(String cityName, Map<String, List<String>> forecastData,
                                           JList<String> dayList, JTextArea forecastDetailsArea)
    {
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.Y_AXIS));
        cityPanel.setBorder(BorderFactory.createTitledBorder("Forecast for " + cityName));

        // city name label
        JLabel cityLabel = new JLabel("Select Date for " + cityName);
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityPanel.add(cityLabel);

        // date selection list
        dayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dayList.addListSelectionListener(e -> displayForecastForSelectedDay(dayList, forecastDetailsArea, forecastData));
        JScrollPane dayListScrollPane = new JScrollPane(dayList);
        dayListScrollPane.setPreferredSize(new Dimension(150, 100));
        cityPanel.add(dayListScrollPane);

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
        populateDayList(city1DayList, city1ForecastData);
        populateDayList(city2DayList, city2ForecastData);
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
