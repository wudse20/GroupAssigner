package se.skorup.gui.layouts;

/**
 * Generates a column generator with two columns.
 * */
public class DoubleColumnGenerator extends NColumnGenerator
{
    /**
     * Creates a new DoubleColumnGenerator.
     *
     * @param height the vgap
     * @param width the number for hgap calculation.
     * */
    public DoubleColumnGenerator(int width, int height)
    {
        super(2, width, height);
    }
}
