package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @param gm the instance of the GroupManger resonsible for the group.
     * @param left the id's of the persons that are left.
     * @param current the current group being worked on.
     * @throws NoGroupAvailableException iff there is no possible person to be chosen.
     * @return the id of the person that's the next person.
     */
    protected abstract int getNextPerson(
        GroupManager gm, Set<Integer> left, Set<Integer> current
    ) throws NoGroupAvailableException;

    /**
     * Checks if a person is allowed in the group.
     *
     * @param id the id of the person to tested.
     * @param current the current group in creation.
     * @param gm the group manager in charge of the group.
     * @return {@code true} iff the person is allowed to exist in current.
     * */
    protected boolean isPersonAllowed(int id, Collection<Integer> current, GroupManager gm)
    {
        return current.stream().noneMatch(id2 -> Tuple.imageOf(gm.getDenyGraph(), id2).contains(id));
    }

    @Override
    public List<List<Set<Integer>>> generate(
        GroupManager gm, int size, boolean overflow
    ) throws NoGroupAvailableException, IllegalArgumentException
    {
        var res = new ArrayList<Set<Integer>>();
        var count = gm.getMemberCountOfRole(Person.Role.CANDIDATE);
        var left =
            gm.getAllOfRoll(Person.Role.CANDIDATE)
              .stream()
              .map(Person::getId)
              .collect(Collectors.toCollection(HashSet::new));

        var currentCount = 0;
        var current = new HashSet<Integer>();

        for (var i = 0; i < count; i++)
        {
            if (currentCount == size)
            {
                currentCount = 0;
                res.add(current);
                current = new HashSet<>();
            }

            current.add(getNextPerson(gm, left, current));
            currentCount++;
        }

        if (currentCount != 0)
            res.add(current);

        return Collections.singletonList(res);
    }

    @Override
    public List<List<Set<Integer>>> generate(GroupManager gm, List<Integer> sizes) throws NoGroupAvailableException
    {
        var res = new ArrayList<Set<Integer>>();
        var count = gm.getMemberCountOfRole(Person.Role.CANDIDATE);
        var left =
                gm.getAllOfRoll(Person.Role.CANDIDATE)
                        .stream()
                        .map(Person::getId)
                        .collect(Collectors.toCollection(HashSet::new));

        var currentCount = 0;
        var sizePointer = 0;
        var current = new HashSet<Integer>();

        for (var i = 0; i < count; i++)
        {
            if (sizePointer < sizes.size() && currentCount == sizes.get(sizePointer))
            {
                currentCount = 0;
                sizePointer++;
                res.add(current);
                current = new HashSet<>();
            }

            current.add(getNextPerson(gm, left, current));
            currentCount++;
        }

        if (currentCount != 0)
            res.add(current);

        return Collections.singletonList(res);
    }
}
