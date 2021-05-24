package se.skorup.main.manager;

import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The manager that registers and keeps track of
 * the different people in a group.
 * */
public class GroupManager implements Serializable
{
    /** The next id that will be used. */
    private static int nextId = 0;

    /**
     * The map that keeps track of all the persons,
     * key is the id of the person.
     * */
    private final Map<Integer, Person> group;

    /**
     * Creates a new empty GroupManager.<br><br>
     *
     * This will reset the id counter.
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
     * @return the created person.
     * @throws IllegalArgumentException iff the name or the role is {@code null}
     *                                  and the name is shorter than 3 chars.
     * */
    public Person registerPerson(String name, Person.Role r) throws IllegalArgumentException
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
        Person p = (r.equals(Person.Role.LEADER)) ? new Leader(name, id) : new Candidate(name, id);
        group.put(id, p);
        return p;
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
     * Returns a set that contains all the persons.
     *
     * @return A set that contains all persons.
     * */
    public Set<Person> getAllPersons()
    {
        return new HashSet<>(group.values());
    }

    /**
     * Get all persons of roll r.
     *
     * @param r the roll that's wanted.
     * @return a set of persons with only the role r.
     * @throws IllegalArgumentException iff r is {@code null}.
     * */
    public Set<Person> getAllOfRoll(final Person.Role r)
    {
        return group.values()
                    .stream()
                    .filter(x ->
                        r.equals(Person.Role.LEADER) ?
                        x instanceof Leader :
                        x instanceof Candidate
                    ).collect(Collectors.toSet());
    }

    /**
     * Gets the next id of the person.
     * */
    public static int getNextId()
    {
        return nextId++;
    }
}
