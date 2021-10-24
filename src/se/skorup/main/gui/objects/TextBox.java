package se.skorup.main.gui.objects;

import se.skorup.API.util.DebugMethods;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A text box that can be drawn in the GUI.
 * */
public class TextBox
{
    /** The text of the TextBox. */
    private String text;

    /** The x-position of the text box. */
    private final int x;

    /** The y-position of the text box. */
    private final int y;

    /** The color of the text box. */
    private Color c;

    /** The hit box of this text box. */
    private HitBox hb;

    /**
     * Creates a new TextBox.
     *
     * @param text the text of the text box.
     * @param x the x-position.
     * @param y the y-position.
     * @param c the default color of the TextBox.
     * */
    public TextBox(String text, int x, int y, Color c)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.c = c;
    }

    /**
     * Draws the text, with the
     * default color the object
     * was instantiated with.
     *
     * @param g the instance of the Graphics object drawing.
     * */
    public void draw(Graphics2D g)
    {
        draw(this.c, g);
    }

    /**
     * Draws the text with the
     * passed color.
     *
     * @param c the color the text will be drawn in.
     * @param g the instance of the Graphics object drawing.
     * */
    public void draw(Color c, Graphics2D g)
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
        var res = hb.isCollision(x, y);

        if (res)
            DebugMethods.log("Detected hit on %s".formatted(text), DebugMethods.LogType.DEBUG);

        return res;
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
     * Setter for: text
     *
     * @param text the new text of the TextBox.
     * */
    public void setText(String text)
    {
        this.text = text;
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

    /**
     * Setter for: color.
     *
     * @param c the new color.
     * */
    public void setColor(Color c)
    {
        this.c = c;
    }

    /**
     * Getter for: color.
     *
     * @return the current color.
     * */
    public Color getColor()
    {
        return this.c;
    }

    @Override
    public String toString()
    {
        return "[text = " + text + ", color = " + c + "]";
    }

    @Override
    public int hashCode()
    {
        return text.hashCode() + c.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof TextBox t &&
               text.equals(t.text)    &&
               c.equals(t.c);
    }
}
