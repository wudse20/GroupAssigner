package se.skorup.gui.layouts;

import java.awt.GridLayout;
import java.awt.LayoutManager;

/**
 * Generates a group generator for N columns
 * */
public abstract class NColumnGenerator implements LayoutGenerator
{
    private final int columns, width, height;

    /**
     * Creates a new NColumnGenerator.
     *
     * @param columns the number of columns.
     * @param height the vgap
     * @param width the number for hgap calculation.
     * */
    protected NColumnGenerator(int columns, int width, int height)
    {
        this.columns = columns;
        this.width = width;
        this.height = height;
    }

    @Override
    public final LayoutManager generateLayout(int nbrGroups)
    {
        var rows = (int) Math.ceil(((double) nbrGroups) / columns);
        return new GridLayout(rows, columns, width / (columns + 1), height);
    }

    @Override
    public String toString()
    {
        return "ColumnGenerator(N = %d)".formatted(columns);
    }
}
