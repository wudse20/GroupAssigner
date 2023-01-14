package se.skorup.games.base;

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
     * Draws the HitBox.
     *
     * @param g the graphics object drawing
     * @param c the color of the HitBox
     * */
    public void draw(Graphics2D g, Color c)
    {
        g.setColor(c);
        g.drawRect(p.x(), p.y() - height, width, height);
    }
}