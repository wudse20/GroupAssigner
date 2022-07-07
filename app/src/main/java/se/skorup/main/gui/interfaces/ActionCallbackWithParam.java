package se.skorup.main.gui.interfaces;

/**
 * A callback with a param.
 * */
@FunctionalInterface
public interface ActionCallbackWithParam<T>
{
    /**
     * An action with a String.
     *
     * @param param the generated param from the action.
     * */
    void action(T param);
}
