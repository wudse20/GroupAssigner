package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.groups.exceptions.GroupCreationFailedException;
import se.skorup.main.manager.Group;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creates subgroups respecting the wishes by the candidates,
 * trying to fulfill as many wishes as possible. This algorithm
 * will not generate multiple results.
 * */
public class WishlistGroupCreator extends GroupCreatorTemplate
{
    /** The id of the starting person. */
    protected final int startingPerson;
    private boolean shouldUseStartPerson;

    /**
     * Creates a new GroupCreator, without a starting person.
     * <br><br>
     * This is used for debugging purposes.
     * */
    public WishlistGroupCreator()
    {
        this(-1);
        this.shouldUseStartPerson = false;
    }

    /**
     * Creates a new GroupCreator, with a starting person.
     *
     * @param p the person to start with.
     * */
    public WishlistGroupCreator(int p)
    {
        this.startingPerson = p;
        this.shouldUseStartPerson = true;
    }

    /**
     * Gets the wishes of a person with id: id. This method will only
     * return the persons that aren't included in any subgroup.
     *
     * @param gm the Group in charge.
     * @param left the persons that aren't yet used.
     * @param id the id of the person to be checked against.
     * @return a list with the wishes for this id.
     * */
    private List<Integer> getWishes(Group gm, Set<Integer> left, int id)
    {
        return new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), id)).intersection(left).toList();
    }

    /**
     * Gets the number wishes of a person with id: id. This method will only
     * return the persons that aren't included in any subgroup.
     *
     * @param gm   the Group in charge.
     * @param left the persons that aren't yet used.
     * @param id   the id of the person to be checked against.
     * @return the number of wishes for this id.
     */
    private int getNumberWishes(Group gm, Set<Integer> left, int id)
    {
        return getWishes(gm, left, id).size();
    }

    /**
     * Gets the person with the least wishes left int the stream.
     *
     * @param candidates the candidets to be choosen from.
     * @param gm the Group in charge.
     * @param left the persons left to use.
     * */
    private int getLeastWishes(Stream<Integer> candidates, Group gm, HashSet<Integer> left)
    {
        return candidates.map(x -> new PersonWishEntry(x, getNumberWishes(gm, left, x)))
                         .sorted()
                         .toList()
                         .get(0)
                         .id;
    }

    /**
     * Gets the optimal person at this moment, this is very greedy
     * and might not give an optimal solution, but is good enough.
     *
     * @param gm the Group in charge.
     * @param left the unused ids.
     * @param current the currently worked on subgroup.
     * @param lastId the id of the last person chosen.
     * @return the id of the next optimal person.
     * */
    private int getOptimalPerson(Group gm, Set<Integer> left, Set<Integer> current, int lastId)
    {
        var l = // Removes all the id:s that aren't allowed.
            left.stream()
                .filter(i -> isPersonAllowed(i, current, gm))
                .collect(Collectors.toCollection(HashSet::new));

        if (l.isEmpty())
            throw new GroupCreationFailedException("Cannot create group, too many denylist items!");

        if (current.isEmpty()) // First person of the subgroup
        {
            return getLeastWishes(l.stream(), gm, l);
        }

        // The subgroup is already started.
        var wishes = getWishes(gm, l, lastId);

        if (wishes.isEmpty()) // If there are no wishes left.
        {
            var list = new ArrayList<Integer>();

            // Get all the persons that has wished for c.
            for (var c : l) {
                if (getWishes(gm, l, c).contains(lastId))
                    list.add(c);
            }

            if (list.isEmpty()) {
                // Get a random person that hasn't been used.
                return new ArrayList<>(l).get(new Random().nextInt(l.size()));
            } else {
                return getLeastWishes(list.stream(), gm, l);
            }
        }
        else
        {
            return getLeastWishes(wishes.stream(), gm, l);
        }
    }

    @Override
    protected int getNextPerson(
        Group gm, Set<Integer> left,
        Set<Integer> current, int lastId
    ) throws GroupCreationFailedException
    {
        if (shouldUseStartPerson)
        {
            shouldUseStartPerson = false;
            left.remove(startingPerson);
            return startingPerson;
        }

        var opt = getOptimalPerson(gm, left, current, lastId);
        left.remove(opt);
        return opt;
    }

    @Override
    public String toString()
    {
        return "REPORT IF YOU SEE THIS; RAPPORTERA OM DU SER DETTA!";
    }

    private record PersonWishEntry(int id, int nbrWishes) implements Comparable<PersonWishEntry>
    {
        @Override
        public int compareTo(PersonWishEntry other)
        {
            return Integer.compare(nbrWishes, other.nbrWishes);
        }
    }
}
