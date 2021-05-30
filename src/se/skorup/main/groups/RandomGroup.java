package se.skorup.main.groups;

import se.skorup.main.manager.GroupManager;

import java.util.List;
import java.util.Set;

/**
 * The class used to generate random groups.
 * */
public class RandomGroup implements GroupCreator
{
    /** The group manager in use. */
    private final GroupManager gm;

    /**
     * Creates a new RandomGroup
     * creator.
     * */
    public RandomGroup(GroupManager gm)
    {
        this.gm = gm;
    }

    @Override
    public List<Set<Integer>> generateGroup(byte groupSize) throws IllegalArgumentException
    {
        return null;
    }

    @Override
    public List<Set<Integer>> generateGroup(short nbrGroups) throws IllegalArgumentException
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "Slumpmässig grupp";
    }
}
