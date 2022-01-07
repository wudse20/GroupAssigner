package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An alternate GroupCreator for wishlist generation.
 * */
public final class AlternateWishlistGroupCreator extends WishlistGroupCreator
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
    protected Person getPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException
    {
        if (current.size() == 0)
        {
            if (added.size() == 0)
            {
                p = getRandomPerson(candidates);
            }
            else
            {
                var arr = ImmutableArray.fromList(candidates).map(x -> {
                    var wishes =
                        new ImmutableHashSet<>(Arrays.stream(x.getWishlist()).boxed().collect(Collectors.toSet()));
                    return new Tuple(x.getId(), wishes.diff(added).size()); // first value: id, second value: nbrWishes
                }).sortBy(Comparator.comparingInt(Tuple::b)).map(Tuple::a)
                  .dropMatching(added.toArray(new Integer[0])).map(gm::getPersonFromId);

                p = arr.get(0); // Gets person with fewest wishes left.
                candidates.remove(p);
            }
        }
        else
        {
            var wishes = getWishes(wish, p.getId());

            if (wishes.isEmpty())
            {
                p = getAllowedPerson(deny, current, candidates, p);
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

                    if (!added.contains(j) && !isPersonDisallowed(deny, current, p.getId()))
                        break;

                    p = null;
                }

                // Resets iff there are no free wishes.
                if (arr.size() == 0)
                    p = null;

                if (p == null)
                {
                    // No wishes; that aren't blocked so resorts to random group generation.
                    p = getAllowedPerson(deny, current, candidates, getRandomPerson(candidates));
                }
                else
                {
                    candidates.remove(p);
                }
            }
        }

        return p;
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar alternativ 2";
    }
}
