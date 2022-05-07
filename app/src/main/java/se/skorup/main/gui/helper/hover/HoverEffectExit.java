package se.skorup.main.gui.helper.hover;

import se.skorup.main.gui.components.SubgroupItemButton;

/**
 * The hover exit interface.
 * */
@FunctionalInterface
public interface HoverEffectExit
{
    /**
     * The action that happens on exit.
     *
     * @param btn the button affected.
     * */
    void onExit(SubgroupItemButton btn);
}
