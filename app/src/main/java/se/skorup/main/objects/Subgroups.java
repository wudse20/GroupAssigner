package se.skorup.main.objects;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.groups.creators.WishlistGroupCreator;
import se.skorup.main.manager.GroupManager;

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
            labels[index] = isLeaderMode && index < leaders.size() ?
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
     * Adds a person to a subgroup.
     *
     * @param id the id of the person.
     * @param groupIndex the index of the group.
     * */
    public void addPersonToGroup(int id, int groupIndex)
    {
        groups.get(groupIndex).add(id);
    }

    /**
     * Removes a person from a subgroup.
     *
     * @param id the id of the person.
     * @param groupIndex the index of the group.
     * */
    public void removePersonFromGroup(int id, int groupIndex)
    {
        groups.get(groupIndex).remove(id);
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

    /**
     * Figures out the number of wishes for a person in
     * its subgroup.
     *
     * @param id the id of the person.
     * @param gm the group manager in use.
     * */
    public int getNumberOfWishes(int id, GroupManager gm)
    {
        var wishes = new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), id));

        ImmutableHashSet<Integer> group = null;
        for (var g : groups)
        {
            if (g.contains(id))
            {
                group = new ImmutableHashSet<>(g);
                break;
            }
        }

        assert group != null;
        return wishes.intersection(group).size();
    }

    /**
     * Used only for unit tests.
     *
     * @return the serial version UUID of this class.
     * */
    long getSerialVersionUID()
    {
        return serialVersionUID;
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
