package se.skorup.main.gui.frames;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * The frame that houses the custom CSV parsing
 * of groups.
 * */
public class CSVFrame extends JFrame
{
    private final List<ActionCallbackWithParam<GroupManager>> callbacks = new ArrayList<>();

    private final Container cp = this.getContentPane();

    /**
     * Creates a new CSVFrame.
     * */
    public CSVFrame()
    {
        super("Skapa grupper utifrån CSV.");
        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(1600, 900));
        this.setVisible(true);

        cp.setLayout(new BorderLayout());
        cp.setBackground(Utils.BACKGROUND_COLOR);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {

    }

    /**
     * Invokes the callbacks.
     *
     * @param gm the created group manager.
     * */
    private void invokeCallbacks(GroupManager gm)
    {
        callbacks.forEach(ac -> ac.action(gm));
    }

    /**
     * Adds an action callback.
     *
     * @param ac the action callback to be added.
     * */
    public void addActionCallback(ActionCallbackWithParam<GroupManager> ac)
    {
        if (ac != null)
            callbacks.add(ac);
    }
}
