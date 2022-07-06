package se.skorup.main.gui.interfaces;

/**
 * A callback with a String.
 * */
@FunctionalInterface
public interface StringActionCallback
{
    /**
     * An action with a String.
     *
     * @param text the generated text from the action.
     * */
    void action(String text);
}
