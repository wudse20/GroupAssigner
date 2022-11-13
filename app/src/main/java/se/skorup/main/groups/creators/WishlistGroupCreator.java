package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
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
class WishlistGroupCreator extends GroupCreatorTemplate
{
    private final int startingPerson;
    private boolean shouldUseStartPerson;

    /**
     * Creates a new GroupCreator, with a starting person.
     *
     * @param p the person to start with.
     * */
    protected WishlistGroupCreator(int p)
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
        var added = new ImmutableHashSet<>(gm.getAllIdsOfRoll(Person.Role.CANDIDATE)).diff(left);
        return new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), id)).diff(added).toList();
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
    private Set<Integer> getWishedForIdsInLeft(GroupManager gm, Set<Integer> left, Set<Integer> ids)
    {
        return Tuple.imageOfSet(gm.getWishGraph(), ids);
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
     * @throws NoGroupAvailableException iff it isn't possible to get an id.
     * @return the id of the person with the fewest wishes left.
     * */
    private int getIdWithLeastWishes(
        GroupManager gm, Set<Integer> candidates, Collection<Integer> current
    ) throws NoGroupAvailableException
    {
        var list =
            candidates.stream()
                      .map(id -> new PersonWishEntry(id, getNumberWishes(gm, candidates, id)))
                      .sorted()
                      .map(PersonWishEntry::id)
                      .collect(Collectors.toCollection(LinkedList::new));


        var id = list.remove(0);
        while (!isPersonAllowed(id, current, gm))
        {
            if (list.isEmpty())
                throw new NoGroupAvailableException("Cannot create subgroups, too many denylist items");

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
            left.remove(id);
            return id;
        }

        // If we are continuing with a group then choose the person with the fewest wishes left.
        var wishes = new HashSet<>(getWishes(gm, left, lastId));

        if (!wishes.isEmpty())
        {
            var id = getIdWithLeastWishes(gm, wishes, current);
            left.remove(id);
            return id;
        }

        var candidates = getWishedForIdsInLeft(gm, left, current);

        if (!candidates.isEmpty())
        {
            var id = getIdWithLeastWishes(gm, candidates, current);
            left.remove(id);
            return id;
        }

        return getIdWithLeastWishes(gm, left, current);
    }

    @Override
    protected int getNextPerson(
        GroupManager gm, Set<Integer> left,
        Set<Integer> current, int lastId
    ) throws NoGroupAvailableException
    {
        if (shouldUseStartPerson)
        {
            shouldUseStartPerson = false;
            left.remove(startingPerson);
            return startingPerson;
        }

        return getOptimalPerson(gm, left, current, lastId);
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
