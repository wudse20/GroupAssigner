package se.skorup.main.gui.panels;

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
    }
}
