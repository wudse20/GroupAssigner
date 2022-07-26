package se.skorup.snake;

import javax.swing.JFrame;
import java.awt.Dimension;

/**
 * A snake game that is cool and stuff.
 * */
public final class SnakeFrame extends JFrame
{
    /** The width of the frame. */
    public static final int WIDTH = 400;

    /** The height of the frame. */
    public static final int HEIGHT = 400;

    private final SnakePanel sp = new SnakePanel();

    /**
     * Creates a new snake game.
     * */
    public SnakeFrame()
    {
        super("Snake");

        this.setResizable(false);
        this.setSize(new Dimension(WIDTH - 5, HEIGHT - 2)); // -5 & -2 was needed.
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(sp);
        this.addKeyListener(sp);
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
