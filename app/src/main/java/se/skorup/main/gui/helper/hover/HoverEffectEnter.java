package se.skorup.main.gui.helper.hover;

import se.skorup.main.gui.components.SubgroupItemButton;

/**
 * The hover enter interface
 * */
@FunctionalInterface
public interface HoverEffectEnter
{
    /**
     * The action that happens on enter.
     *
     * @param btn the button affected.
     * */
    void onEnter(SubgroupItemButton btn);
}
