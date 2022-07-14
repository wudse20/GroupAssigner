package se.skorup.main.objects;

import java.io.Serial;

/**
 * The class that represents a leader.
 * */
public final class Leader extends Person
{
    @Serial
    private static final long serialVersionUID = 5485343530584026120L;

    /**
     * Creates a new leader with a name.
     *
     * @param name the name of the person.
     * @param id   the id of the person.
     * */
    public Leader(String name, int id)
    {
        super(name, id);
    }

    @Override
    public Leader clone()
    {
        var newCandidate = new Leader(name, id);
        newCandidate.denylist = denylist;
        newCandidate.wishlist = wishlist;

        return newCandidate;
    }
}