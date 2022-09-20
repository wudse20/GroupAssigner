package se.skorup.main.gui.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

/**
 * The base class for the statistics panels.
 * */
public abstract sealed class AbstractStatisticsPanel extends JPanel permits GroupStatisticsPanel
{
    /**
     * Creates an abstract statistics panel.
     * <br><br>
     * This will create update the data, set the properties
     * and add the components.
     * */
    public AbstractStatisticsPanel()
    {
        this.setProperties();
        this.updateData();
        this.addComponents();
    }

    /**
     * Setup for the labels to all look and feel the same.
     *
     * @param label the label that is being affected.
     * @param color the color that is the color of the ground of the label.
     * @return it will return itself. So when done it is will invoke {@code return label;}.
     * */
    protected final JLabel setUpLabel(JLabel label, Color color)
    {
        label.setForeground(color);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));

        return label;
    }

    /**
     * Adds the components.
     * */
    protected abstract void addComponents();

    /**
     * Sets the properties of everything and anything.
     * */
    protected abstract void setProperties();

    /**
     * Updates the data.
     * */
    protected abstract void updateData();
}
