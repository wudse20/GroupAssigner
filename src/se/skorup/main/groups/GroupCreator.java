package se.skorup.main.groups;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.util.List;
import java.util.Set;

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
