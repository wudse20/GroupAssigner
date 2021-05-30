package se.skorup.main.groups;

import java.util.List;
import java.util.Set;

/**
 * The interface holding the methods
 * for creating sub groups in the program.
 * */
public interface GroupCreator
{
    /**
     * Generates subgroups of the provided
     * GroupManager, with a specified group size.
     * If it cannot evenly divide the groups into
     * groups of size groupSize, it will create
     * the last group smaller than the group size.
     *
     * @param groupSize The size of the group. Must
     *                  be larger than 1, i.e groupSize &#8805; 2.
     * @return a list containing sets of integers which corresponds
     *         to groups.
     * @throws IllegalArgumentException iff groupSize &lt; 2.
     * */
    List<Set<Integer>> generateGroup(byte groupSize) throws IllegalArgumentException;

    /**
     * Generates subgroups of the provided
     * GroupManager, with a specified number
     * of groups. This will create evenly
     * size groups as long as possible.
     *
     * @param nbrGroups the amount of groups. Must be
     *                  larger than 1, i.e nbrGroups &#8805; 2.
     * @return a list containing sets of integers which corresponds
     *         to groups.
     * @throws IllegalArgumentException iff nbrGroups &lt; 2.
     * */
    List<Set<Integer>> generateGroup(short nbrGroups) throws IllegalArgumentException;
}
