package se.skorup.util.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A immutable wrapper class for the HashSet.
 *
 * @param <E> The type of the elements.
 * */
public class ImmutableHashSet<E> implements Iterable<E>
{
    /** The set in the background. */
    private final Set<E> set;

    /**
     * Creates a new ImmutableHashSet from
     * an array.
     *
     * @param elems the elementes to be added.
     * */
    @SafeVarargs
    public ImmutableHashSet(E... elems)
    {
        this.set = new HashSet<>(Arrays.asList(elems));
    }

    /**
     * Creates a new ImmutableHashSet from a
     * set.
     *
     * @param set the set to become immutable.
     * */
    public ImmutableHashSet(Set<E> set)
    {
        this.set = new HashSet<>(set);
    }

    /**
     * Creates a new ImmutableHashSet from a
     * Collection.
     *
     * @param col the col to become an ImmutableHashSet.
     * */
    public ImmutableHashSet(Collection<E> col)
    {
        this.set = new HashSet<>(col);
    }

    /**
     * Creates a new ImmutableHashSet from an
     * ImmutableCollection.
     *
     * @param col the col to become an ImmutableHashSet.
     * */
    public ImmutableHashSet(ImmutableHashSet<E> col)
    {
        this.set = new HashSet<>(col.toList());
    }

    /**
     * The intersection between this set and the
     * other set.
     *
     * @param other the other set. If {@code null} then it throws
     *              NullPointerException.
     * @return the elements both in the other set and this set.
     * @throws NullPointerException iff other is {@code null}.
     * */
    public ImmutableHashSet<E> intersection(Set<E> other) throws NullPointerException
    {
        if (other == null)
            throw new NullPointerException("Other is null");

        var res = new HashSet<E>();

        if (size() > other.size())
        {
            for (var e : this)
                if (other.contains(e))
                    res.add(e);
        }
        else
        {
            for (var e : other)
                if (contains(e))
                    res.add(e);
        }

        return new ImmutableHashSet<>(res);
    }

    /**
     * The intersection between this set and the
     * other set.
     *
     * @param other the other set. If {@code null} then it throws
     *              NullPointerException.
     * @return A set containing the elements both in the other set and this set.
     * @throws NullPointerException iff other is {@code null}.
     * */
    public ImmutableHashSet<E> intersection(ImmutableHashSet<E> other) throws NullPointerException
    {
        if (other == null)
            throw new NullPointerException("Other is null");

        return intersection(other.set);
    }

    /**
     * The union between this set and the other set.
     *
     * @param other the other set. If {@code null} then it throws
     *              NullPointerException.
     * @return A set containing all the elements of both sets.
     * @throws NullPointerException iff other is {@code null}.
     * */
    public ImmutableHashSet<E> union(Set<E> other) throws NullPointerException
    {
        if (other == null)
            throw new NullPointerException("Other is null");

        var res = new HashSet<>((other.size() > size()) ? other : set);
        res.addAll((other.size() > size()) ? set : other);
        return new ImmutableHashSet<>(res);
    }

    /**
     * The union between this set and the other set.
     *
     * @param other the other set. If {@code null} then it throws
     *              NullPointerException.
     * @return A set containing all the elements of both sets.
     * @throws NullPointerException iff other is {@code null}.
     * */
    public ImmutableHashSet<E> union(ImmutableHashSet<E> other) throws NullPointerException
    {
        if (other == null)
            throw new NullPointerException("Other is null");

        return union(other.set);
    }

    /**
     * Computes the difference of two sets.<br><br>
     *
     * A diff B = A &#8745; B<sup>c</sup>, i.e.
     * all elements that are in A, but not in B. <br><br>
     *
     * The set A is this instance and B is the
     * param other.
     *
     * @param other the set B.
     * @return A set containing all elements in A, but
     *         not in B.
     * @throws IllegalArgumentException iff other is null.
     * */
    public ImmutableHashSet<E> diff(Set<E> other) throws IllegalArgumentException
    {
        if (other == null)
            throw new IllegalArgumentException("Other cannot be null");

        var res = new HashSet<E>();

        for (var e : this)
            if (!other.contains(e))
                res.add(e);

        return new ImmutableHashSet<E>(res);
    }

    /**
     * Computes the difference of two sets.<br><br>
     *
     * A diff B = A &#8745; B<sup>c</sup>, i.e.
     * all elements that are in A but not in B. <br><br>
     *
     * The set A is this instance and B is the
     * param other.
     *
     * @param other the set B.
     * @return A set containing all elements in A, but
     *         not in B.
     * @throws IllegalArgumentException iff other is null.
     * */
    public ImmutableHashSet<E> diff(ImmutableHashSet<E> other) throws IllegalArgumentException
    {
        return diff(other.set);
    }

    /**
     * Creates a {@link java.util.HashSet set} containing all elements and
     * then returns it.
     *
     * @return a {@link java.util.HashSet set} containing all elements of
     *         this ImmutableHashSet.
     * */
    public Set<E> toSet()
    {
        // new to not expose the instance of the set used to handle the hashset.
        return new HashSet<>(set);
    }

    /**
     * The size of the set.
     *
     * @return the size of the set.
     * */
    public int size()
    {
        return set.size();
    }

    /**
     * Checks if the immutable set is empty.
     *
     * @return {@code true}, if the set is empty, else {@code false}.
     * */
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    /**
     * Makes the ImmutableCollection into a list.
     *
     * @return a list with the objects in the ImmutableArray
     * */
    public List<E> toList()
    {
        return new ArrayList<>(set);
    }

    /**
     * Tests a predicate against all elements.
     *
     * @param p the test.
     * @return {@code true} if all elements matches p, else {@code false}.
     * */
    public boolean forAll(Predicate<E> p)
    {
        for (var e : this)
            if (e != null && !p.test(e))
                return false;
            else if (e == null)
                return false;

        return true;
    }

    /**
     * Maps a function to each element in the array and
     * returns a new array with the newly created elements.
     *
     * @param <T> the new type of the elements
     * @param m the function to be applies to each element.
     * @return the new array with the elements mapped.w
     */
    public <T> ImmutableHashSet<T> map(Function<E, T> m)
    {
        var res = new HashSet<T>();
        set.forEach(x -> res.add(m.apply(x)));
        return new ImmutableHashSet<>(res);
    }

    /**
     * Creates a string representation of the set,
     * with the separator between the elements. <br><br>
     *
     * Returns an empty string if the list is empty,
     * no mather what then the separator is.
     *
     * @param sep the separator between the elements.
     * @return a string representation of the array, with sep as separator.
     *         If empty then an empty string will be returned.
     * */
    public String mkString(String sep)
    {
        if (size() == 0)
            return "";

        var sb = new StringBuilder("ImmutableHashSet[");

        for (var e : set)
            sb.append(e).append(sep);

        return sb.delete(sb.length() - sep.length(), sb.length()).append(']').toString();
    }

    /**
     * Drops all the elements equal to the passed argument.
     * After it has dropped the elements equal to the passed
     * argument, it will return a new ImmutableCollection with
     * the remaining elements.<br><br>
     *
     * If the passed argument is null or empty then it will return
     * the instance of the current array.
     *
     * @param e the elements to be dropped.
     * @return a new ImmutableCollection with the remaining elements.
     * */
    @SafeVarargs
    public final ImmutableHashSet<E> dropMatching(E... e)
    {
        if (e == null)
            return this;
        else if (e.length == 0)
            return this;

        var list = this.toList();

        for (var n : e)
            list = list.stream().filter(x -> !x.equals(n)).toList();


        return new ImmutableHashSet<>(list);
    }

    /**
     * Checks if the set contains an item.
     *
     * @param e the item to be searched for.
     * @return {@code true} iff the collection contains
     *         the item, otherwise {@code false}.
     * */
    public boolean contains(E e)
    {
        return set.contains(e);
    }


    @Override
    public Iterator<E> iterator()
    {
        return set.iterator();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof ImmutableHashSet ihs &&
                this.set.equals(ihs.set);
    }

    @Override
    public int hashCode()
    {
        return this.set.hashCode();
    }

    @Override
    public String toString()
    {
        return mkString(", ");
    }

    /**
     * Creates an ImmutableHashSet from a collection c.
     *
     * @param <T> The type of the elements in the collection.
     * @param c The collection to be converted.
     * @return An ImmutableHashSet containing the elements of c.
     * */
    public static <T> ImmutableHashSet<T> fromCollection(Collection<T> c)
    {
        return new ImmutableHashSet<>(c);
    }

    /**
     * Creates a new empty ImmutableHashSet.
     *
     * @param <T> The type of the elements.
     * @return An empty ImmutableHashSet.
     * */
    public static <T> ImmutableHashSet<T> empty()
    {
        return ImmutableHashSet.fromCollection(List.of());
    }
}