package se.skorup.main.gui.objects;

import java.awt.Graphics2D;

/**
 * A text box that can be drawn in the GUI.
 * */
public class TextBox
{
    /** The text of the TextBox. */
    private String text;

    /** The graphics object drawing. */
    private final Graphics2D g;

    /** The hit box of this text box. */
    private HitBox hb;

    /**
     * Creates a new TextBox.
     *go
     * @param text the text of the text box.
     * @param g the graphics object drawing.
     * */
    public TextBox(String text, Graphics2D g)
    {
        this.text = text;
        this.g = g;
    }

    /**
     * Draws the text.
     * */
    public void draw(int x, int y)
    {
        var fm = g.getFontMetrics();
        var textWidth = fm.stringWidth(text);
        var textHeight = fm.getHeight();

        hb = new HitBox(x, y, textWidth, textHeight);
        g.drawString(text, x, y);
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
     * Setter for: text.
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
}
