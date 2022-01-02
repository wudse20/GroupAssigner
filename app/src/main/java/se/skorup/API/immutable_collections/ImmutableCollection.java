package se.skorup.API.immutable_collections;

import se.skorup.API.immutable_collections.functional_interfaces.MapFunction;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * The interface and super type for the immutable collections.
 *
 * @param <E> the type of the collections elements.
 * */
public interface ImmutableCollection<E>
{
    /**
     * The size of the array.
     *
     * @return the length of the array.
     * */
    int size();

    /**
     * Checks if the immutable array is empty.
     *
     * @return {@code true}, if the array is empty, else {@code false}.
     * */
    boolean isEmpty();

    /**
     * Makes the ImmutableArray into an
     * array.
     *
     * @return an array with the objects in the ImmutableArray
     * */
    E[] toArray();

    /**
     * Makes the ImmutableArray into a
     * list.
     *
     * @return a list with the objects in the ImmutableArray
     * */
    List<E> toList();

    /**
     * Gets an element at index.
     *
     * @param index the index it will get the item at.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if the provided index is out of bounds.
     * */
    E get(int index);

    /**
     * Gets first occurrence of the element
     * matching the passed argument.
     *
     * @param elem the element that's searched for.
     * @return the first occurrence of the element, if
     *         doesn't exist it will return {@code null}
     * */
    E get(E elem);

    /**
     * Tests a predicate against all elements.
     *
     * @param p the test.
     * @return {@code true} if all elements matches p, else {@code false}.
     * */
    boolean forAll(Predicate<E> p);

    /**
     * Maps a function to each element in the array and
     * returns a new array with the newly created elements.
     *
     * @param m the mapper function.
     * @param <T> the new type of the elements
     * @return the new array with the elements mapped.w
     */
    <T> ImmutableCollection<T> map(MapFunction<E, T> m);

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
    String mkString(String sep);

    /**
     * Drops the n first elements form the collection.
     *
     * @param n the number of elements to drop in the list.
     * @return the new ImmutableArray with n dropped elements.
     * @throws IllegalArgumentException if n &gt;= size or if n &lt; 1.
     * */
    ImmutableCollection<E> drop(int n) throws IllegalArgumentException;

    /**
     * Sorts the list to its natural ordering. This
     * requires the type to implement comparator, it
     * doesn't then it will return {@code null}. If
     * the list is empty then it will return an empty
     * list.
     *
     * @return a sorted list, according to the natural ordering.
     *         if the type doesn't implement comparable then it
     *         will return {@code null}. If the list is empty, then it
     *         will return an empty list.
     * */
    ImmutableCollection<E> sorted();

    /**
     * Sorts a list according to a predicate. If the
     * list is empty an empty list will be returned.
     *
     * @param c the predicate to be compared against during
     *          the sorting.
     * @return the sorted list according to the predicate.
     *         If the list is empty, then it will return an
     *         empty list.
     * */
    ImmutableCollection<E> sortBy(Comparator<E> c);

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
    ImmutableCollection<E> dropMatching(E... e);

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
    ImmutableCollection<E> dropWhile(Predicate<E> p);

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
    ImmutableCollection<E> replace(int index, E newElement) throws IndexOutOfBoundsException;

    /**
     * Checks if the collection contains
     * an item.
     *
     * @param e the item to be searched for.
     * @return {@code true} iff the collection contains
     *         the item, otherwise {@code false}.
     * */
    boolean contains(E e);

    /**
     * Uses linear search to find the first
     * matching element to the predicate p
     * and returns it. If there are no elements
     * matching it will return {@code null}.
     *
     * @param p the predicate the elements will
     *          be tested against.
     * @return the first element matching, if no
     *         elements are matching then it will
     *         return {@code null}.
     * */
    E getFirstMatch(Predicate<E> p);


    /**
     * Uses linear search to find the index
     * of an element. It will return -1 if
     * there are no matching element. This
     * will find the first occurrence.
     *
     * @param e the item that's being searched for.
     * @return the index of the if no such element
     *         exists it will return -1
     * */
    int indexOf(E e);
}
