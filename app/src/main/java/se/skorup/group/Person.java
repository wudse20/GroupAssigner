package se.skorup.group;

import java.io.Serial;
import java.io.Serializable;

/**
 * A person in the GUI.
 *
 * @param name the name of the person.
 * @param id   the id of the person.
 * */
public record Person(String name, int id) implements Serializable, Comparable<Person>
{
    @Serial
    private static final long serialVersionUID = 7020998523010460504L;

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof Person p && id == p.id;
    }

    @Override
    public int compareTo(Person o)
    {
        return name.compareTo(o.name);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
