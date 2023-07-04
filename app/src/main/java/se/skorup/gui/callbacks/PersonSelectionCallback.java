package se.skorup.gui.callbacks;

import se.skorup.group.Group;
import se.skorup.group.Person;

/**
 * The callback used when a person is selected.
 * */
@FunctionalInterface
public interface PersonSelectionCallback
{
    /**
     * The callback function.
     *
     * @param p the person affected.
     * @param g the group of the person affected.
     * */
    void personSelected(Person p, Group g);
}
