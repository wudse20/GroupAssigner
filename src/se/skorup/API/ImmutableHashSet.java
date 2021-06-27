package se.skorup.API;

import se.skorup.API.functional_interfaces.MapFunction;

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
     * @param other the other set.
     * @return the elements both in the other set and this set.
     * */
    public ImmutableHashSet<E> intersection(Set<E> other)
    {
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
     * @param other the other set.
     * @return A set containing the elements both in the other set and this set.
     * */
    public ImmutableHashSet<E> intersection(ImmutableHashSet<E> other)
    {
        return intersection(other.set);
    }

    /**
     * The union between this set and the other set.
     *
     * @param other the other set.
     * @return A set containing all the elements of both sets.
     * */
    public ImmutableHashSet<E> union(Set<E> other)
    {
        var res = new HashSet<>((other.size() > size()) ? other : set);
        res.addAll((other.size() < size()) ? other : set);
        return new ImmutableHashSet<>(res);
    }

    /**
     * The union between this set and the other set.
     *
     * @param other the other set.
     * @return A set containing all the elements of both sets.
     * */
    public ImmutableHashSet<E> union(ImmutableHashSet<E> other)
    {
        return new ImmutableHashSet<>(other.set);
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
        for (var e : set)
            if (!p.test(e))
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

    @Override
    public ImmutableCollection<E> dropWhile(Predicate<E> p)
    {
        var imArr = ImmutableArray.fromCollection(set).dropWhile(p);
        return new ImmutableHashSet<>(imArr);
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

    @Override
    public Iterator<E> iterator()
    {
        return set.iterator();
    }
}
