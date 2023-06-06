package se.skorup.gui.helper;

import se.skorup.util.Utils;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

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
    protected void paintTabBackground(
        Graphics g, int tabPlacement, int tabIndex, int x,
        int y, int width, int height, boolean selected
    )
    {
        g.setColor(selected ? Utils.BACKGROUND_COLOR : Utils.COMPONENT_BACKGROUND_COLOR);
        g.fillRect(x, y, width, height);
    }
}