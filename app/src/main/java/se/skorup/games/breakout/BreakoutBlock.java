package se.skorup.games.breakout;

import se.skorup.games.base.HitBox;
import se.skorup.games.base.Pos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A block in the breakout game.
 * */
public class BreakoutBlock extends BreakoutComponent
{
    private HitBox hb;
    private Color c;

    /**
     * Creates a new breakout block.
     *
     * @param width the width of the block.
     * @param height the height of the block.
     * @param id the id of the component.
     * @param c the color of the block.
     * */
    public BreakoutBlock(Pos p, int width, int height, int id, Color c)
    {
        super(p, width, height, id);
        this.c = c;
    }

    /**
     * Calculates and returns the next direction
     * of the ball.
     *
     * @param x the x pos of collision.
     * @param y the y pos of collision.
     * @param width the width of the component colliding
     * */
    public Pos nextDir(int x, int y, int width)
    {
        if (hb == null) // Should never happen!
            return new Pos(1, 1);

        var p = hb.p();
        var left = new HitBox(new Pos(p.x(), p.y()), -5, hb.height());
        var right = new HitBox(new Pos(p.x() + hb.width(), p.y()), 5, hb.height());

        if (left.isPosInBound(new Pos(x + width, y)))
            return new Pos(-1, 1);

        if (right.isPosInBound(new Pos(x, y)))
            return new Pos(-1, 1);

        return new Pos(1, -1);
    }

    @Override
    protected void design(Graphics2D g, Color unused, HitBox hb)
    {
        this.hb = hb; // Stores the hit box for later use.

        hb.fill(g, c);
        hb.draw(g, c.darker(), 2);
    }
}
