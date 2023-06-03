package se.skorup.group;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Thread-Safe group manager.
 * */
public class Group implements Serializable
{
    @Serial
    private static final long serialVersionUID = 622788425016189858L;

    private int currentId = 0;

    private final Map<Integer, Person> persons;
    private final Map<Integer, Set<Integer>> denylist;
    private final Map<Integer, Set<Integer>> wishlist;

    private final String name;

    /**
     * Creates a new Group.
     *
     * @param name the name of the group.
     * */
    public Group(String name)
    {
        this.persons = new HashMap<>();
        this.denylist = new HashMap<>();
        this.wishlist = new HashMap<>();
        this.name = name;
    }

    /**
     * Adds id1 and id2 so that they will both be denied.
     *
     * @param id1 the first person to be denied.
     * @param id2 the second person to be denied.
     * */
    public synchronized void addDenyItem(int id1, int id2)
    {
        var l1 = denylist.getOrDefault(id1, new HashSet<>());
        var l2 = denylist.getOrDefault(id2, new HashSet<>());

        l1.add(id2);
        l2.add(id1);

        denylist.put(id1, l1);
        denylist.put(id2, l2);
    }

    /**
     * Checks if two ids are allowed in the same group.
     *
     * @param id1 the first id.
     * @param id2 the second id.
     * */
    public synchronized boolean isDenied(int id1, int id2)
    {
        return denylist.getOrDefault(id1, new HashSet<>()).contains(id2) ||
               denylist.getOrDefault(id2, new HashSet<>()).contains(id1);
    }

    /**
     * Adds wished to wishers wishlist.
     *
     * @param wisher the id of the person doing the wish.
     * @param wished the person that wisher is wishing for.
     * */
    public synchronized void addWishItem(int wisher, int wished)
    {
        var set = wishlist.getOrDefault(wisher, new HashSet<>());
        set.add(wished);
        wishlist.put(wisher, set);
    }

    /**
     * Gets all the wished ids for a person. Note that this result
     * might change, since it is only current value. This will not be
     * updated and the return set is a copy, i.e. another instance.
     *
     * @param id the id that we are searching for.
     * @return a set of the ids that id has wished for.
     * */
    public synchronized Set<Integer> getWishedIds(int id)
    {
        return new HashSet<>(wishlist.getOrDefault(id, new HashSet<>()));
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
        return new HashSet<>(persons.keySet());
    }

    /**
     * Gets the size of the group, i.e., the number of
     * persons in it.
     *
     * @return the number persons in the group.
     * */
    public synchronized int size()
    {
        return persons.size();
    }

    @Override
    public String toString()
    {
        return name;
    }
}