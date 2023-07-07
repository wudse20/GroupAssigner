package se.skorup.gui.callbacks;

import se.skorup.group.Group;
import se.skorup.group.Person;

/**
 * The callback used when a person is deleted
 * from a group.
 * */
@FunctionalInterface
public interface DeleteCallback
{
    /**
     * The callback is invoked when a
     * person is deleted from a group.
     *
     * @param g the group that the person is deleted from.
     * @param p the person that is being deleted.
     * */
    void onDelete(Group g, Person p);
}
