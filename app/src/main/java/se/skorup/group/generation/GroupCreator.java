package se.skorup.group.generation;

import se.skorup.group.Group;

import java.util.List;
import java.util.Set;

/**
 * The base type for all generators.
 * */
public interface GroupCreator
{
    /**
     * Generates subgroups of size: size. It could generate
     * multiple groups if the implementing algorithm is designed for that.
     * If the algorithm is designed for multiple subgroups, the resulting list
     * could contain more than one element. If it isn't designed for that,
     * then it will contain one element.
     *
     * @param gm the GroupManger responsible for the group to create subgroups in.
     * @param size the size of the subgroups.
     * @param overflow If the sizes do not match then it will overflow and add the remaining
     *                 people to the last group and overflow is set to {@code true}, else it
     *                 will create one extra group.
     * @throws GroupCreationFailedException iff there is no way to create a group.
     * @throws IllegalArgumentException iff size &lt; 2.
     * @return A list of generated subgroups.
     * */
    List<List<Set<Integer>>> generate(
        Group gm, int size, boolean overflow
    ) throws GroupCreationFailedException, IllegalArgumentException;

    /**
     * Generates subgroups of size: size. It could generate
     * multiple groups if the implementing algorithm is designed for that.
     * If the algorithm is designed for multiple subgroups, the resulting list
     * could contain more than one element. If it isn't designed for that,
     * then it will contain one element.
     *
     * @param gm the GroupManger responsible for the group to create subgroups in.
     * @param sizes the sizes of the subgroups.
     * @throws GroupCreationFailedException iff there is no way to create a group.
     * @throws IllegalArgumentException iff size &lt; 2.
     * @return A list of generated subgroups.
     * */
    List<List<Set<Integer>>> generate(Group gm, List<Integer> sizes) throws GroupCreationFailedException;

    /**
     * Interrupts the group creation. This is optional to implement,
     * and by default it does nothing.
     * */
    default void interrupt() {}
}
