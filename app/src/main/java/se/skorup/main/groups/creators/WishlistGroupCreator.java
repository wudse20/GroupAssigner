package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An alternate GroupCreator for wishlist generation.
 * */
public final class WishlistGroupCreator extends GroupCreatorTemplate
{
    private boolean shouldUseStartPerson;
    private Person startingPerson;

    /**
     * Creates a new GroupCreator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     * */
    public WishlistGroupCreator(GroupManager gm)
    {
        super(gm);
    }

    /**
     * Creates a new GroupCreator, with a starting person.
     *
     * @param gm the group manager used to create the subgroups.
     * @param p the person to start with.
     * */
    public WishlistGroupCreator(GroupManager gm, Person p)
    {
        this(gm);
        this.startingPerson = p;
        this.shouldUseStartPerson = true;
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
    private Person getNextPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException
    {
        if (current.size() == 0) // Starting a new subgroup.
        {
            if (added.size() == 0) // The first person.
            {
                p = getRandomPerson(candidates); // Gets a random person to start with.
            }
            else // We've already added a person, i.e. not first group; then we pick the person with the fewest wishes left.
            {
                // Takes the person with the fewest wishes left.
                p = candidates.stream()
                              .map(x -> new PersonWithWishesLeft(x, getWishes(wish, added, x.getId()).size()))
                              .sorted(Comparator.comparingInt(a -> a.wishes))
                              .toList()
                              .get(0)
                              .p();
            }
        }
        else // The group is already started.
        {
            var wishes = getWishes(wish, added, p.getId()); // The wishes of p.

            if (wishes.isEmpty()) // If there are no wishes left.
            {
                var list = new ArrayList<Person>();

                // Get all the persons that has wished for p.
                for (var c : candidates)
                {
                    if (getWishes(wish, added, c.getId()).contains(p.getId()))
                    {
                        list.add(c);
                    }
                }

                // No persons then just get a person that is allowed.
                if (list.isEmpty())
                {
                    p = getAllowedPerson(deny, current, candidates, added, p);
                }
                else
                {
                    p = list.stream()
                            .map(x -> new PersonWithWishesLeft(x, getWishes(wish, added, x.getId()).size()))
                            .sorted(Comparator.comparingInt(a -> a.wishes))
                            .toList()
                            .get(0)
                            .p();
                }
            }
            else
            {
                p = wishes.stream()
                          .map(gm::getPersonFromId)
                          .map(x -> new PersonWithWishesLeft(x, getWishes(wish, added, x.getId()).size()))
                          .sorted(Comparator.comparingInt(a -> a.wishes))
                          .toList()
                          .get(0)
                          .p();
            }
        }

        return p;
    }

    @Override
    protected Person getPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException
    {
        if (shouldUseStartPerson && startingPerson != null)
        {
            var person = startingPerson;
            startingPerson = null;
            return person;
        }

        return getNextPerson(current, candidates, wish, deny, added, p);
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar (borde aldrig synas)";
    }

    @Override
    public int hashCode()
    {
        return gm.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof WishlistGroupCreator wgc &&
               wgc.gm.equals(gm);
    }

    private record PersonWithWishesLeft(Person p, int wishes) {}
}
