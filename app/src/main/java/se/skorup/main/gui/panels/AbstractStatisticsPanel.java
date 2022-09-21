package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

/**
 * The base class for the statistics panels.
 * */
public abstract sealed class AbstractStatisticsPanel extends JPanel permits GroupStatisticsPanel, SubgroupStatisticsPanel
{
    /**
     * Initializes the panel.
     * */
    protected final void init(Optional<Subgroups> sg)
    {
        this.setProperties();
        this.updateData(sg);
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
     * Gets the surrounding border.
     *
     * @param label the text that will be displayed on the border.
     * */
    protected Border getBorder(String label)
    {
        var border = BorderFactory.createTitledBorder(
            BorderFactory.createDashedBorder(null, 1.25f, 6, 4, true), label
        );

        border.setTitleColor(Utils.FOREGROUND_COLOR);
        return border;
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
    protected abstract void updateData(Optional<Subgroups> sg);
}
