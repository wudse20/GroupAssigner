package se.skorup.main.groups;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.ImmutableHashSet;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * An alternate GroupCreator for wishlist generation.
 * */
public class AlternateWishlistGroupCreator extends WishlistGroupCreator
{
    /**
     * Creates a new GroupCreator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
    public AlternateWishlistGroupCreator(GroupManager gm)
    {
        super(gm);
    }

    @Override
    public List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        var result = new ArrayList<Set<Integer>>();
        var random = new Random();
        var candidates = new ArrayList<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var wish = gm.getWishGraph();
        var deny = gm.getDenyGraph();
        var added = new HashSet<Integer>();

        int i = 0;
        Set<Integer> current = null; // Null to have it initialized, with a value.
        Person p = null; // Null to have it initialized, with a value.
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
                if (added.size() == 0)
                {
                    p = candidates.remove(random.nextInt(candidates.size()));
                }
                else
                {
                    var arr = ImmutableArray.fromList(candidates).map(x -> {
                        var wishes =
                            new ImmutableHashSet<>(Arrays.stream(x.getWishlist()).boxed().collect(Collectors.toSet()));
                        // first value: id, second value: nbrWishes
                        return new Tuple(x.getId(), wishes.diff(added).size());
                    }).sortBy(Comparator.comparingInt(Tuple::b)).map(Tuple::a)
                      .dropMatching(added.toArray(new Integer[0])).map(gm::getPersonFromId);

                    p = arr.get(0); // Gets person with fewest wishes left.
                    candidates.remove(p);
                }
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
                    var arr = ImmutableArray.fromList(wishes).map(x -> {
                        var d = new ImmutableHashSet<>(Tuple.imageOf(wish, x)).diff(added);
                        return new Tuple(x, d.size()); // a is id and b is the amount of wishes.
                    }).sortBy(Comparator.comparingInt(Tuple::b)).map(Tuple::a).dropMatching(added.toArray(new Integer[0]));

                    // Tries to get person with fewest wishes left
                    // and the goes throw the ascending order of
                    // wishes left.
                    for (var j : arr.toList())
                    {
                        p = gm().getPersonFromId(j);

                        if (!added.contains(j) && !Tuple.imageOfSet(deny, current).contains(j))
                            break;

                        p = null;
                    }

                    // Resets iff there are no free wishes.
                    if (arr.size() == 0)
                        p = null;

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

            added.add(p.getId());
            current.add(p.getId());
        }

        if (current != null)
            result.add(current);

        return result;
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar alternativ 2 (NYI)";
    }
}
