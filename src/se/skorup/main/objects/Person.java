package se.skorup.main.objects;

import se.skorup.main.Main;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The supertype for all persons.
 * */
public abstract class Person implements Serializable, Comparable<Person>
{
    /**
     * The enum that keeps track of the different
     * roles of the different persons.
     * */
    public enum Role
    {
        /** The candidate role, TODO: find new name. */
        CANDIDATE,
        /** The leader role. */
        LEADER
    }

    /**
     * The enum that keeps track of the
     * different main groups.
     * */
    public enum MainGroup
    {
        /** The first main group. */
        MAIN_GROUP_1,
        /** The second main group. */
        MAIN_GROUP_2
    }

    /** The name of the person. */
    protected final String name;

    /** The id of the person. */
    protected int id;

    /** The denylist of the person. */
    protected Set<Integer> denylist = new HashSet<>();

    /** The wishes of the person. */
    protected Set<Integer> wishlist = new HashSet<>();

    /** The main group of this person. */
    protected MainGroup mg = MainGroup.MAIN_GROUP_1;

    /**
     * Creates a new person with a name.
     *
     * @param name the name of the person.
     * @param id the id of the person.
     * */
    protected Person(String name, int id)
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
     * Setter for: id <br><br>
     *
     * WARNING DO NOT USE. only used for tests.
     *
     * @param id the new id.
     * */
    public void setId(int id)
    {
        this.id = id;
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

    /**
     * Sets the denylist of this person. <br><br>
     *
     * This method clones the input.
     *
     * @param persons the new set of persons on
     *                the denylist. If {@code null}
     *                then it will return without
     *                doing anything.
     * */
    public void setDenylist(Set<Person> persons)
    {
        if (persons == null)
            return;

        this.denylist = persons.stream().map(Person::getId).collect(Collectors.toSet());
    }

    /**
     * Sets wishlist of this person. <br><br>
     *
     * This method clones the input.
     *
     * @param persons the new set of persons on
     *                the wishlist. If {@code null}
     *                then it will return without
     *                doing anything.
     * */
    public void setWishlist(Set<Person> persons)
    {
        if (persons == null)
            return;

        this.wishlist = persons.stream().map(Person::getId).collect(Collectors.toSet());
    }

    /**
     * Getter for: MainGroup
     *
     * @return the main group of this person.
     * */
    public MainGroup getMainGroup()
    {
        return this.mg;
    }

    /**
     * Setter for: MainGroup
     *
     * @param mg the new MainGroup of this person.
     * */
    public void setMainGroup(MainGroup mg)
    {
        this.mg = mg;
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

    @Override
    public Person clone()
    {
        try
        {
            return (Person) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

    @Override
    public int compareTo(Person p)
    {
        return Integer.compare(id, p.id);
    }
}
