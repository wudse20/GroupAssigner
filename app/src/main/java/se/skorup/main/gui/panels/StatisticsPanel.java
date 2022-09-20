package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JPanel;

/**
 * The panel used for displaying statistic about subgroups
 * and the currently selected group.
 * */
public class StatisticsPanel extends JPanel
{
    /**
     * Creates a new Statistics panel.
     * */
    public StatisticsPanel()
    {
        this.setProperties();
    }

    /**
     * Sets the properties of the group.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
    }
}
