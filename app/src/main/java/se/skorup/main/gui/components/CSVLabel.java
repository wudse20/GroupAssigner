package se.skorup.main.gui.components;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;

/**
 * A CSVLabel in the GUI for CSV editing.
 * */
public class CSVLabel extends JLabel
{
    private final int x;
    private final int y;

    /**
     * Creates a new CSV label with a label,
     * x and y position and a foreground and
     * background color.
     *
     * @param label the label of the label.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param background the background color.
     * @param foreground the foreground color.
     * */
    public CSVLabel(String label, int x, int y, Color background, Color foreground)
    {
        super(label, SwingConstants.CENTER);

        this.x = x;
        this.y = y;

        this.setForeground(foreground);
        this.setBackground(background);
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(foreground));
    }

    /**
     * Getter for: x
     *
     * @return the x-coordinate.
     * */
    public int getXCoordinate()
    {
        return x;
    }

    /**
     * Getter for: y
     *
     * @return the y-coordinate.
     * */
    public int getYCoordinate()
    {
        return y;
    }
}
