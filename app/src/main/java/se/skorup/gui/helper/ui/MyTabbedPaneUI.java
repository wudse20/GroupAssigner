package se.skorup.gui.helper.ui;

import se.skorup.util.Utils;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A custom UI that matches with the rest of the program.
 * */
public class MyTabbedPaneUI extends BasicTabbedPaneUI
{
    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex)
    {
        // Do nothing to prevent the border from being drawn :)
    }

    @Override
    protected void paintFocusIndicator(
        Graphics g, int tabPlacement, Rectangle[] rects,
        int tabIndex, Rectangle iconRect, Rectangle textRect,
        boolean isSelected
    )
    {
        // Do nothing to prevent the focus border to be painted.
    }

    @Override
    protected void paintTabBackground(
        Graphics g, int tabPlacement, int tabIndex, int x,
        int y, int width, int height, boolean selected
    )
    {
        g.setColor(selected ? Utils.BACKGROUND_COLOR : Utils.COMPONENT_BACKGROUND_COLOR);
        g.fillRect(x, y, width, height);
    }
}