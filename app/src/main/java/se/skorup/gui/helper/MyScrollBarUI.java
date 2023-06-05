package se.skorup.gui.helper;

import se.skorup.util.Utils;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * A custom UI that matches with the rest of the program.
 * */
public class MyScrollBarUI extends BasicScrollBarUI
{
    @Override
    protected void configureScrollBarColors()
    {
        thumbColor = Utils.COMPONENT_BACKGROUND_COLOR.darker().darker();
        trackColor = Utils.BACKGROUND_COLOR;
    }

    @Override
    protected void paintThumb(Graphics gOld, JComponent c, Rectangle thumbBounds)
    {
        var g = (Graphics2D) gOld;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(thumbColor);
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected JButton createDecreaseButton(int orientation)
    {
        return new JButton()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(0, 0);
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation)
    {
        return new JButton()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(0, 0);
            }
        };
    }
}