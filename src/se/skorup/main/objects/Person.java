package se.skorup.main.objects;

import java.io.Serializable;

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

    @Override
    public String toString()
    {
        return "%s: %d".formatted(name, id);
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
