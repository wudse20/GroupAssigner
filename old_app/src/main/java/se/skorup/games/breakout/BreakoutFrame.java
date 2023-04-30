package se.skorup.games.breakout;

import se.skorup.games.base.GameFrame;

/**
 * The frame containing the Breakout game.
 * */
public class BreakoutFrame extends GameFrame
{
    /**
     * Creates the breakout frame.
     * */
    public BreakoutFrame()
    {
        super("Breakout", 400, 300);
        var sp = new BreakoutPanel(this);
        this.getContentPane().add(sp);
        this.addKeyListener(sp);
        this.center();
    }

    /**
     * Main-method used to only start the BreakoutGame.
     * This game will exit the program on close.
     *
     * @param args the passed arguments.
     * */
    public static void main(String[] args)
    {
        var breakout = new BreakoutFrame();
        breakout.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
