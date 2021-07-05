package se.skorup.main.gui.panels;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import java.awt.Dimension;
import java.awt.Rectangle;

public class SubGroupPanel extends JPanel implements Scrollable
{
    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }
}
