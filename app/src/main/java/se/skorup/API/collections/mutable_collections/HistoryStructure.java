package se.skorup.API.collections.mutable_collections;

import java.util.Optional;

/**
 * The interface for the custom history structures.
 *
 * @param <E> the type of the elements of the list.
 * */
public interface HistoryStructure<E>
{
    /**
     * The size of the structure.
     *
     * @return the size of the structure.
     * */
    int size();

    /**
     * Checks if the collection is empty,
     * i.e. size == 0 is {@code true}.
     *
     * @return {@code true} iff the list is empty.
     * */
    boolean empty();

    /**
     * Adds an element to the last spot in the list.
     * <br><br>
     * This is accomplished in O(1).
     *
     * @param e the element to be added.
     * */
    void add(E e);


    /**
     * Resets the pointer to the start of the structure.
     * */
    void reset();

    /**
     * Gets the next element in the list going
     * forwards. This means that the newer element
     * will be gotten.
     *
     * @return the next element going forwards.
     * */
    Optional<E> forward();

    /**
     * Gets the next element in the list going
     * backwards. This means that the older element
     * will be gotten.
     *
     * @return the next element going backwards.
     * */
    Optional<E> backward();

    /**
     * Peeks at the current point in history.
     *
     * @return the current point in history.
     * */
    Optional<E> peek();
}
