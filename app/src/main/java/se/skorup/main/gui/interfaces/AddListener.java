package se.skorup.main.gui.interfaces;

import se.skorup.main.gui.events.AddEvent;

/**
 * The listener that listens
 * after the AddEvent
 * */
@FunctionalInterface
public interface AddListener
{
    /**
     * This is invoke iff a group has
     * been properly created.
     *
     * @param e the event that has been
     *          triggered.
     * */
    void groupCreated(AddEvent e);
}
