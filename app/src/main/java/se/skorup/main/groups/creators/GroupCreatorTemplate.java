package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** The template for a GroupCreator. */
public abstract class GroupCreatorTemplate implements GroupCreator
{
    protected final GroupManager gm;

    /**
     * Creates a template group creator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
    public GroupCreatorTemplate(GroupManager gm)
    {
        this.gm = gm;
    }

    /**
     * Gets the next person.
     *
     * @param current the currently worked on group.
     * @param candidates the unused candidates.
     * @param wish the wishes of this group.
     * @param deny the denylist of this group.
     * @param added the persons that are already used.
     * @param p the current person.
     * @throws NoGroupAvailableException iff there are no possible groups.
     * */
    protected abstract Person getPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException;

    @Override
    public final List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        var result = new ArrayList<Set<Integer>>();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var wish = gm.getWishGraph();
        var deny = gm.getDenyGraph();
        var added = new HashSet<Integer>();

        int i = 0;
        Set<Integer> current = null; // Just to have it initialized.
        Person p = null; // Just to have it initialized.
        while (candidates.size() != 0)
        {
            if (shouldCreateNewGroup(i++, size))
                current = addGroup(result, current, candidates, overflow, size);

            assert current != null; // To stop it from complaining.
            p = getPerson(current, candidates, wish, deny, added, p);
            current.add(p.getId());
            added.add(p.getId());
        }

        if (current != null)
            result.add(current);

        return result;
    }

    @Override
    public final List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (sizes == null)
            throw new IllegalArgumentException("Not enough groups 0");
        else if (sizes.size() < 2)
            throw new IllegalArgumentException(
                    "Not enough groups %d".formatted(Objects.requireNonNullElse(sizes.size(), 0))
            );

        var result = new ArrayList<Set<Integer>>();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var wish = gm.getWishGraph();
        var deny = gm.getDenyGraph();
        var added = new HashSet<Integer>();

        int i = 0;
        int ii = 0;
        Set<Integer> current = new HashSet<>();
        Person p = null; // Just to have it initialized.
        while (candidates.size() != 0)
        {
            p = getPerson(current, candidates, wish, deny, added, p);
            current.add(p.getId());
            added.add(p.getId());

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
}
