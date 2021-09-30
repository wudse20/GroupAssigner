package se.skorup.main.objects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The record for the subgroup.
 *
 * @param name the name of the group.
 * @param groups the generated groups.
 * @param isLeaderMode if {@code true} the group, was paired with the leaders.
 * @param isWishListMode if {@code true} the groups where generated using this
 *                       {@link se.skorup.main.groups.WishlistGroupCreator GroupCreator}.
 * @param labels the labels of the groups.
 * @param leaders the leaders of the group.
 * */
public record Subgroups(
        String name, List<Set<Integer>> groups,
        boolean isLeaderMode, boolean isWishListMode,
        String[] labels, List<Person> leaders
) implements Serializable
{
    /**
     * Changes the name of the Subgroups.
     *
     * @param name the new name of the Subgroups.
     * @return a new subgroups object with a new name.
     * */
    public Subgroups changeName(String name)
    {
        return new Subgroups(name, groups, isLeaderMode, isWishListMode, labels, leaders);
    }

    /**
     * Gets the label of the specific index for the
     * group. If there exists no label, then it will
     * create one.
     *
     * @param index the index of the group in the list.
     * @return the label of the group.
     * */
    public String getLabel(int index)
    {
        if (labels[index] == null)
            labels[index] = isLeaderMode ?
            leaders.get(index).getName() :
            "Grupp %d".formatted(index + 1);

        return labels[index];
    }

    @Override
    public String toString()
    {
        return "Subgroups[name=%s, groups=%s, isLeaderMode=%b, isWishListMode=%b, labels=%s, leaders=%s]"
                .formatted(name, groups, isLeaderMode, isWishListMode, Arrays.toString(labels), leaders);
    }
}
