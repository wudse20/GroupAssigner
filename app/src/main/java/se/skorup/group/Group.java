package se.skorup.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Thread-Safe group manager.
 * */
public class Group
{
    private int currentId = 0;

    private final Map<Integer, Person> persons;
    private final String name;

    /**
     * Creates a new Group.
     *
     * @param name the name of the group.
     * */
    public Group(String name)
    {
        this.persons = new HashMap<>();
        this.name = name;
    }

    /**
     * Registers a new person to the group.
     *
     * @param name the name of the person to be added.
     * @return the id of the person.
     * */
    public synchronized int registerPerson(String name)
    {
        persons.put(currentId, new Person(name, currentId));
        return currentId++;
    }

    /**
     * Returns a list of all the names in the Group.
     *
     * @return a list of all the names.
     * */
    public synchronized Collection<String> getNames()
    {
        return persons.values()
                      .stream()
                      .map(Person::name)
                      .toList();
    }

    /**
     * Returns a set of all the ids.
     *
     * @return a set of all the ids in the group.
     * */
    public synchronized Collection<Integer> getIds()
    {
        return persons.keySet();
    }

    public String toString()
    {
        return name;
    }
}
