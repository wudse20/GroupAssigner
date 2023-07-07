package se.skorup.gui.callbacks;

/**
 * Used as a functional interface with a callback.
 *
 * @param <T> the param type of the callback.
 * */
@FunctionalInterface
public interface ActionCallback<T>
{
    /**
     * The action of the callback.
     *
     * @param param the parameter sent back with the callback.
     * */
    void action(T param);
}
