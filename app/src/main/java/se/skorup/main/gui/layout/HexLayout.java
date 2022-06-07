package se.skorup.main.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * A layout manager for displaying hexagon buttons,
 * in a honeycomb shape.
 * */
public class HexLayout implements LayoutManager
{
    private final boolean isFirstRowEven;
    private final int columns;

    /**
     * Creates a new HexLayout with a specified amount of
     * columns and with the instruction if the first row
     * is odd or even.
     *
     * @param columns the number of columns in the honeycomb
     *                pattern.
     * @param isFirstRowEven if {@code true} then the first row is even,
     *                       else if {@code false} then the first row is odd.
     * */
    public HexLayout(int columns, boolean isFirstRowEven)
    {
        this.columns = columns;
        this.isFirstRowEven = isFirstRowEven;
    }

    /**
     * Calculates the number of rows based on the number of
     * columns and if the first row is even or odd.
     *
     * @param parent the parent container of this layout.
     * @return the number of rows.
     * */
    private int calculateNbrRows(Container parent)
    {
        var rows = (this.isFirstRowEven) ? 1 : 0;
        var cells = parent.getComponents().length;

        while (cells > 0)
            cells -= this.columns - (rows++ % 2);

        if (this.isFirstRowEven)
            rows--;

        return rows;
    }

    /**
     * Calculates the size of the layout.
     *
     * @param parent the parent container.
     * @return the size of the layout.
     * */
    private Dimension getSize(Container parent)
    {
        var rows = calculateNbrRows(parent);
        var hexWidth = (parent.getWidth() / (columns * 2 + columns - 1)) * 2;
        var hexHeight = (parent.getHeight() / (rows + 1)) * 2;

        return new Dimension(rows * hexHeight, columns * hexWidth);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        return getSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent)
    {
        return getSize(parent);
    }

    @Override
    public void layoutContainer(Container parent)
    {
        var rows = calculateNbrRows(parent);
        var hexWidth = (parent.getWidth() / (columns * 2 + columns - 1)) * 2;
        var hexHeight = (parent.getHeight() / (rows + 1)) * 2;
        var currentRow = 0;
        var currentColumn = 0;
        var iteration = isFirstRowEven ? 0 : 1;

        for (var c : parent.getComponents())
        {
            var evenRow = iteration % 2 == 0;
            var offsetX = 0;
            var offsetY = 0;

            if (evenRow)
            {
                offsetX = (int) (hexWidth * .75) + currentColumn * hexWidth + currentColumn * (hexWidth / 2);
                offsetY = currentRow * (hexHeight / 2 - 6);
            }
            else
            {
                offsetX = currentColumn * hexWidth + currentColumn * (hexWidth / 2);
                offsetY = currentRow * (hexHeight / 2 - 6);
            }

            c.setBounds(offsetX, offsetY, hexWidth, hexHeight);

            currentColumn++;

            if ((evenRow && currentColumn == columns - 1) || (!evenRow && currentColumn == this.columns))
            {
                currentColumn = 0;
                currentRow++;
                iteration++;
            }
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}
}
