package se.skorup.API;

import se.skorup.API.functional_interfaces.FillFunction;
import se.skorup.API.functional_interfaces.MapFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An immutable array.
 *
 * @param <E> The type of the elements
 */
public class ImmutableArray<E> implements ImmutableCollection<E>, Iterable<E>
{
    /** The elements of the array. */
    private final E[] elems;

    /**
     * Creates an immutable array,
     * with the passed elements.
     *
     * @param elems the elements to be added
     */
    @SafeVarargs
    public ImmutableArray(E... elems)
    {
        this.elems = elems;
    }

    /**
     * Creates an immutable array from a list.<br><br>
     *
     * If the list is empty then it will create an empty
     * new ImmutableArray&lt;T&gt;.
     *
     * @param list the list of elements to be added to the array.
     */
    public ImmutableArray(List<E> list)
    {
        E[] arr = (list == null) ? (E[]) new Object[0] : (E[]) new Object[list.size()];

        if (list != null)
            list.toArray(arr);

        elems = arr;
    }

    /**
     * The size of the array.
     *
     * @return the length of the array.
     * */
    public int size()
    {
        return elems.length;
    }

    /**
     * Checks if the immutable array is empty.
     *
     * @return {@code true}, if the array is empty, else {@code false}.
     * */
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Makes the ImmutableArray into an
     * array.
     *
     * @return an array with the objects in the ImmutableArray
     * */
    @Override
    public E[] toArray()
    {
        return elems;
    }

    /**
     * Makes the ImmutableArray into a
     * list.
     *
     * @return a list with the objects in the ImmutableArray
     * */
    @Override
    public List<E> toList()
    {
        return Arrays.asList(elems);
    }

    /**
     * Gets an element at index.
     *
     * @param index the index it will get the item at.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if the provided index is out of bounds.
     * */
    @Override
    public E get(int index) throws IndexOutOfBoundsException
    {
        return elems[index];
    }

    /**
     * Gets first occurrence of the element
     * matching the passed argument.
     *
     * @param elem the element that's searched for.
     * @return the first occurrence of the element, if
     *         doesn't exist it will return {@code null}
     * */
    @Override
    public E get(E elem)
    {
        for (var e : this)
        {
            if (e.equals(elem))
                return e;
        }

        return null;
    }

    /**
     * Tests a predicate against all elements.
     *
     * @param p the test.
     * @return {@code true} if all elements matches p, else {@code false}.
     * */
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

    /**
     * Maps a function to each element in the array and
     * returns a new array with the newly created elements.
     *
     * @param <T> the new type of the elements
     * @param m the mapper function.
     * @return the new array with the elements mapped.w
     */
    @Override
    public<T> ImmutableArray<T> map(MapFunction<E, T> m)
    {
        var newArr = (T []) new Object[size()];

        for (int i = 0; i < size(); i++)
            newArr[i] = m.apply(elems[i]);

        return new ImmutableArray<>(newArr);
    }

    /**
     * Creates a string representation of the list,
     * with the separator between the elements. <br><br>
     *
     * Returns an empty string if the list is empty,
     * no mather what the the separator is.
     *
     * @param sep the separator between the elements.
     * @return a string representation of the array, with sep as separator.
     *         If empty then an empty string will be returned.
     * */
    @Override
    public String mkString(final String sep)
    {
        if (isEmpty())
            return "";

        final var sb = new StringBuilder();
        forEach(x -> sb.append(x).append(sep));
        return sb.substring(0, sb.length() - sep.length());
    }

    /**
     * Drops the n first elements form the collection.
     *
     * @param n the number of elements to drop in the list.
     * @return the new ImmutableArray with n dropped elements.
     * @throws IllegalArgumentException if n >= size.
     * */
    @Override
    public ImmutableArray<E> drop(int n) throws IllegalArgumentException
    {
        if (n >= size())
            throw new IllegalArgumentException(
                    "n = %d is out of bounds for size = %d".formatted(n, size())
            );
        else if (n < 1)
            throw new IllegalArgumentException("n = %d must be greater than 1".formatted(n));

        var list = new ArrayList<E>();

        for (var i = n; i < size(); i++)
            list.add(this.get(i));

        return ImmutableArray.fromList(list);
    }

    /**
     * Sorts the list to its natural ordering. This
     * requires the type to implement comparator, it
     * doesn't then it will return {@code null}. If
     * the list is empty then it will return an empty
     * list.<br><br>
     *
     * This sorting is stable.
     *
     * @return a sorted list, according to the natural ordering.
     *         if the type doesn't implement comparable then it
     *         will return {@code null}. If the list is empty, then it
     *         will return an empty list.
     * */
    @Override
    public ImmutableArray<E> sorted()
    {
        if (isEmpty())
            return new ImmutableArray<>();

        try
        {
            var list = (List<Comparable<E>>) this.toList();
            list.sort((a, b) -> a.compareTo((E) b));
            return (ImmutableArray<E>) ImmutableArray.fromList(list);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    /**
     * Sorts a list according to a predicate. If the
     * list is empty an empty list will be returned.<br><br>
     *
     * This sorting is stable.
     *
     * @param c the predicate to be compared against during
     *          the sorting.
     * @return  the sorted list according to the predicate.
     *          If the list is empty, then it will return an
     *          empty list.
     */
    @Override
    public ImmutableArray<E> sortBy(Comparator<E> c)
    {
        if (isEmpty())
            return new ImmutableArray<>();

        var list = this.toList();
        list.sort(c);
        return ImmutableArray.fromList(list);
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
    @Override @SafeVarargs
    public final ImmutableArray<E> dropMatching(E... e)
    {
        if (e == null)
            return this;
        else if (e.length == 0)
            return this;

        var list = this.toList();

        for (var n : e)
            list = list.stream().filter(x -> !x.equals(n)).toList();


        return ImmutableArray.fromList(list);
    }

    /**
     * Drops the elements while the passed predicate
     * is true. <br><br>
     *
     * If the passed argument is null then it will
     * return it self.
     *
     * @param p the predicate to be tested against the elements.
     * @return an ImmutableCollection with the remaining elements,
     *         if all elements are removed then it will return an
     *         empty ImmutableArray. If the passed argument, p, is
     *         null then it will return it self;
     * */
    @Override
    public ImmutableArray<E> dropWhile(Predicate<E> p)
    {
        if (p == null)
            return this;

        var res = new ArrayList<E>();

        for (var e : this)
        {
            if (p.test(e))
                res.add(e);
            else
                break;
        }

        return ImmutableArray.fromList(res);
    }

    /**
     * Replaces a element at a specified index and
     * returns a new ImmutableCollection with the
     * updated element.
     *
     * @param index the index to replace the element at.
     * @param newElement the new element.
     * @return a new ImmutableCollection where the newElement has been placed
     *          at index index.
     * @throws IndexOutOfBoundsException iff the index is out of bounds for the collection.
     * */
    @Override
    public ImmutableArray<E> replace(int index, E newElement) throws IndexOutOfBoundsException
    {
        var newArr = new ImmutableArray<>(elems);
        newArr.elems[index] = newElement;
        return newArr;
    }

    /**
     * Checks if the collection contains
     * an item.
     *
     * @param e the item to be searched for.
     * @return {@code true} iff the collection contains
     *         the item, otherwise {@code false}.
     * */
    @Override
    public boolean contains(E e)
    {
        return this.toList().contains(e);
    }

    @Override
    public Iterator<E> iterator()
    {
        return new ImmutableArrayIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action)
    {
        for (var e : elems)
            action.accept(e);
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof ImmutableArray arr &&
                arr.size() == size() &&
                Arrays.equals(elems, arr.elems);
    }

    /**
     * Calls {@link Arrays#hashCode(Object[])} to create the hash code.
     *
     * @return the value of {@link Arrays#hashCode(Object[])}.
     * */
    @Override
    public int hashCode()
    {
        return Arrays.hashCode(elems);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(elems);
    }

    /**
     * The iterator for the immutable array.
     * */
    private class ImmutableArrayIterator implements Iterator<E>
    {
        /** The counter of the iterator.*/
        private int count;

        /**
         * Initializes the Iterator
         * */
        private ImmutableArrayIterator()
        {
            count = -1;
        }

        @Override
        public boolean hasNext()
        {
            return (count + 1) < size();
        }

        @Override
        public E next()
        {
            if (!hasNext())
                throw new NoSuchElementException();

            return elems[++count];
        }
    }

    /**
     * Creates an ImmutableArray from a List. If the passed list is
     * null then it will return a new empty ImmutableArray with elements
     * of type T.
     *
     * @param <T> the type of the elements of the List.
     * @param list the list of elements to be added.
     * @return an ImmutableArray with the elements, same references, from the list.
     *         If the passed list is null it will return an empty ImmutableArray&lt;T&gt;.
     * */
    public static <T> ImmutableArray<T> fromList(List<T> list)
    {
        return new ImmutableArray<>(list);
    }

    /**
     * Creates a new ImmutableArray from an collection. If the passed
     * collection is null then it will return a new ImmutableArray with
     * elements of type T.
     *
     * @param <T> the type of the elements.
     * @param col the collection to be converted to an ImmutableArray.
     * @return the ImmutableArray that the collection was converted to.
     *         If col is {@code null} then it will return an empty
     *         ImmutableArray of type ImmutableArray&lt;T&gt;
     * */
    public static <T> ImmutableArray<T> fromCollection(Collection<T> col)
    {
        if (col == null)
            return new ImmutableArray<>();

        return ImmutableArray.fromList(new Vector<>(col));
    }

    /**
     * Creates an ImmutableArray from an array. If the passed array is null
     * then it will return a new empty ImmutableArray with elements of type
     * T.
     *
     * @param <T> the type of the element of the list.
     * @param arr the array of elements to be created.
     * @return an ImmutableArray with the elements, sam references, from the array.
     *         If the passed argument is null then it will return an empty immutable
     *         array with elements of type T.
     * */
    public static <T> ImmutableArray<T> fromArray(T[] arr)
    {
        if (arr == null)
            return new ImmutableArray<>();

        return new ImmutableArray<>(arr);
    }

    /**
     * Fills an ImmutableArray with n elements according to the
     * fill function, f.
     *
     * @param <T> the type of the elements.
     * @param n the number of elements.
     * @param f the function called to fill the elements.
     * @return an ImmutableArray with n elements according to
     *         the fill function.
     * @throws IllegalArgumentException if the FillFunction, f, is null and if fill &lt;= 0.
     * */
    public static <T> ImmutableArray<T> fill(int n, FillFunction<T> f) throws IllegalArgumentException
    {
        if (f == null)
            throw new IllegalArgumentException("Parameter f cannot be null");
        else if (n <= 0)
            throw new IllegalArgumentException(
                    "Parameter n, %d, cannot be less or equal to zero.".formatted(n)
            );

        var al = new ArrayList<T>();

        for (int i = 0; i < n; i++)
            al.add(f.action());

        return ImmutableArray.fromList(al);
    }
}