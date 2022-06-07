package se.skorup.main.gui.components;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * A button in the shape of a hexagon.
 * */
public class HexButton extends JButton
{
    private final Color background;
    private final Color border;
    private final Color foreground;

    private Polygon bounds;
    private String label;

    /**
     * Creates a new HexButton
     *
     * @param label the label of the button.
     * @param background the background color.
     * @param border the border color.
     * @param foreground the foreground color.
     * */
    public HexButton(String label, Color background, Color border, Color foreground)
    {
        this.label = label;
        this.background = background;
        this.border = border;
        this.foreground = foreground;
    }

    /**
     * Builds a hexagon with a given width, height and ratio.
     *
     * @param width the width of the hexagon.
     * @param height the height of the hexagon.
     * @param ratio the ratio between the edges.
     * @return the built hexagon.
     * */
    private Polygon buildHexagon(int width, int height, double ratio)
    {
        var hex = new Polygon();

        for (int i = 0; i < 6 ; i++)
        {
            var x = width / 2 + (int) ((width - 2) / 2 * Math.cos(i * 2 * Math.PI / 6) * ratio);
            var y = height / 2 + (int) ((height - 2) / 2 * Math.sin(i * 2 * Math.PI / 6) * ratio);
            hex.addPoint(x, y);
        }

        return hex;
    }

    /**
     * Calculates the bounds of the button.
     * */
    private void calculateBounds()
    {
        this.bounds = buildHexagon(this.getWidth(), this.getHeight(), 1.0);
    }

    @Override
    public boolean contains(Point p)
    {
        return this.bounds.contains(p);
    }

    @Override
    public boolean contains(int x, int y)
    {
        return this.contains(new Point(x, y));
    }

    @Override
    public void setSize(Dimension d)
    {
        super.setSize(d);
        this.calculateBounds();
    }

    @Override
    public void setSize(int w, int h)
    {
        this.setSize(new Dimension(w, h));
    }

    @Override
    public void setBounds(int x, int y, int w, int h)
    {
        super.setBounds(x, y, w, h);
        this.calculateBounds();
    }

    @Override
    public void setBounds(Rectangle r)
    {
        super.setBounds(r);
        this.calculateBounds();
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld;

        g.setColor(border);
        var stroke = buildHexagon(this.getWidth(), this.getHeight(), 1.01);
        g.drawPolygon(stroke);
        g.fillPolygon(stroke);

        g.setColor(background);
        var background = buildHexagon(this.getWidth(), this.getHeight(), 0.95);
        g.drawPolygon(background);
        g.fillPolygon(background);

        var f = new Font(Font.DIALOG, Font.PLAIN, 12);
        g.setFont(f);
        g.setColor(foreground);

        var fm = g.getFontMetrics();
        var width = fm.stringWidth(label);
        var height = fm.getHeight();

        g.drawString(label, (this.getWidth() - width) / 2, (this.getHeight() + height - 25) / 2);
    }

    @Override
    public String getText()
    {
        return label;
    }

    @Override
    public void setText(String text)
    {
        this.label = text;
        this.repaint();
    }
}
