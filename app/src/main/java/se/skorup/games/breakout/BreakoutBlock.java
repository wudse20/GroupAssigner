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
    /**
     * Creates a new breakout block.
     *
     * @param width the width of the block.
     * @param height the height of the block.
     * */
    public BreakoutBlock(Pos p, int width, int height)
    {
        super(p, width, height);
    }

    @Override
    protected void design(Graphics2D g, Color c, HitBox hb)
    {
        hb.fill(g, c);
        hb.draw(g, c.darker(), 2);
    }
}
