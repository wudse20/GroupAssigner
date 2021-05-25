package se.skorup.main.objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The supertype for all persons.
 * */
public abstract class Person implements Serializable
{
    /**
     * The enum that keeps track of the different
     * roles of the different persons.
     * */
    public enum Role
    {
        CANDIDATE, LEADER
    }

    /** The name of the person. */
    private final String name;

    /** The id of the person. */
    private final int id;

    /** The denylist of the person. */
    private final Set<Integer> denylist = new HashSet<>();

    /** The wishes of the person. */
    private final Set<Integer> wishlist = new HashSet<>();

    /**
     * Creates a new person with a name.
     *
     * @param name the name of the person.
     * @param id the id of the person.
     * */
    public Person(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    /**
     * Adds an id to the wishlist.
     *
     * @param id the id to add to the wishlist.
     * @return {@code true} iff the id was added to the
     *         list, else {@code false}.
     * */
    public boolean addWishlistId(int id)
    {
        return wishlist.add(id);
    }

    /**
     * Adds an id to the denylist.
     *
     * @param id the id to add to the denylist.
     * @return {@code true} iff the id was added to the
     *         list, else {@code false}.
     * */
    public boolean addDenylistId(int id)
    {
        return denylist.add(id);
    }

    /**
     * Getter for: name
     *
     * @return the name of the person.
     * */
    public String getName()
    {
        return name;
    }

    /**
     * Getter for: id
     *
     * @return the id of the person.
     * */
    public int getId()
    {
        return id;
    }

    /**
     * Getter for: denylist
     *
     * @return the denylist as an primitive int array.
     * */
    public int[] getDenylist()
    {
        return this.denylist.stream().mapToInt(x -> x).toArray();
    }

    /**
     * Getter for: wishlist
     *
     * @return the wishlist as an primitive int array.
     * */
    public int[] getWishlist()
    {
        return this.wishlist.stream().mapToInt(x -> x).toArray();
    }

    @Override
    public String toString()
    {
        return "%s, id: %d".formatted(name, id);
    }

    /**
     * A person is equal to another person
     * iff their id's are the same.
     *
     * @param o the other person to compare against.
     * */
    @Override
    public boolean equals(Object o)
    {
        if (
            o instanceof Leader && this instanceof Candidate ||
            o instanceof Candidate && this instanceof Leader
        )
            return false;

        return o instanceof Person p && id == p.id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
