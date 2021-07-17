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
import java.util.Vector;

/**
 * The group creator used to create groups
 * based on the wishes of the candidates.
 *  */
public class WishlistGroupCreator implements GroupCreator
{
    private final GroupManager gm;

    /**
     * Creates a wishlist group creator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
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
        Person p = null; // Just to have it initialized.
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

            if (current.size() == 0)
            {
                p = candidates.remove(random.nextInt(candidates.size()));
            }
            else
            {
                var wishes = new Vector<>(Tuple.imageOf(wish, p.getId()));

                if (wishes.isEmpty())
                {
                    p = candidates.remove(random.nextInt(candidates.size())); // No wishes grab random person.

                    int count = 0;
                    while (Tuple.imageOfSet(deny, current).contains(p.getId()))
                    {
                        if (++count == 1000)
                            throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                        candidates.add(p);
                        p = candidates.remove(random.nextInt(candidates.size()));
                    }
                }
                else
                {
                    for (int j : wishes)
                    {
                        p = gm.getPersonFromId(j);

                        if (!added.contains(j) && !Tuple.imageOfSet(deny, current).contains(j))
                            break;

                        p = null;
                    }

                    if (p == null)
                    {
                        // No wishes; that aren't blocked so resorts to random group
                        // generation.
                        p = candidates.remove(random.nextInt(candidates.size()));

                        int count = 0;
                        while (Tuple.imageOfSet(deny, current).contains(p.getId()))
                        {
                            if (++count == 1000)
                                throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                            candidates.add(p);
                            p = candidates.remove(random.nextInt(candidates.size()));
                        }
                    }
                    else
                    {
                        candidates.remove(p);
                    }
                }
            }

            current.add(p.getId());
            added.add(p.getId());
        }

        if (current != null)
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

        return generateGroup(gm.getMemberCountOfRole(Person.Role.CANDIDATE) / nbrGroups, overflow);
    }

    @Override
    public List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        if (sizes == null)
            throw new IllegalArgumentException("Not enough groups 0");
        else if (sizes.size() < 2)
            throw new IllegalArgumentException(
                    "Not enough groups %d".formatted(Objects.requireNonNullElse(sizes.size(), 0))
            );


        var result = new ArrayList<Set<Integer>>();
        var random = new Random();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var wish = gm.getWishGraph();
        var deny = gm.getDenyGraph();
        var added = new HashSet<Integer>();

        int i = 0;
        int ii = 0;
        Set<Integer> current = new HashSet<>();
        Person p = null; // Just to have it initialized.
        while (candidates.size() != 0) {
            if (current.size() == 0) {
                p = candidates.remove(random.nextInt(candidates.size()));
            } else {
                var wishes = new Vector<>(Tuple.imageOf(wish, p.getId()));

                if (wishes.isEmpty()) {
                    p = candidates.remove(random.nextInt(candidates.size())); // No wishes grab random person.

                    int count = 0;
                    while (Tuple.imageOfSet(deny, current).contains(p.getId())) {
                        if (++count == 1000)
                            throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                        candidates.add(p);
                        p = candidates.remove(random.nextInt(candidates.size()));
                    }
                } else {
                    for (int j : wishes) {
                        p = gm.getPersonFromId(j);

                        if (!added.contains(j) && !Tuple.imageOfSet(deny, current).contains(j))
                            break;

                        p = null;
                    }

                    if (p == null) {
                        // No wishes; that aren't blocked so resorts to random group
                        // generation.
                        p = candidates.remove(random.nextInt(candidates.size()));

                        int count = 0;
                        while (Tuple.imageOfSet(deny, current).contains(p.getId())) {
                            if (++count == 1000)
                                throw new NoGroupAvailableException("Cannot create a group, to many denylist items.");

                            candidates.add(p);
                            p = candidates.remove(random.nextInt(candidates.size()));
                        }
                    } else {
                        candidates.remove(p);
                    }
                }
            }

            current.add(p.getId());
            added.add(p.getId());

            if (sizes.get(i) == ++ii) {
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
        return "Skapa grupper efter önskelista";
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof WishlistGroupCreator gc &&
                this.toString().equals(gc.toString());
    }

    public GroupManager gm()
    {
        return gm;
    }

}
