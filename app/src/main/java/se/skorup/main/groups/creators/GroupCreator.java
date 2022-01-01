package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * The interface holding the methods
 * for creating sub groups in the program.
 * */
public interface GroupCreator
{
    /**
     * Generates the different, groups of size
     * size.
     *
     * @param size the size of the group.
     * @param overflow if the groups should overflow or not.
     * @return a List containing the generated groups.
     * @throws NoGroupAvailableException iff there's no way to create a group.
     * */
    List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException;

    /**
     * Generates subgroups of the provided
     * GroupManager, with a specified group size.
     * If it cannot evenly divide the groups into
     * groups of size groupSize, it will create
     * the last group smaller than the group size.
     *
     * @param groupSize The size of the group. Must
     *                  be larger than 1, i.e groupSize &#8805; 2.
     * @param overflow if {@code true} then it will overflow and create
     *                 extra groups, else it will put the remainder in
     *                 the last group.
     * @return a list containing sets of integers which corresponds
     *         to groups.
     * @throws IllegalArgumentException iff groupSize &lt; 2.
     * @throws NoGroupAvailableException iff there is now way to create a group.
     * */
    default List<Set<Integer>> generateGroup(byte groupSize, boolean overflow)
            throws IllegalArgumentException, NoGroupAvailableException
    {
        if (groupSize < 2)
            throw new IllegalArgumentException(
                    "groupSize needs to greater or equal to 2, your value: %d < 2".formatted(groupSize)
            );

        return generateGroup((int) groupSize, overflow); // Cast to int to prevent infinite recursion.
    }

    /**
     * Generates subgroups of the provided
     * GroupManager, with a specified number
     * of groups. This will create evenly
     * size groups as long as possible.
     *
     * @param nbrGroups the amount of groups. Must be
     *                  larger than 1, i.e nbrGroups &#8805; 2.
     * @param overflow if {@code true} then it will overflow and create
     *                 extra groups, else it will put the remainder in
     *                 the last group.
     * @param gm The group manager that holds the main group.
     * @return a list containing sets of integers which corresponds
     *         to groups.
     * @throws IllegalArgumentException iff nbrGroups &lt; 2.
     * @throws NoGroupAvailableException iff there's no way to create a group.
     * */
    default List<Set<Integer>> generateGroup(short nbrGroups, boolean overflow, GroupManager gm)
            throws IllegalArgumentException, NoGroupAvailableException
    {
        if (nbrGroups < 2)
            throw new IllegalArgumentException(
                    "nbrGroups needs to greater or equal to 2, your value: %d < 2".formatted(nbrGroups)
            );

        return generateGroup(
                (int) Math.round((double) gm.getMemberCountOfRole(Person.Role.CANDIDATE) / (double) nbrGroups),
                overflow
        );
    }

    /**
     * Tests if there should be a new group or
     * if the group isn't full.
     *
     * @param i the current index the creator is on.
     * @param size the size of the group.
     * @return {@code true} iff it should create a new group<br>
     *         {@code false} otherwise.
     * */
    default boolean shouldCreateNewGroup(int i, int size)
    {
        return i % size == 0;
    }

    /**
     * Adds a group to the result in the correct way. This only works
     * for the same size group.
     *
     * @param result the list of all the finished groups.
     * @param current the current group.
     * @param candidates the candidates of the big group, that are unused.
     * @param overflow the overflow boolean, if true it will overflow else
     *                 it will create one extra group.
     * @param size the size of the groups.
     * @return the set that's supposed to be the current group.
     * */
    default Set<Integer> addGroup(
        List<Set<Integer>> result, Set<Integer> current,
        List<Person> candidates, boolean overflow, int size
    )
    {
        if (current == null && !overflow)
            return new HashSet<>();

        if (!overflow && candidates.size() >= size && current.size() != 0)
        {
            result.add(current);
            return new HashSet<>();
        }
        else if (overflow)
        {
            if (current != null)
                result.add(current);

            return new HashSet<>();
        }

        return current;
    }

    /**
     * Gets a random person from the list. It
     * will remove the person from the list.
     *
     * @param candidates the candidates that are unused.
     * @return the randomly chosen person.
     * */
    default Person getRandomPerson(List<Person> candidates)
    {
        return candidates.remove(new Random().nextInt(candidates.size()));
    }

    /**
     * Checks if a person is disallowed in the Group.
     *
     * @param deny the deny graph of the person.
     * @param current the currently worked on group.
     * @param id the id of the person being checked.
     * @return {@code true} iff the person is disallowed. <br>
     *         {@code false} otherwise.
     * */
    default boolean isPersonDisallowed(Set<Tuple> deny, Set<Integer> current, int id)
    {
        return Tuple.imageOfSet(deny, current).contains(id);
    }

    /**
     * Gets the wishes for a given id.
     *
     * @param wishes the set containing all the wishes.
     * @param id the id of the persons wishes.
     * @return a list containing the wishes.
     * */
    default List<Integer> getWishes(Set<Tuple> wishes, int id)
    {
        return new Vector<>(Tuple.imageOf(wishes, id));
    }

    /**
     * Finds a allowed person.
     *
     * @param deny the deny graph of the person.
     * @param current the current group in progress.
     * @param candidates the unused persons.
     * @param p the person that's being checked at the start.
     * @return the the first found allowed person.
     * @throws NoGroupAvailableException iff there are no person that's allowed.
     * */
    default Person getAllowedPerson(
        Set<Tuple> deny, Set<Integer> current,
        List<Person> candidates, Person p
    ) throws NoGroupAvailableException
    {
        int count = 0;
        while (isPersonDisallowed(deny, current, p.getId()))
        {
            if (++count == 1000)
                throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

            candidates.add(p);
            p = getRandomPerson(candidates);
        }

        return p;
    }

    /**
     * Generates subgroups of the provided GroupManager,
     * with specified group sizes. This will overflow
     * into the last group if there are too many entries
     * and it will stop early if there are to many entries.
     *
     * @param sizes the list of integers containing the sizes
     *              of the different groups.
     * @return a list containing sets of integers which corresponds
     *         to groups. The integers are the id's in their group
     *         manager.
     * @throws IllegalArgumentException iff sizes is empty or there is only one group.
     * @throws NoGroupAvailableException iff there's no way to create a group.
     * */
    List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException;
}
