package se.skorup.main.groups;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class used to generate random groups.
 *
 * @param gm the group manager in use.
 * */
public record RandomGroupCreator(GroupManager gm) implements GroupCreator
{
    @Override
    public List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        var deny = gm.getDenyGraph();
        var result = new ArrayList<Set<Integer>>();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));

        int i = 0;
        Set<Integer> current = null; // Just to have it initialized.
        while (candidates.size() != 0)
        {
            if (shouldCreateNewGroup(i++, size))
                current = addGroup(result, current, candidates, overflow, size);

            assert current != null; // To stop it from complaining.
            current.add(getAllowedPerson(deny, current, candidates, getRandomPerson(candidates)).getId());
        }

        if (current != null && !result.contains(current))
            result.add(current);

        return result;
    }

    @Override
    public List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (sizes == null)
            throw new IllegalArgumentException("Not enough groups 0");
        else if (sizes.size() == 0)
            throw new IllegalArgumentException("Not enough groups 0");


        var deny = gm.getDenyGraph();
        var result = new ArrayList<Set<Integer>>();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));

        int i = 0;
        int ii = 0;
        var current = new HashSet<Integer>();
        while (candidates.size() != 0)
        {
            current.add(getAllowedPerson(deny, current, candidates, getRandomPerson(candidates)).getId());

            if (i != sizes.size() && sizes.get(i) == ++ii)
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

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof RandomGroupCreator gc &&
               this.toString().equals(gc.toString());
    }
}
