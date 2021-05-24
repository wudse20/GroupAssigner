package se.skorup.main.gui.panels;

import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.GroupManager;

import javax.swing.JPanel;
import java.util.List;

/**
 * The control panel at the top of the MainFrame.
 * */
public class ControlPanel extends JPanel
{
    /** The reference to the managers. */
    private final List<GroupManager> managers;

    private final MainFrame mf;

    /**
     * Creates a new ControlPanel
     *
     * @param mf the instance of the mainframe.
     * @param managers the instance of the managers.
     * */
    public ControlPanel(MainFrame mf, List<GroupManager> managers)
    {
        this.mf = mf;
        this.managers = managers;
    }
}
