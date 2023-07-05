package se.skorup.group;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private String name;

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
     * Sets the name of the group
     *
     * @param name the new name of the group.
     * */
    public synchronized void setName(String name)
    {
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
     * Removes a deny item.
     *
     * @param id1 the first id.
     * @param id2 the second id.
     * */
    public synchronized void removeDenyItem(int id1, int id2)
    {
        var l1 = denylist.getOrDefault(id1, new HashSet<>());
        var l2 = denylist.getOrDefault(id2, new HashSet<>());

        l1.remove(id2);
        l2.remove(id1);

        denylist.put(id1, l1);
        denylist.put(id2, l2);
    }

    /**
     * Checks if two ids are allowed in the same group.
     *
     * @param id1 the first id.
     * @param id2 the second id.
     * @return {@code true} iff a person is blocked else {@code false}
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
     * Removes wished to wishers wishlist.
     *
     * @param wisher the id of the person removing the wish.
     * @param wished the person that wisher was wishing for.
     * */
    public synchronized void removeWishItem(int wisher, int wished)
    {
        var set = wishlist.getOrDefault(wisher, new HashSet<>());
        set.remove(wished);
        wishlist.put(wisher, set);
    }

    /**
     * Gets all the wished ids for a person. Note that this result
     * might change, since it is only current value. This will not be
     * updated, and the return set is a copy, i.e., another instance.
     *
     * @param id the id that we are searching for.
     * @return a set of the ids that id has wished for.
     * */
    public synchronized Set<Integer> getWishedIds(int id)
    {
        return new HashSet<>(wishlist.getOrDefault(id, new HashSet<>()));
    }

    /**
     * Gets all the denied ids for a person. Note that this result
     * might change, since it is only current value. This will not be
     * updated, and the return set is a copy, i.e., another instance.
     *
     * @param id the id that we are searching for.
     * @return a set of the ids that is denied for id.
     * */
    public synchronized Set<Integer> getDeniedIds(int id)
    {
        return new HashSet<>(denylist.getOrDefault(id, new HashSet<>()));
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
     * Removes a person from the group.
     * */
    public synchronized void removePerson(int id)
    {
        persons.remove(id);
        wishlist.remove(id);
        denylist.remove(id);
    }

    /**
     * Returns a list of all the names currently in the Group.
     *
     * @return a list of all the names.
     * */
    public synchronized Collection<String> getNames()
    {
        return persons.values()
                      .stream()
                      .map(Person::name)
                      .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a set of all the ids currently in the group.
     *
     * @return a set of all the ids in the group.
     * */
    public synchronized Collection<Integer> getIds()
    {
        return new HashSet<>(persons.keySet());
    }

    /**
     * Returns a collection of the people currently in the group.
     *
     * @return a collection of all the people currently in the group.
     * */
    public synchronized Collection<Person> getPersons()
    {
        return new ArrayList<>(persons.values());
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

    /**
     * Gets all the persons that match the provided name.
     *
     * @param name the provided name to search after.
     * @return a list of all the persons matching the provided name.
     * */
    public synchronized List<Person> getPersonFromName(String name)
    {
        var al = new ArrayList<Person>();

        for (var p : persons.values())
        {
            if (p.name().equals(name))
                al.add(p);
        }

        return al;
    }

    @Override
    public synchronized String toString()
    {
        return name;
    }

    @Override
    public synchronized int hashCode()
    {
        return persons.hashCode() + wishlist.hashCode() + denylist.hashCode() + name.hashCode();
    }

    @Override
    public synchronized boolean equals(Object o)
    {
        return o instanceof Group g        &&
               persons.equals(g.persons)   &&
               wishlist.equals(g.wishlist) &&
               denylist.equals(g.denylist) &&
               name.equals(g.name);
    }
}
