import javax.swing.*;
import java.awt.*;

public class ForecastPanel extends JFrame
{
    public ForecastPanel()
    {
        setTitle("Forecast Comparison Results");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allows only this window to close
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new BorderLayout());

        add(contentPanel);
    }

}
