package se.skorup.main.gui.helper;

/**
 * Generates a column generator with two columns.
 * */
public class TripleColumnGenerator extends NColumnGenerator
{
    /**
     * Creates a new TripleColumnGenerator.
     *
     * @param height the vgap
     * @param width the number for hgap calculation.
     * */
    public TripleColumnGenerator(int width, int height)
    {
        super(3, width, height);
    }
}
