package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Subgroups;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel
{
    private final GroupFrame gf;

    private final GroupManager gm;

    private Subgroups current;

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupPanel(GroupFrame gf)
    {
        this.gf = gf;
        this.gm = gf.getManager();

        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

        gf.addActionListener(e -> gf.waitCursorAction(this::generateGroups), GroupButtonPanel.Buttons.CREATE);
    }

    /**
     * Gets the correct group generator,
     * based on all inputs.
     *
     * TODO: IMPLEMENT
     * */
    private GroupCreator getGroupGenerator()
    {
        return null;
    }

    /**
     * Generates the group.
     * */
    private void generateGroups()
    {
        var gc = getGroupGenerator();

        switch (gf.getSizeState())
        {
            case NUMBER_GROUPS:
            case NUMBER_PERSONS:
            case PAIR_WITH_LEADERS:
            case DIFFERENT_GROUP_SIZES:
        }
    }

    /**
     * Calculates the height of the panel. TODO: IMPLEMENT
     *
     * @return the calculated height of the panel.
     * */
    private int height()
    {
        return 0;
    }

    @Override
    public Dimension getPreferredSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }

    @Override
    public Dimension getMinimumSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }

    @Override
    public Dimension getMaximumSize()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) dim.getWidth(), Math.max((int) dim.getHeight(), height()));
    }
}
