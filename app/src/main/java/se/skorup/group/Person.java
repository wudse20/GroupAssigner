package se.skorup.group;

/**
 * A person in the GUI.
 *
 * @param name the name of the person.
 * @param id the id of the person.
 * */
public record Person(String name, int id)
{
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
}