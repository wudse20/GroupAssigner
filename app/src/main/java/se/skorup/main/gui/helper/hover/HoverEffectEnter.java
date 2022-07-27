package se.skorup.main.gui.helper.hover;

import java.awt.Component;

/**
 * The hover enter interface
 * */
@FunctionalInterface
public interface HoverEffectEnter
{
    /**
     * The action that happens on enter.
     *
     * @param comp the button affected.
     * */
    void onEnter(Component comp);
}
