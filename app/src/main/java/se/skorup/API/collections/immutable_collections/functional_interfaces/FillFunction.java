package se.skorup.API.collections.immutable_collections.functional_interfaces;

/**
 * The functional interface used to fill an immutable array.
 *
 * @param <T> The type of the elements of the ImmutableArray.
 * */
@FunctionalInterface
public interface FillFunction<T>
{
    /**
     * The action.
     *
     * @return the result of implemented action.
     * */
    T action();
}
