package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.groups.exceptions.GroupCreationFailedException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Tuple;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Creates subgroups respecting the wishes by the candidates,
 * trying to fulfill as many wishes as possible. This algorithm
 * will not generate multiple results.
 * */
public class WishlistGroupCreator extends GroupCreatorTemplate
{
    private final int startingPerson;
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
     * @param gm the GroupManager in charge.
     * @param left the persons that aren't yet used.
     * @param id the id of the person to be checked against.
     * @return a list with the wishes for this id.
     * */
    private List<Integer> getWishes(GroupManager gm, Set<Integer> left, int id)
    {
        return new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), id)).intersection(left).toList();
    }

    /**
     * This method - with a super crappy name, gets persons that has wished for
     * anyone in the ids set, that still in left.
     *
     * @param gm the GroupManager in charge.
     * @param left the unused persons.
     * @param ids the ids that are the targets in the check.
     * @return a set containing the result.
     * */
    private Set<Integer> getWishedForIdsInLeft(GroupManager gm, Collection<Integer> left, Set<Integer> ids)
    {
        var res = Tuple.imageOfSet(gm.getWishGraph(), ids);
        res.retainAll(left);
        return res;
    }

    /**
     * Gets the number wishes of a person with id: id. This method will only
     * return the persons that aren't included in any subgroup.
     *
     * @param gm   the GroupManager in charge.
     * @param left the persons that aren't yet used.
     * @param id   the id of the person to be checked against.
     * @return the number of wishes for this id.
     */
    private int getNumberWishes(GroupManager gm, Set<Integer> left, int id)
    {
        return getWishes(gm, left, id).size();
    }

    /**
     * Gets the ID with the fewest wishes left.
     *
     * @param gm the GroupManager in charge.
     * @param candidates the candidates for this.
     * @throws GroupCreationFailedException iff it isn't possible to get an id.
     * @return the id of the person with the fewest wishes left. -1 if and only
     *         if it fails with the creation.
     * */
    private int getIdWithLeastWishes(
        GroupManager gm, Set<Integer> candidates, Collection<Integer> current
    ) throws GroupCreationFailedException
    {
        var list =
            candidates.stream()
                      .map(id -> new PersonWishEntry(id, getNumberWishes(gm, candidates, id)))
                      .sorted()
                      .map(PersonWishEntry::id)
                      .collect(Collectors.toCollection(LinkedList::new));

        var id = list.remove(0);
        while (!isPersonAllowed(id, current, gm) || current.contains(id))
        {
            if (list.isEmpty())
                return -1;

            id = list.remove(0);
        }

        return id;
    }

    /**
     * Gets the optimal person at this moment, this is very greedy
     * and might not give an optimal solution, but is good enough.
     *
     * @param gm the GroupManager in charge.
     * @param left the unused ids.
     * @param current the currently worked on subgroup.
     * @param lastId the id of the last person chosen.
     * @return the id of the next optimal person.
     * */
    private int getOptimalPerson(GroupManager gm, Set<Integer> left, Set<Integer> current, int lastId)
    {
        // If it's time for a new subgroup choose with the fewest wishes left.
        if (current.isEmpty())
        {
            var id = getIdWithLeastWishes(gm, left, current);

            if (current.contains(id))
                throw new GroupCreationFailedException("Please report! current.contains(id) :: new subgroup");

            if (id != -1)
            {
                left.remove(id);
                return id;
            }
        }

        // If we are continuing with a group then choose the person with the fewest wishes left.
        var wishes = new HashSet<>(getWishes(gm, left, lastId));

        if (!wishes.isEmpty())
        {
            var id = getIdWithLeastWishes(gm, wishes, current);

            if (current.contains(id))
                throw new GroupCreationFailedException("Please report! current.contains(id) :: wishes");

            if (id != -1)
            {
                left.remove(id);
                return id;
            }
        }

        var candidates = getWishedForIdsInLeft(gm, left, current);

        if (!candidates.isEmpty())
        {
            var id = getIdWithLeastWishes(gm, candidates, current);

            if (current.contains(id))
                throw new GroupCreationFailedException("Please report! current.contains(id) :: optimal");

            if (id != -1)
            {
                left.remove(id);
                return id;
            }
        }

        var id = getIdWithLeastWishes(gm, left, current);

        if (id == -1)
            throw new GroupCreationFailedException("GroupCreation failed, too many denylist items.");

        return id;
    }

    @Override
    protected int getNextPerson(
        GroupManager gm, Set<Integer> left,
        Set<Integer> current, int lastId
    ) throws GroupCreationFailedException
    {
        if (shouldUseStartPerson)
        {
            shouldUseStartPerson = false;
            left.remove(startingPerson);
            return startingPerson;
        }

        return getOptimalPerson(gm, left, current, lastId);
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
