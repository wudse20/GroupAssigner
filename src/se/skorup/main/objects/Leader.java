package se.skorup.main.objects;

/**
 * The class that represents a leader.
 * */
public final class Leader extends Person
{
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