package se.skorup.API.collections.immutable_collections;

import se.skorup.API.collections.immutable_collections.functional_interfaces.MapFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A immutable wrapper class for the HashSet.
 *
 * @param <E> The type of the elements.
 * */
public class ImmutableHashSet<E> implements ImmutableCollection<E>, Iterable<E>
{
    /** The set in the background. */
    private final HashSet<E> set;

    /**
     * Creates a new ImmutableHashSet from
     * an array.
     *
     * @param elems the elementes to be added.
     * */
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
    public ImmutableHashSet(ImmutableCollection col)
    {
        this.set = new HashSet<>(col.toList());
    }

    /**
     * The intersection between this set and the
     * other set.
     *
     * @param other the other set. If {@code null} the it throws
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
     * @param other the other set. If {@code null} the it throws
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
     * @param other the other set. If {@code null} the it throws
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
     * @param other the other set. If {@code null} the it throws
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
     * all elements that are in A but not in B. <br><br>
     *
     * The set A is this instance and B is the
     * param other.
     *
     * @param other the set B.
     * @return A set containing all elements in A but
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
     * @return A set containing all elements in A but
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

    @Override
    public int size()
    {
        return set.size();
    }

    @Override
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    @Override
    public E[] toArray()
    {
        var arr = (E[]) new Object[set.size()];
        return set.toArray(arr);
    }

    @Override
    public List<E> toList()
    {
        return new ArrayList<>(set);
    }

    /**
     * Doesn't work on the ImmutableHashSet, since
     * the set cannot be indexed.
     *
     * @param index see {@link ImmutableCollection#get(int)}.
     * @return see {@link ImmutableCollection#get(int)}.
     * @throws UnsupportedOperationException Always since it cannot be indexed.
     * */
    @Override
    public E get(int index) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Cannot get item from index in set.");
    }

    @Override
    public E get(E elem)
    {
        if (set.contains(elem))
        {
            return new ArrayList<>(set).stream()
                                       .filter(x -> x.equals(elem))
                                       .findFirst()
                                       .orElse(null);
        }

        return null;
    }

    @Override
    public boolean forAll(Predicate<E> p)
    {
        for (var e : this)
            if (e != null && !p.test(e))
                return false;
            else if (e == null)
                return false;

        return true;
    }

    @Override
    public <T> ImmutableHashSet<T> map(MapFunction<E, T> m)
    {
        var res = new HashSet<T>();
        set.forEach(x -> res.add(m.apply(x)));
        return new ImmutableHashSet<>(res);
    }

    @Override
    public String mkString(String sep)
    {
        return ImmutableArray.fromCollection(set).mkString(sep);
    }

    /**
     * Doesn't work on the ImmutableHashSet, since
     * the set cannot drop first elements.
     *
     * @param n see {@link ImmutableCollection#drop(int)}.
     * @return see {@link ImmutableCollection#drop(int)}.
     * @throws IllegalArgumentException see {@link ImmutableCollection#drop(int)}.
     * @throws UnsupportedOperationException Always since it cannot drop first elements.
     * */
    @Override
    public ImmutableCollection<E> drop(int n) throws IllegalArgumentException
    {
        throw new UnsupportedOperationException("Cannot drop first elements of a set.");
    }

    @Override
    public ImmutableArray<E> sorted()
    {
        return ImmutableArray.fromCollection(set).sorted();
    }

    @Override
    public ImmutableArray<E> sortBy(Comparator<E> c)
    {
        return ImmutableArray.fromCollection(set).sortBy(c);
    }

    @Override
    public ImmutableHashSet<E> dropMatching(E... e)
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
     * Doesn't work on the ImmutableHashSet, since
     * the set cannot drop while, since there are
     * no ordering
     *
     * @param p see {@link ImmutableCollection#dropWhile(Predicate)}.
     * @return see {@link ImmutableCollection#dropWhile(Predicate)}.
     * @throws UnsupportedOperationException Always since it cannot drop while, since
     *                                       there are no ordering.
     * */
    @Override
    public ImmutableCollection<E> dropWhile(Predicate<E> p)
    {
        throw new UnsupportedOperationException("There are no specified order, so cannot traverse in an order.");
    }

    /**
     * Cannot be used on sets, since they cannot
     * be indexed.
     *
     * @param index see {@link ImmutableCollection#replace(int, Object)}
     * @param newElement see {@link ImmutableCollection#replace(int, Object)}
     * @return see {@link ImmutableCollection#replace(int, Object)}
     * @throws IndexOutOfBoundsException see {@link ImmutableCollection#replace(int, Object)}
     * @throws UnsupportedOperationException Always, since indices doesn't make sense on sets.
     * */
    @Override
    public ImmutableCollection<E> replace(int index, E newElement)
            throws IndexOutOfBoundsException, UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Cannot get item from index in set.");
    }

    @Override
    public boolean contains(E e)
    {
        return set.contains(e);
    }

    /**
     * Uses linear search to find the first
     * matching element to the predicate p
     * and returns it. If there are no elements
     * matching it will return {@code null}. <br><br>
     *
     * Since there's is no way to determine the order
     * it will just give any element match the predicate
     * if it exists.
     *
     * @param p the predicate the elements will
     *          be tested against.
     * @return the first element matching, if no
     *         elements are matching then it will
     *         return {@code null}.
     * */
    @Override
    public E getFirstMatch(Predicate<E> p)
    {
        for (var e : this)
            if (p.test(e))
                return e;

        return null;
    }

    /**
     * THIS WILL ALWAYS RETURN -1,
     * SINCE THERE'S NO INDICES IN
     * THE SET.
     *
     * @param e {@link ImmutableCollection#indexOf}
     * @return {@link ImmutableCollection#indexOf}
     * */
    @Override
    public int indexOf(E e)
    {
        return -1;
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
        return '[' + mkString(", ") + ']';
    }
}
