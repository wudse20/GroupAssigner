package se.skorup.main.gui.interfaces;

import se.skorup.main.objects.Person;

import java.util.List;

/**
 * A callback from PersonList.
 * */
@FunctionalInterface
public interface PersonCallback
{
    /**
     * An action that will be ran when the list is pressed.
     *
     * @param persons the person from the list selection.
     * */
    void action(List<Person> persons);
}
