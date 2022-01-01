package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.List;
import java.util.Set;

/**
 * The group creator used to create groups
 * based on the wishes of the candidates.
 *  */
public sealed class WishlistGroupCreator extends GroupCreatorTemplate permits AlternateWishlistGroupCreator
{
    /**
     * Creates a wishlist group creator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
    public WishlistGroupCreator(GroupManager gm)
    {
        super(gm);
    }

    /**
     * Gets the next person.
     *
     * @param current the currently worked on group.
     * @param candidates the unused candidates.
     * @param wish the wishes of this group.
     * @param deny the denylist of this group.
     * @param added the persons that are already used.
     * @param p the current person.
     * @throws NoGroupAvailableException iff there are no possible groups.
     * */
    protected Person getPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException
    {
        assert current != null; // Just to stop it from complaining.
        if (p == null)
        {
            return getRandomPerson(candidates);
        }
        else
        {
            var wishes = getWishes(wish, p.getId());

            if (wishes.isEmpty())
            {
                p = getAllowedPerson(deny, current, candidates, getRandomPerson(candidates));
            }
            else
            {
                for (int j : wishes)
                {
                    p = gm.getPersonFromId(j);

                    if (!added.contains(j) && !isPersonDisallowed(deny, current, p.getId()))
                        break;

                    p = null;
                }

                if (p == null) // No wishes; that aren't blocked so resorts to random group generation.
                    p = getAllowedPerson(deny, current, candidates, getRandomPerson(candidates));
                else
                    candidates.remove(p);
            }
        }

        return p;
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskelista";
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof WishlistGroupCreator gc &&
                this.toString().equals(gc.toString());
    }

    public GroupManager gm()
    {
        return gm;
    }

}
