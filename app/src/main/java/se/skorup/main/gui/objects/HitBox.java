package se.skorup.main.gui.objects;

import java.awt.Color;
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
        var oldColor = g.getColor();

        g.setColor(Color.GREEN);
        g.drawRect(x, y, width, height);
        g.setColor(oldColor);
    }

    /**
     * Checks if the position is in the hit box.
     *
     * @param x the x position.
     * @param y the y position.
     * @return {@code true} iff x and y is inside
     *         the hit box.
     * */
    public boolean isCollision(int x, int y)
    {
        return this.x + this.width >= x  &&
               x >= this.x               &&
               this.y + this.height >= y &&
               this.y <= y;
    }
}
