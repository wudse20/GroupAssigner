package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Optional;

/**
 * The base class for the statistics panels.
 * */
public abstract sealed class AbstractStatisticsPanel extends JPanel permits GroupStatisticsPanel, SubgroupStatisticsPanel
{
    protected final GroupManager gm;
    protected final int len = "Antal personer med 100 önskningar uppfyllda:".length();

    /**
     * Creates a new AbstractStatisticsPanel.
     *
     * @param gm the instance of the group manager in use.
     * */
    protected AbstractStatisticsPanel(GroupManager gm)
    {
        this.gm = gm;
    }

    /**
     * Updates the information on the panel.
     *
     * @param sg the subgroups that might exist, when updating.
     * */
    public final void updateStatistics(Optional<Subgroups> sg)
    {
        this.updateData(sg);
        this.addComponents();
    }

    /**
     * Initializes the panel.
     *
     * @param sg the optional of the subgroups that could be
     *           used in the updateData call.
     * */
    protected final void init(Optional<Subgroups> sg)
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
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
     * The basic layout for all the panels that are
     * AbstractStatisticsPanels.
     *
     * @param container the container panel that holds the information.
     * @param borderTitle the title of the border.
     * */
    protected void basicLayout(Container container, String borderTitle)
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setForeground(Utils.FOREGROUND_COLOR);
        cont.setLayout(new FlowLayout(FlowLayout.CENTER));
        cont.setBorder(getBorder(borderTitle));
        cont.add(container);

        this.add(new JLabel(" "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(cont, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel(" "), BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties of everything and anything.
     * */
    protected abstract void setProperties();

    /**
     * Updates the data.
     * */
    protected abstract void updateData(Optional<Subgroups> sg);
}
