package se.skorup.main.groups;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The group creator used to create groups
 * based on the wishes of the candidates.
 * */
public class WishlistGroupCreator implements GroupCreator
{
    /** The group manager used to create the groups. */
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

    /**
     * Generates the different, groups of size
     * size.
     *
     * @param size the size of the group.
     * @param overflow if the groups should overflow or not.
     * @return a List containing the generated groups.
     * @throws NoGroupAvailableException iff there's no way to create a group.
     * */
    private List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        var result = new ArrayList<Set<Integer>>();
        var random = new Random();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var wish = gm.getWishGraph();
        var deny = gm.getDenyGraph();
        var added = new HashSet<Integer>();

        int i = 0;
        Set<Integer> current = null; // Just to have it initialized.
        while (candidates.size() != 0)
        {
            if (i++ % size == 0)
            {
                if (current != null)
                    result.add(current);

                if (!overflow && candidates.size() >= size)
                    current = new HashSet<>();
                else if (overflow)
                    current = new HashSet<>();
            }

//            var p = candidates.remove(random.nextInt(candidates.size()));
//
//            int count = 0;
//            while (Tuple.imageOfSet(deny, current).contains(p.getId()))
//            {
//                if (++count == 1000)
//                    throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");
//
//                candidates.add(p);
//                p = candidates.remove(random.nextInt(candidates.size()));
//            }
        }

        return result;
    }

    @Override
    public List<Set<Integer>> generateGroup(byte groupSize, boolean overflow) throws IllegalArgumentException, NoGroupAvailableException
    {
        return null;
    }

    @Override
    public List<Set<Integer>> generateGroup(short nbrGroups, boolean overflow) throws IllegalArgumentException, NoGroupAvailableException
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskelista";
    }
}
