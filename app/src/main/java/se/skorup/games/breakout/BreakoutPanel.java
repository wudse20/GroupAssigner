package se.skorup.games.breakout;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.games.base.Pos;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel that is the action breakout game.
 * */
public class BreakoutPanel extends JPanel implements KeyListener
{
    private final BreakoutFrame bf;
    private final List<BreakoutComponent> blocks = new ArrayList<>();
    private final List<BreakoutComponent> balls = new ArrayList<>();
    private final BreakoutComponent paddle;

    /**
     * Creates the breakout panel.
     *
     * @param bf the instance of the BreakoutFrame in use.
     * */
    public BreakoutPanel(BreakoutFrame bf)
    {
        this.bf = bf;
        this.paddle = new BreakoutPaddle(new Pos(bf.width() / 2 - 20, bf.height() - 75), 40, 10);
        this.setUpBlocks();
        this.setUpBalls();
    }

    /**
     * Sets up the blocks.
     * */
    private void setUpBlocks()
    {
        var width = bf.width();
        var height = bf.height();
        var spaceTop = 10;
        var blockWidth = 20;
        var blockHeight = 10;

        var blocksWidth = width / blockWidth;
        var blocksHeight = ((height / 4) - spaceTop) / blockHeight;

        for (var i = 1; i < blocksWidth - 2; i++)
        {
            for (var ii = 0; ii < blocksHeight * 2 - 2; ii++)
            {
                blocks.add(
                    new BreakoutBlock(
                        new Pos(
                            3 + blockWidth * i,
                            spaceTop + blockHeight * ii
                        ),
                        blockWidth, blockHeight
                    ));
            }
        }

        DebugMethods.log("Finished setting up blocks", DebugMethods.LogType.DEBUG);
    }

    private void setUpBalls()
    {
        var ball = new BreakoutBall(new Pos(bf.width() / 2 - 5, bf.height() / 2), 10, 10);
        balls.add(ball);
    }

    /**
     * Draws the background.
     *
     * @param g The graphics object in use.
     * */
    private void drawBackground(Graphics2D g)
    {
        g.setColor(Utils.BACKGROUND_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Draws the blocks.
     *
     * @param g the graphics object used to draw.
     * */
    private void drawBlocks(Graphics2D g)
    {
        blocks.forEach(b -> b.draw(g, Color.blue));
    }

    /**
     * Draws the balls.
     *
     * @param g the graphics object used to draw.
     * */
    private void drawBall(Graphics2D g)
    {
        balls.forEach(b -> b.draw(g, Utils.LIGHT_RED));
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        DebugMethods.log("Started drawing", DebugMethods.LogType.DEBUG);
        var g = (Graphics2D) gOld;

        // Background
        drawBackground(g);

        // Draw blocks
        drawBlocks(g);

        // Draw balls
        drawBall(g);

        // Draw paddle
        paddle.draw(g, Utils.LIGHT_BLUE);

        DebugMethods.log("Finished drawing", DebugMethods.LogType.DEBUG);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
