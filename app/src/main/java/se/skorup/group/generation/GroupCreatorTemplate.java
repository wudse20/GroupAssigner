package se.skorup.group.generation;

import se.skorup.group.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A template class for a group creator.
 * */
public abstract class GroupCreatorTemplate implements GroupCreator
{
    /** No one should instantiate this class anomalously. */
    protected GroupCreatorTemplate() {}

    /**
     * Gets the next person.
     *
     * @param gm the instance of the GroupManger responsible for the group.
     * @param left the id's of the persons that are left.
     * @param current the current group being worked on.
     * @param lastId the id that was the last chosen.
     * @return the id of the person that's the next person.
     * @throws GroupCreationFailedException iff there is no possible person to be chosen.
     * */
    protected abstract int getNextPerson(
        Group gm, Set<Integer> left, Set<Integer> current, int lastId
    ) throws GroupCreationFailedException;

    /**
     * Checks if a person is allowed in the group.
     *
     * @param id the id of the person to tested.
     * @param current the current group in creation.
     * @param gm the group manager in charge of the group.
     * @return {@code true} iff the person is allowed to exist in current.
     * */
    protected boolean isPersonAllowed(int id, Collection<Integer> current, Group gm)
    {
        for (var p : current)
        {
            if (gm.isDenied(id, p))
                return false;
        }

        return true;
    }

    @Override
    public List<List<Set<Integer>>> generate(
        Group gm, int size, boolean overflow
    ) throws GroupCreationFailedException, IllegalArgumentException
    {
        var res = new ArrayList<Set<Integer>>();
        var count = gm.size();
        var left = gm.getIds();

        var currentCount = 0;
        var addedCount = 0;
        var last = -1;
        var current = new HashSet<Integer>();

        for (var i = 0; i < count; i++)
        {
            if (Thread.interrupted())
                return List.of();

            if (currentCount == size)
            {
                currentCount = 0;
                res.add(current);

                if (current.size() != size)
                    throw new GroupCreationFailedException("Wrong size of group; Please report!");

                addedCount += current.size();
                if (addedCount + left.size() != count)
                    throw new GroupCreationFailedException("Please Report: One or more persons are used more than once!");

                current = new HashSet<>();
            }

            current.add(last = getNextPerson(gm, (Set<Integer>) left, current, last));
            currentCount++;

            if (currentCount != current.size())
                throw new GroupCreationFailedException("Wrong size of group; Please report!");
        }

        if (currentCount != 0)
            res.add(current);

        return Collections.singletonList(res);
    }

    @Override
    public List<List<Set<Integer>>> generate(Group gm, List<Integer> sizes) throws GroupCreationFailedException
    {
        var res = new ArrayList<Set<Integer>>();
        var count = gm.size();
        var left = gm.getIds();
        var currentCount = 0;
        var sizePointer = 0;
        var last = -1;
        var current = new HashSet<Integer>();

        for (var i = 0; i < count; i++)
        {
            if (Thread.interrupted())
                return List.of();

            if (sizePointer < sizes.size() && currentCount == sizes.get(sizePointer))
            {
                if (sizes.get(sizePointer) != current.size())
                    throw new GroupCreationFailedException("Wrong group size; please report!");

                currentCount = 0;
                sizePointer++;
                res.add(current);
                current = new HashSet<>();
            }

            current.add(last = getNextPerson(gm, (Set<Integer>) left, current, last));
            currentCount++;

            if (currentCount != current.size())
                throw new GroupCreationFailedException("Wrong size of group; Please report!");
        }

        if (currentCount != 0)
            res.add(current);

        return Collections.singletonList(res);
    }
}
