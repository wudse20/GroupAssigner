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
 * */
public record SubGroup(String name, List<Set<Integer>> groups, boolean isLeaderMode) implements Serializable {}
