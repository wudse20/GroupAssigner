package se.skorup.main.objects;

import java.io.Serial;

/**
 * TODO: rename.
 *
 * The class for the candidates.
 * */
public final class Candidate extends Person
{
    @Serial
    private static final long serialVersionUID = 5358269934766198466L;

    /**
     * Creates a new person with a name.
     *
     * @param name the name of the person.
     * @param id   the id of the person.
     * */
    public Candidate(String name, int id)
    {
        super(name, id);
    }

    @Override
    public Candidate clone()
    {
        var newCandidate = new Candidate(name, id);
        newCandidate.denylist = denylist;
        newCandidate.wishlist = wishlist;

        return newCandidate;
    }
}
