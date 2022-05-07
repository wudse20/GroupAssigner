package se.skorup.main.objects;

import se.skorup.main.groups.creators.WishlistGroupCreator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The record for the subgroup. The iterable will be the same as
 * {@code instance.groups().iterable()}.
 *
 * @param name the name of the group.
 * @param groups the generated groups.
 * @param isLeaderMode if {@code true} the group, was paired with the leaders.
 * @param isWishListMode if {@code true} the groups where generated using this
 *                       {@link WishlistGroupCreator GroupCreator}.
 * @param labels the labels of the groups.
 * @param leaders the leaders of the group.
 * */
public record Subgroups(
        String name, List<Set<Integer>> groups,
        boolean isLeaderMode, boolean isWishListMode,
        String[] labels, List<Person> leaders
) implements Serializable, Iterable<Set<Integer>>
{
    @Serial
    private static final long serialVersionUID = 3812458839615746121L;

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

    /**
     * Sets the label of a given index.
     *
     * @param index the group index.
     * @param label the new label of the group.
     * */
    public void setLabel(int index, String label)
    {
        labels[index] = label;
    }

    /**
     * Gets the number of groups available
     * in this subgroups instance.
     *
     * @return the number of subgroups.
     * */
    public int getGroupCount()
    {
        return groups.size();
    }

    @Override
    public String toString()
    {
        return "Subgroups[name=%s, groups=%s, isLeaderMode=%b, isWishListMode=%b, labels=%s, leaders=%s]"
                .formatted(name, groups, isLeaderMode, isWishListMode, Arrays.toString(labels), leaders);
    }

    @Override
    public Iterator<Set<Integer>> iterator()
    {
        return groups.iterator();
    }
}
