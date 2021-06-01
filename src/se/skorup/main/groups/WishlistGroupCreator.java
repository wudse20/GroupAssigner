package se.skorup.main.groups;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;

import java.util.List;
import java.util.Set;

/**
 * The group creator used to create groups
 * based on the wishes of the candidates.
 * */
public class WishlistGroupCreator implements GroupCreator
{
    private final GroupManager gm;

    /**
     * Creates a new wishlist group creator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     * */
    public WishlistGroupCreator(GroupManager gm)
    {
        this.gm = gm;
    }

    @Override
    public List<Set<Integer>> generateGroup(byte groupSize) throws IllegalArgumentException, NoGroupAvailableException
    {
        return null;
    }

    @Override
    public List<Set<Integer>> generateGroup(short nbrGroups) throws IllegalArgumentException, NoGroupAvailableException
    {
        return null;
    }
}
