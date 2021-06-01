package se.skorup.main.gui.frames;

import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.manager.GroupManager;

import javax.swing.JFrame;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame
{
    /** The group manager in use. */
    private final GroupManager gm;

    /** The random group creator. */
    private final GroupCreator randomCreator;

    /** The wishlist group creator. */
    private final GroupCreator wishlistCreator;

    /**
     * Creates a new group frame.
     *
     * @param gm the group manager in use.
     * */
    public GroupFrame(GroupManager gm)
    {
        super("Skapa grupper");

        this.gm = gm;
        this.randomCreator = new RandomGroupCreator(gm);
        this.wishlistCreator = new WishlistGroupCreator(gm);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {

    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {

    }
}
