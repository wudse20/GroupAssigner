package se.skorup.games.breakout;

import se.skorup.games.base.HitBox;
import se.skorup.games.base.Pos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The base class for everything breakout.
 * */
public abstract class BreakoutComponent
{
    private final int id;

    private HitBox hb;

    /**
     * Creates a new BreakoutComponent.
     *
     * @param p the position.
     * @param width the width of the component.
     * @param height the height of the component.
     * @param id the id of the component.
     * */
    protected BreakoutComponent(Pos p, int width, int height, int id)
    {
        this.hb = new HitBox(p, width, height);
        this.id = id;
    }

    /**
     * The design of the component. This where you tell the game
     * how to draw the component.
     *
     * @param g the instance of the graphics object drawing.
     * @param c the color of the block.
     * @param hb the HitBox of the component.
     * */
    protected abstract void design(Graphics2D g, Color c, HitBox hb);

    /**
     * Checks if a position collides with us.
     *
     * @param p the position to be tested.
     * @return {@code true} if p &#8712; this <br>
     *         {@code false} if p &#8713; this
     * */
    public final boolean isCollision(Pos p)
    {
        return hb.isPosInBound(p);
    }

    /**
     * Checks if a BreakoutComponent collides with us.
     *
     * @param comp the BreakoutComponent to be tested.
     * @return {@code true} if p &#8712; this <br>
     *         {@code false} if p &#8713; this
     * */
    public final boolean isCollision(BreakoutComponent comp)
    {
        return hb.isHitBoxOverlapping(comp.hb);
    }

    /**
     * Draws the component.
     *
     * @param g the graphics object drawing.
     * @param c the color of the component.
     * */
    public final void draw(Graphics2D g, Color c)
    {
        design(g, c, hb);
    }

    /**
     * Moves the platform.
     *
     * @param dx the x distance moved.
     * @param dy the y distance moved.
     * */
    public final void move(int dx, int dy)
    {
        this.hb = new HitBox(new Pos(hb.p().x() + dx, hb.p().y() + dy), hb.width(), hb.height());
    }

    /**
     * Moves the component, but clamped between minX and maxX.
     *
     * @param dx the x distance to be moved.
     * @param dy the y distance to be moved.
     * @param minX the minimum x allowed.
     * @param maxX the maximum x allowed.
     * */
    public final void move(int dx, int dy, int minX, int maxX)
    {
        if (hb.p().x() + dx >= maxX - hb.width())
        {
            move(0, dy);
            this.hb = new HitBox(new Pos(maxX - hb.width(), hb.p().y()), hb.width(), hb.height());
        }
        else if (hb.p().x() + dx <= minX)
        {
            move(-hb.p().x(), dy);
        }
        else
        {
            move(dx, dy);
        }
    }

    /**
     * Getter for: position
     *
     * @return the position of the left corner of the component.
     * */
    public final Pos getPosition()
    {
        return hb.p();
    }

    /**
     * Getter for: width
     *
     * @return the width of the component.
     * */
    public int getWidth()
    {
        return hb.width();
    }

    /**
     * Getter for: height
     *
     * @return the height of the component.
     * */
    public int getHeight()
    {
        return hb.height();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof BreakoutComponent bc &&
               id == bc.id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
