package se.skorup.main.gui.objects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A text box that can be drawn in the GUI.
 * */
public class TextBox
{
    /** The text of the TextBox. */
    private final String text;

    /** The x-position of the text box. */
    private final int x;

    /** The y-position of the text box. */
    private final int y;

    /** The color of the text box. */
    private final Color c;

    /** The graphics object drawing. */
    private final Graphics2D g;

    /** The hit box of this text box. */
    private HitBox hb;

    /**
     * Creates a new TextBox.
     *
     * @param text the text of the text box.
     * @param g the graphics object drawing.
     * @param x the x-position.
     * @param y the y-position.
     * @param c the default color of the TextBox.
     * */
    public TextBox(String text, Graphics2D g, int x, int y, Color c)
    {
        this.text = text;
        this.g = g;
        this.x = x;
        this.y = y;
        this.c = c;
    }

    /**
     * Draws the text.
     * */
    public void draw()
    {
        var fm = g.getFontMetrics();
        var textWidth = fm.stringWidth(text);
        var textHeight = fm.getHeight();
        var oldColor = g.getColor();

        hb = new HitBox(x, y - textHeight, textWidth, textHeight);

        g.setColor(c);
        g.drawString(text, x, y);
        g.setColor(oldColor);
    }

    /**
     * Checks if a position is hitting the
     * TextBox.
     *
     * @param x the x-position.
     * @param y the y-position.
     * @return {@code true} iff the position is colliding
     *         with the HitBox, else {@code false}.
     * */
    public boolean isCollision(int x, int y)
    {
        return hb.isCollison(x, y);
    }

    /**
     * Getter for: text
     *
     * @return the text of the TextBox.
     * */
    public String getText()
    {
        return text;
    }

    /**
     * Getter for: hit box.
     *
     * @return the hit box of the TextBox.
     * */
    public HitBox getHitBox()
    {
        return hb;
    }
}
