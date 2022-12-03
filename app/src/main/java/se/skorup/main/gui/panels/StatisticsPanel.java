package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.manager.Group;
import se.skorup.main.objects.Subgroups;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.Optional;

/**
 * The panel used for displaying statistic about subgroups
 * and the currently selected group.
 * */
public class StatisticsPanel extends JPanel
{
    private final Group gm;
    private final GroupStatisticsPanel gsp;
    private final SubgroupStatisticsPanel ssp;

    /**
     * Creates a new Statistics panel.
     *
     * @param gm the instance of the GroupManager in use.
     * */
    public StatisticsPanel(Group gm)
    {
        this.gm = gm;
        this.gsp = new GroupStatisticsPanel(gm);
        this.ssp = new SubgroupStatisticsPanel(gm);

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

    /**
     * Updates the statistics of the subgroup part of this frame..
     *
     * @param sg the subgroups that the statistics should be based on.
     * */
    public void updateStatistics(Subgroups sg)
    {
        ssp.updateStatistics(Optional.ofNullable(sg));
    }
}
