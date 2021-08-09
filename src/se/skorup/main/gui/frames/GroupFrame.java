package se.skorup.main.gui.frames;

import se.skorup.main.gui.interfaces.ActionCallback;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;

import java.util.List;
import java.util.Vector;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame
{
    /** The list with all the callbacks. */
    private final List<ActionCallback> callbacks = new Vector<>();

    /**
     * Creates a new group frame.
     *
     * @param gm the group manager in use.
     * */
    public GroupFrame(GroupManager gm)
    {
        super("Skapa undergrupper!");
    }

    /**
     * Adds an action callback to the frame.
     *
     * @param ac the callback to be added. If {@code null}
     *           then it will do noting.
     * */
    public void addActionCallback(ActionCallback ac)
    {
        if (ac == null)
            return;

        callbacks.add(ac);
    }
}
