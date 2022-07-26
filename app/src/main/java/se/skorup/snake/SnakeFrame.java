package se.skorup.snake;

import javax.swing.JFrame;
import java.awt.Dimension;

/**
 * A snake game that is cool and stuff.
 * */
public final class SnakeFrame extends JFrame
{
    /** The width of the frame. */
    public static final int WIDTH = 600;

    /** The height of the frame. */
    public static final int HEIGHT = 600;

    private final SnakePanel sp;

    /**
     * Creates a new snake game.
     * */
    public SnakeFrame()
    {
        super("Snake");

        this.sp = new SnakePanel(this);
        this.setResizable(false);
        this.setSize(new Dimension(WIDTH - 7, HEIGHT - 3)); // -7 & -3 was needed.
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
