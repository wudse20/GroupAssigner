package se.skorup.gui.callbacks;

import java.awt.Component;

/**
 * The hover-enter interface
 * */
@FunctionalInterface
public interface HoverEffectEnter<T extends Component>
{
    /**
     * The action that happens on enter.
     *
     * @param comp the button affected.
     * */
    void onEnter(T comp);
}