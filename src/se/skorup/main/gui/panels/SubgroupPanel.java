package se.skorup.main.gui.panels;

import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.JPanel;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel
{
    /** The instance of the GroupFrame this panel is a part off. */
    private final GroupFrame gf;

    /**
     * Creates a new SubgroupSettingsPanel..
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupPanel(GroupFrame gf)
    {
        this.gf = gf;
    }
}
