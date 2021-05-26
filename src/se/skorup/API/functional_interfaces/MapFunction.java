package se.skorup.API.functional_interfaces;

/**
 * The interface used in: <br>
 *     - {@link ImmutableArray#map(MapFunction)}
 *
 * @param <E> the type of the elements to be converted
 * @param <T> the new type of the converted elements
 * */
@FunctionalInterface
public interface MapFunction<E, T>
{
    /**
     * The function that is being applied
     * to the elements.
     *
     * @param e the object the function is being applied to.
     * @return the result of the applied function.s
     * */
    T apply(E e);
}
