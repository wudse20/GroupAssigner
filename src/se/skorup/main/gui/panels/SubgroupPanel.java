package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.SubGroup;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * The panel that draws the SubGroups.
 * */
public class SubgroupPanel extends JPanel implements Scrollable
{
    /** The vertical spacer. */
    private static final int VERTICAL_SPACER = 25;

    /** The instance of the GroupFrame. */
    private final GroupFrame gf;

    /** The GroupManager in use. */
    private GroupManager gm;

    /** The font metrics of the font. */
    private FontMetrics fm;

    /** The current groups. */
    private SubGroup currentGroups;

    /**
     * Creates a new SubGroupPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * @param gm the instance of the GroupManager that
     *           holds the persons with the IDs.
     * */
    public SubgroupPanel(GroupFrame gf, GroupManager gm)
    {
        this.gf = gf;
        this.gm = gm;
        this.currentGroups = gf.getCurrentGroups();
        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Draws the groups, if they're
     * created.
     * */
    public void drawGroups()
    {

    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld;
        fm = g.getFontMetrics();

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public Dimension getPreferredSize()
    {
        var height = currentGroups.groups().size() / 2 *
            ((currentGroups.groups().size() != 0) ? currentGroups.groups().get(0).size() : 5) *
            ((fm != null ? fm.getHeight() : 0) + VERTICAL_SPACER);
        return new Dimension(512, height);
    }

    @Override
    public Dimension getMinimumSize()
    {
        var height = currentGroups.groups().size() / 2 *
            ((currentGroups.groups().size() != 0) ? currentGroups.groups().get(0).size() : 5) *
            ((fm != null ? fm.getHeight() : 0) + VERTICAL_SPACER);
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
