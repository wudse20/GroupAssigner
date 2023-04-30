package se.skorup.main.gui.helper.hover;

import java.awt.Component;

/**
 * The hover exit interface.
 * */
@FunctionalInterface
public interface HoverEffectExit<T extends Component>
{
    /**
     * The action that happens on exit.
     *
     * @param comp the button affected.
     * */
    void onExit(T comp);
}
