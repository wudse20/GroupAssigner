package se.skorup.main.objects;

import java.io.Serializable;
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
 * */
public record Subgroups(
        String name, List<Set<Integer>> groups,
        boolean isLeaderMode, boolean isWishListMode,
        List<String> labels) implements Serializable
{
    /**
     * Changes the name of the Subgroups.
     *
     * @param name the new name of the Subgroups.
     * @return a new subgroups object with a new name.
     * */
    public Subgroups changeName(String name)
    {
        return new Subgroups(name, groups, isLeaderMode, isWishListMode, labels);
    }
}
