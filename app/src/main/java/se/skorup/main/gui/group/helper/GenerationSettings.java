package se.skorup.main.gui.group.helper;

import java.util.List;

/**
 * The settings that are supposed to be used when generating groups.
 *
 * @param useMainGroups If main groups should be used or not.
 * @param sizes the sizes of the different groups.
 * @param mg1Sizes the sizes associated with main group 1.
 *                 This is unused if <i>useMainGroups</i> is set to {@code false}.
 * @param mg2Sizes the sizes associated with main group 2.
 *                 This is unused if <i>useMainGroups</i> is set to {@code false}.
 * */
public record GenerationSettings(
    boolean useMainGroups, List<Integer> sizes,
    List<Integer> mg1Sizes, List<Integer> mg2Sizes
) {}
