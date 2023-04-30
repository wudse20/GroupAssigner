package se.skorup.main.gui.helper.layout;

/**
 * A layout with single column generator.
 * */
public class SingleColumnGenerator extends NColumnGenerator
{
    /**
     * Creates a new SingleColumnGenerator.
     *
     * @param height the vgap
     * @param width the number for hgap calculation.
     * */
    public SingleColumnGenerator(int width, int height)
    {
        super(1, width, height);
    }
}
