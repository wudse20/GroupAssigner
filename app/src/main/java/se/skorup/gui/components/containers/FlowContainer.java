package se.skorup.gui.components.containers;

import java.awt.Component;
import java.awt.FlowLayout;

/**
 * A container with one item that uses a FlowLayout.
 *
 * @see java.awt.FlowLayout FlowLayout
 * */
public final class FlowContainer extends Panel
{
    /**
     * Creates a new FlowContainer.
     *
     * @param c the component to be held in the container.
     * @param alignment the alignment of the flow layout.
     * @see FlowLayout#LEFT
     * @see FlowLayout#RIGHT
     * @see FlowLayout#CENTER
     * @see FlowLayout#TRAILING
     * @see FlowLayout#LEADING
     * */
    public FlowContainer(Component c, int alignment)
    {
        super(new FlowLayout(alignment));
        this.add(c);
    }
}
