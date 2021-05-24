package se.skorup.main.gui.frames;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainFrame of the GUI.
 * */
public class MainFrame extends JFrame
{
    /** The frame's container. */
    private final Container cp = this.getContentPane();

    /** The layout of the frame.*/
    private final BorderLayout layout = new BorderLayout();

    /** The group managers. */
    private final List<GroupManager> managers = new ArrayList<>();

    /**
     * Creates a new MainFrame.
     * */
    public MainFrame()
    {
        super("Gruppskapare");
        this.init();
    }

    /**
     * Initializes the frame.
     * */
    private void init()
    {
        DebugMethods.log("Starting initialization of MainFrame.", DebugMethods.LogType.DEBUG);
        this.setProperties();
        DebugMethods.log("The properties has been set.", DebugMethods.LogType.DEBUG);
        this.setVisible(true);
        DebugMethods.log("Frame is now initialized", DebugMethods.LogType.DEBUG);
    }

    /**
     * Sets the properties of the frame and
     * its components.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 300));

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);
    }
}
