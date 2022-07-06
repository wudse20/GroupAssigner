package se.skorup.main.gui.interfaces;

/**
 * A callback with text from a button.
 * */
@FunctionalInterface
public interface ButtonCallback
{
    /**
     * An action with a String.
     *
     * @param text the generated text from the action.
     * */
    void action(String text);
}
