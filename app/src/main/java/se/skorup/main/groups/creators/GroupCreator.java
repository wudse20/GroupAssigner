package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;

import java.util.List;
import java.util.Set;

/**
 * The common subtype for all GroupCreators.
 * */
public interface GroupCreator
{
    /**
     * Generates subgroups of size: size. It could generate
     * multiple groups if the implementing algorithm is designed for that.
     * If the algorithm is designed for multiple list the resulting list
     * could contain more than one element. If it isn't designed for that
     * then it will contain one element.
     *
     * @param gm the GroupManger responsible for the group to create subgroups in.
     * @param size the size of the subgroups.
     * @param overflow If the sizes do not match then it will overflow and add the remaining
     *                 people to the last group and overflow is set to {@code true}, else it
     *                 will create one extra group.
     * @throws NoGroupAvailableException iff there is no way to create a group.
     * @throws IllegalArgumentException iff size < 2.
     * @return A list of generated subgroups.
     * */
    List<List<Set<Integer>>> generate(
        GroupManager gm, int size, boolean overflow
    ) throws NoGroupAvailableException, IllegalArgumentException;

    /**
     * Generates subgroups of sizes matching the list of sizes provided
     * to the method. If they do not match then it will create one extra group.
     * It could generate multiple groups if the implementing algorithm is designed
     * for that. If the algorithm is designed for multiple list the resulting list
     * could contain more than one element. If it isn't designed for that then
     * it will contain one element.
     *
     * @param gm the GroupManger responsible for the group to create subgroups in.
     * @param sizes the sizes of the subgroups.
     * @throws NoGroupAvailableException iff there is no way to create a group.
     * @throws IllegalArgumentException iff size < 2.
     * @return A list of generated subgroups.
     * */
    List<List<Set<Integer>>> generate(GroupManager gm, List<Integer> sizes) throws NoGroupAvailableException;

    /**
     * Interrupts the group creation. This is optional to implement
     * and by default it does nothing.
     * */
    default void interrupt() {}
}
