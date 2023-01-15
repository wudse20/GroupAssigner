package se.skorup.games.breakout;

import se.skorup.games.base.HitBox;
import se.skorup.games.base.Pos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The class that is a breakout ball.
 * */
public class BreakoutBall extends BreakoutComponent
{
    /**
     * Creates a new BreakoutBall.
     *
     * @param start the starting position of the ball.
     * @param width the width of the ball.
     * @param height the height of the ball.
     * */
    public BreakoutBall(Pos start, int width, int height)
    {
        super(start, width, height);
    }

    @Override
    protected void design(Graphics2D g, Color c, HitBox hb)
    {
        g.setColor(c);
        g.fillOval(hb.p().x(), hb.p().y(), hb.width(), hb.height());
    }
}
