package se.skorup.main.gui.objects;

import java.awt.Graphics2D;

/**
 * A hit box in the gui.
 *
 * @param x the x position.
 * @param y the y position.
 * @param width the width of the hit box.
 * @param height the height of the hit box.
 * */
public record HitBox(int x, int y, int width, int height)
{
    /**
     * Draws the hit box.
     *
     * @param g the graphics object drawing.
     * */
    public void drawHitBox(Graphics2D g)
    {
        g.drawRect(x, y, width, height);
    }

    /**
     * Checks if the position is in the hit box.
     *
     * @param x the x position.
     * @param y the y position.
     * */
    public boolean isCollison(int x, int y)
    {
        return (y >= this.y - this.height && y <= this.y) && (x <= this.x + this.width && x >= this.x);
    }
}
