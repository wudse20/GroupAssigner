package se.skorup.games.breakout;

import se.skorup.API.util.Utils;
import se.skorup.games.base.Pos;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The panel that is the action breakout game.
 * */
public class BreakoutPanel extends JPanel implements KeyListener
{
    private static final int RIGHT_OFFSET = 17;

    private final BreakoutFrame bf;
    private final List<BreakoutBlock> blocks = new ArrayList<>();
    private final List<BreakoutComponent> balls = new ArrayList<>();
    private final List<Pos> directions = new ArrayList<>();
    private final BreakoutComponent paddle;
    private final boolean[] pressed = new boolean[2];

    private int id = -1;
    private Timer t;

    /**
     * Creates the breakout panel.
     *
     * @param bf the instance of the BreakoutFrame in use.
     * */
    public BreakoutPanel(BreakoutFrame bf)
    {
        this.bf = bf;
        this.paddle = new BreakoutPaddle(
            new Pos(
                bf.width() / 2 - 20,
                bf.height() - 75
            ),
            40, 10, id++
        );

        this.setUpBlocks();
        this.setUpBalls();

        this.t = new Timer(1000 / 60, (e) -> {
            this.movePaddle();
            this.handleBallCollisions();
            this.moveBall();
            this.revalidate();
            this.repaint();
        });

        this.t.start();
    }

    /**
     * Handles the all the ball collisions.
     * */
    private void handleBallCollisions()
    {
        var shouldSwap = new AtomicBoolean(false);
        var b = new HashSet<NewDirection>();
        // Collisions with block
        balls.forEach(ball -> new ArrayList<>(blocks).forEach(block -> {
            // There is a collision, now we have to figure out what the new direction is.
            if (block.isCollision(ball))
            {
                blocks.remove(block);
                shouldSwap.getAndSet(true);
                b.add(new NewDirection(
                    ball, block.nextDir(
                        ball.getPosition().x(),
                        ball.getPosition().y(),
                        ball.getWidth()
                )));
            }
        }));

        for (var ball : b)
        {
            ballCollision(ball.comp, ball.newDir);
        }

        // Collisions with paddle
        balls.forEach(ball -> {
            if (ball.isCollision(paddle))
            {
                ballCollision(ball, 1, -1);
            }
        });

        // Walls, roof and pit of death.
        new ArrayList<>(balls).forEach(ball -> {
            if (ball.getPosition().x() <= 0) // Left wall
            {
                ballCollision(ball, -1, 1);
            }

            if (ball.getPosition().x() + ball.getWidth() >= bf.width() - RIGHT_OFFSET) // Right wall
            {
                ballCollision(ball, -1, 1);
            }

            if (ball.getPosition().y() <= 0) // Roof
            {
                ballCollision(ball, 1, -1);
            }

            if (ball.getPosition().y() >= bf.height()) // Floor
            {
                balls.remove(ball);

                if (balls.isEmpty())
                    t.stop();
            }
        });
    }

    /**
     * Updates the direction on ballCollision by x, y.
     *
     * @param ball the ball to be affected.
     * @param x the direction difference in x.
     * @param y the direction difference in y.
     * */
    private void ballCollision(BreakoutComponent ball, int x, int y)
    {
        var index = balls.indexOf(ball);
        var newDir = directions.get(index).multiply(new Pos(x, y));
        directions.set(index, newDir);
    }

    /**
     * Updates the direction on ballCollision by p.
     *
     * @param ball the ball to be affected.
     * @param p the difference in direction.
     * */
    private void ballCollision(BreakoutComponent ball, Pos p)
    {
        ballCollision(ball, p.x(), p.y());
    }

    /**
     * Moves the ball.
     * */
    private void moveBall()
    {
        for (int i = 0; i < balls.size(); i++)
        {
            var movement = directions.get(i).multiply(2);
            balls.get(i).move(movement.x(), movement.y());
        }
    }

    /**
     * Handles the paddle movement.
     * */
    private void movePaddle()
    {
        if (pressed[0])
            paddle.move(-5, 0, 0, bf.width() - RIGHT_OFFSET);
        else if (pressed[1])
            paddle.move(5, 0, 0, bf.width() - RIGHT_OFFSET);
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
        var blockHeight = 12;

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
                        blockWidth, blockHeight, id++
                    ));
            }
        }
    }

    private void setUpBalls()
    {
        var ball = new BreakoutBall(new Pos(bf.width() / 2 - 5, bf.height() / 2), 10, 10, id++);
        balls.add(ball);
        directions.add(new Pos(1, 1));
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
        var g = (Graphics2D) gOld;

        // Background
        drawBackground(g);

        // Draw blocks
        drawBlocks(g);

        // Draw balls
        drawBall(g);

        // Draw paddle
        paddle.draw(g, Utils.LIGHT_BLUE);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            pressed[0] = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            pressed[1] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            pressed[0] = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            pressed[1] = false;
        }
    }

    private record NewDirection(BreakoutComponent comp, Pos newDir) {}
}
