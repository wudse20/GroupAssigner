package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;

import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * The panel used for displaying statistic about subgroups
 * and the currently selected group.
 * */
public class StatisticsPanel extends JPanel
{
    private final GroupManager gm;
    private final GroupStatisticsPanel gsp;
    private final SubgroupStatisticsPanel ssp;

    /**
     * Creates a new Statistics panel.
     *
     * @param gm the instance of the GroupManager in use.
     * */
    public StatisticsPanel(GroupManager gm)
    {
        this.gm = gm;
        this.gsp = new GroupStatisticsPanel(gm);
        this.ssp = new SubgroupStatisticsPanel();

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the group.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(new GridLayout(2, 1));
    }

    /**
     * Adds the components to the panel.
     * */
    private void addComponents()
    {
        this.add(gsp);
        this.add(ssp);
    }
}
