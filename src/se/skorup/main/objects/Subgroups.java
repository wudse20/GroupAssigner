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
 * */
public record Subgroups(String name, List<Set<Integer>> groups, boolean isLeaderMode, boolean isWishListMode)
        implements Serializable {}
