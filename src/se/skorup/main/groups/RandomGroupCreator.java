package se.skorup.main.groups;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class used to generate random groups.
 * 
 * TODO: FIX GROUP SIZE!
 * */
public class RandomGroupCreator implements GroupCreator
{
    /** The group manager in use. */
    private final GroupManager gm;

    /**
     * Creates a new RandomGroup
     * creator.
     *
     * @param gm the group manager in use.
     * */
    public RandomGroupCreator(GroupManager gm)
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
        var deny = gm.getDenyGraph();
        var result = new ArrayList<Set<Integer>>();
        var random = new Random();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));

        int i = 0;
        Set<Integer> current = null; // Just to have it initialized.
        while (candidates.size() != 0)
        {
            if (i++ % size == 0)
            {
                if (current == null && !overflow)
                    current = new HashSet<>();

                if (!overflow && candidates.size() >= size && current.size() != 0)
                {
                    result.add(current);
                    current = new HashSet<>();
                }
                else if (overflow)
                {
                    if (current != null)
                        result.add(current);

                    current = new HashSet<>();
                }
            }

            var p = candidates.remove(random.nextInt(candidates.size()));

            int count = 0;
            while (Tuple.imageOfSet(deny, current).contains(p.getId()))
            {
                if (++count == 1000)
                    throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                candidates.add(p);
                p = candidates.remove(random.nextInt(candidates.size()));
            }

            current.add(p.getId());
        }

        if (current != null && !result.contains(current))
            result.add(current);

        return result;
    }

    @Override
    public List<Set<Integer>> generateGroup(byte groupSize, boolean overflow) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (groupSize < 2)
            throw new IllegalArgumentException(
                "groupSize needs to greater or equal to 2, your value: %d < 2".formatted(groupSize)
            );

        return generateGroup((int) groupSize, overflow); // Cast to int to prevent infinite recursion.
    }

    @Override
    public List<Set<Integer>> generateGroup(short nbrGroups, boolean overflow) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (nbrGroups < 2)
            throw new IllegalArgumentException(
                "nbrGroups needs to greater or equal to 2, your value: %d < 2".formatted(nbrGroups)
            );

        return generateGroup(
            (int) Math.ceil((double) gm.getMemberCountOfRole(Person.Role.CANDIDATE) / (double) nbrGroups),
            overflow
        );
    }

    @Override
    public List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (sizes == null)
            throw new IllegalArgumentException("Not enough groups 0");
        else if (sizes.size() == 0)
            throw new IllegalArgumentException(
                "Not enough groups %d".formatted(Objects.requireNonNullElse(sizes.size(), 0))
            );


        var deny = gm.getDenyGraph();
        var result = new ArrayList<Set<Integer>>();
        var random = new Random();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));

        int i = 0;
        int ii = 0;
        var current = new HashSet<Integer>();
        while (candidates.size() != 0)
        {
            var p = candidates.remove(random.nextInt(candidates.size()));

            int count = 0;
            while (Tuple.imageOfSet(deny, current).contains(p.getId()))
            {
                if (++count == 1000)
                    throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                candidates.add(p);
                p = candidates.remove(random.nextInt(candidates.size()));
            }

            current.add(p.getId());

            if (sizes.get(i) == ++ii)
            {
                ii = 0;
                i++;

                result.add(current);
                current = new HashSet<>();
            }
        }

        if (current.size() != 0 && !result.contains(current))
            result.add(current);

        return result;
    }

    @Override
    public String toString()
    {
        return "Slumpmässig grupp";
    }
}
