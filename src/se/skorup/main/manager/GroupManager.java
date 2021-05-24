package se.skorup.main.manager;

import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The manager that registers and keeps track of
 * the different people in a group.
 * */
public class GroupManager implements Serializable
{
    private static int currentId = 0;

    /**
     * The map that keeps track of all the persons,
     * key is the id of the person.
     * */
    private final Map<Integer, Person> group;

    /**
     * Creates a new empty GroupManager.
     * */
    public GroupManager()
    {
        this.group = new HashMap<>();
    }

    /**
     * Registers a person.
     *
     * @param name the name of the person.
     * @param r the role of the person.
     * @throws IllegalArgumentException iff the name or the role is null
     *                                  and the name is shorter than 3 chars.
     * */
    public void registerPerson(String name, Person.Role r) throws IllegalArgumentException
    {
        if (name == null)
            throw new IllegalArgumentException("The provided name cannot be null.");
        else if (r == null)
            throw new IllegalArgumentException("The provided role cannot be null.");
        else if (name.trim().length() < 3)
            throw new IllegalArgumentException(
                    "The provided name needs to be at least of length 3, provided length %d"
                    .formatted(name.trim().length())
            );

        var id = getNextId();

        switch (r)
        {
            case CANDIDATE -> group.put(id, new Candidate(name, id));
            case LEADER -> group.put(id, new Leader(name, id));
        }
    }

    /**
     * Removes a person from the group.
     *
     * @param id the id of the person to be
     *           removed.
     * @return {@code true} iff the person was removed,
     *          else {@code false}.
     * */
    public boolean removePerson(int id)
    {
        return group.remove(id) != null;
    }

    /**
     * Gets the next id of the person.
     * */
    public static int getNextId()
    {
        return currentId++;
    }
}
