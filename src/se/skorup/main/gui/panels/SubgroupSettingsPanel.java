package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.JPanel;

/**
 * The settings for the subgroup generation.
 * */
public class SubgroupSettingsPanel extends JPanel
{
    /** The instance of the GroupFrame this panel is a part off. */
    private final GroupFrame gf;

    /**
     * Creates a new SubgroupSettingsPanel..
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupSettingsPanel(GroupFrame gf)
    {
        this.gf = gf;

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {

    }
}
