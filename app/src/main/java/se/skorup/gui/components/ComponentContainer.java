package se.skorup.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;

/**
 * A container for the button.
 * */
public final class ComponentContainer extends Panel
{
    /**
     * Creates a new panel.
     *
     * @param btn The layout of the panel.
     * */
    public ComponentContainer(Component btn)
    {
        super(new BorderLayout());

        this.add(new Label(" "), BorderLayout.PAGE_START);
        this.add(new Label("     "), BorderLayout.LINE_START);
        this.add(btn, BorderLayout.CENTER);
        this.add(new Label("     "), BorderLayout.LINE_END);
        this.add(new Label(" "), BorderLayout.PAGE_END);
    }
}