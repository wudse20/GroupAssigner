package se.skorup.games.base;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * A base frame for a Game.
 * */
public abstract class GameFrame extends JFrame
{
    private final int width;
    private final int height;

    /**
     * Creates the basic GameFrame and sets the settings.
     *
     * @param name the name/title of the frame.
     * @param height the height of the frame.
     * @param width the width of the frame.
     * */
    protected GameFrame(String name, int width, int height)
    {
        super(name);
        this.width = width;
        this.height = height;
        this.setResizable(false);
        this.setSize(new Dimension(width, height));
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Getter for: width
     *
     * @return the width of the frame.
     * */
    public int width()
    {
        return width;
    }

    /**
     * Getter for: height
     *
     * @return the height of the frame.
     * */
    public int height()
    {
        return height;
    }

    /**
     * Puts the frame at the center of the screen.
     * */
    public void center()
    {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.setLocation(
            dim.width / 2 - width / 2,
            dim.height / 2 - height / 2
        );
    }
}
