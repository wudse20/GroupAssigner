package se.skorup.games.snake;

import se.skorup.games.base.GameFrame;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * A snake game that is cool and stuff.
 * */
public final class SnakeFrame extends GameFrame
{
    /** The width of the frame. */
    public static final int WIDTH = 600;

    /** The height of the frame. */
    public static final int HEIGHT = 600;

    /**
     * Creates a new snake game.
     * */
    public SnakeFrame()
    {
        super("Snake", WIDTH - 7, HEIGHT - 3);
        var sp = new SnakePanel(this);
        this.getContentPane().add(sp);
        this.addKeyListener(sp);
        this.center();
    }

    /**
     * Main-method used to only start the SnakeGame.
     * This game will exit the program on close.
     *
     * @param args the passed arguments.
     * */
    public static void main(String[] args)
    {
        var snake = new SnakeFrame();
        snake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
