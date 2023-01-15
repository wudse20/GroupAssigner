package se.skorup.games.breakout;

import se.skorup.API.util.Utils;
import se.skorup.games.base.HitBox;
import se.skorup.games.base.Pos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The paddle in the breakout game.
 * */
public class BreakoutPaddle extends BreakoutComponent
{
    /**
     * Creates a new BreakoutPaddle.
     *
     * @param p      the position.
     * @param width  the width of the component.
     * @param height the height of the component.
     * */
    protected BreakoutPaddle(Pos p, int width, int height)
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
