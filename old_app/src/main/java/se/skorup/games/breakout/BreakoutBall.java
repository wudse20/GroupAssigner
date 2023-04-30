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
    private Pos d;

    /**
     * Creates a new BreakoutBall.
     *
     * @param start the starting position of the ball.
     * @param width the width of the ball.
     * @param height the height of the ball.
     * @param id the id of the component.
     * */
    public BreakoutBall(Pos start, int width, int height, int id)
    {
        super(new Pos(start.x(), start.y() - height), width, height, id);
    }

    /**
     * Sets the direction of the ball.
     *
     * @param x the x direction.
     * @param y the y direction.
     * */
    public void setDirection(int x, int y)
    {
        this.d = new Pos(x, y);
    }

    /**
     * Sets the direction of the ball.
     *
     * @param d the new direction.
     * */
    public void setDirection(Pos d)
    {
        this.d = d;
    }

    /**
     * Getter for: direction
     *
     * @return the direction of the ball.
     * */
    public Pos getDirection()
    {
        return d;
    }

    @Override
    protected void design(Graphics2D g, Color c, HitBox hb)
    {
        g.setColor(c);
        g.fillOval(hb.p().x(), hb.p().y() + hb.height(), hb.width(), hb.height());
    }
}
