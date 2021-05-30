package se.skorup.main.objects;

import java.util.HashSet;
import java.util.Set;

/**
 * A tuple of two integers..
 * */
public record Tuple(int a, int b)
{
    /**
     * Inverts a binary relation, i.e.
     * swapping a and b.
     *
     * @return a new binary relation where
     *         a and b has swapped places.
     * */
    public Tuple invert()
    {
        return new Tuple(b, a);
    }

    /**
     * Creates the image of a relation under
     * a value.
     *
     * @param relation the relation that is being
     *                 calculated on.
     * @param number the value the relation's image
     *               is calculated under.
     * @return a set containing the image of the relation.
     * */
    public static Set<Integer> imageOf(Set<Tuple> relation, int number)
    {
        var set = new HashSet<Integer>();

        for (var t : relation)
            if (t.a == number)
                set.add(t.b);

        return set;
    }

    /**
     * Creates the image of a relation under
     * a set.
     *
     * @param relation the relation that is being
     *                 calculated on.
     * @param numbers the set the relation's image
     *                is calculated under.
     * */
    public static Set<Integer> imageOfSet(Set<Tuple> relation, Set<Integer> numbers)
    {
        var set = new HashSet<Integer>();

        for (var n : numbers)
            set.addAll(imageOf(relation, n));

        return set;
    }
}
