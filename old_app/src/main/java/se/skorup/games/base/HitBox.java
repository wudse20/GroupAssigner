package se.skorup.games.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A hit box.
 *
 * @param p the position of the HitBox
 * @param width the width of the HitBox
 * @param height the height of the HitBox
 * */
public record HitBox(Pos p, int width, int height)
{
    /**
     * Checks if a position is in the bounds of the HitBox
     *
     * @param p2 the position to be checked.
     * @return {@code true} if the pos, p2, is in bounds of the hit box.
     * */
    public boolean isPosInBound(Pos p2)
    {
        return p.x() <= p2.x()          &&
               p.x() + width >= p2.x()  &&
               p.y() <= p2.y()          &&
               p.y() + height >= p2.y();
    }

    /**
     * Checks if two hit boxes are overlapping.
     *
     * @param hb the HitBox to be checked.
     * @return {@code true} if they are overlapping, else {@code false}
     * */
    public boolean isHitBoxOverlapping(HitBox hb)
    {
        return p.x() <= hb.p.x() + hb.width  &&
               p.x() + width >= hb.p.x()     &&
               p.y() <= hb.p.y() + hb.height &&
               p.y() + height >= hb.p.y();
    }

    /**
     * Draws the HitBox.
     *
     * @param g the graphics object drawing
     * @param c the color of the HitBox
     * */
    public void draw(Graphics2D g, Color c, float lineWidth)
    {
        var stroke = g.getStroke();
        g.setStroke(new BasicStroke(lineWidth));
        g.setColor(c);
        g.drawRect(p.x(), p.y() + height, width, height);
        g.setStroke(stroke);
    }

    /**
     * Fills the HitBox.
     *
     * @param g the graphics object drawing
     * @param c the color of the HitBox
     * */
    public void fill(Graphics2D g, Color c)
    {
        g.setColor(c);
        g.fillRect(p.x(), p.y() + height, width, height);
    }
}