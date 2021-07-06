package se.skorup.main.gui.panels;

import se.skorup.API.ImmutableArray;
import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.Set;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel implements Scrollable
{
    /** The vertical spacer. */
    private static final int VERTICAL_SPACER = 25;

    /** The instance of the GroupFrame. */
    private final GroupFrame gf;

    /** The font metrics of the font. */
    private final FontMetrics fm;

    /** The sub groups being displayed. */
    private ImmutableArray<Set<Integer>> subgroups;

    /**
     * Creates a new SubGroupPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * @param fm the FontMetrics to operate off of.
     * */
    public SubgroupPanel(GroupFrame gf, FontMetrics fm)
    {
        this.gf = gf;
        this.fm = fm;
        subgroups = ImmutableArray.fromCollection(gf.getLastSubgroups());
    }

    @Override
    public Dimension getPreferredSize()
    {
        var height = subgroups.size() / 2 * subgroups.get(0).size() * (fm.getHeight() + VERTICAL_SPACER);
        return new Dimension(512, height);
    }

    @Override
    public Dimension getMinimumSize()
    {
        var height = subgroups.size() / 2 * subgroups.get(0).size() * (fm.getHeight() + VERTICAL_SPACER);
        return new Dimension(512, height);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return new Dimension(512, 256);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 128;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 128;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return getPreferredSize().width <= getParent().getSize().width;
    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return getPreferredSize().height <= getParent().getSize().height;
    }
}
