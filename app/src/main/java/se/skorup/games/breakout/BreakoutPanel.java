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
    private final List<BreakoutBall> balls = new ArrayList<>();
    private final BreakoutComponent paddle;
    private final boolean[] pressed = new boolean[2];

    private int id = -1;
    private long frame = 0;
    private final Timer t;

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
        this.spawnBall();

        this.t = new Timer(1000 / 60, (e) -> {
            if (++frame % 120 == 0)
                spawnBall();

            this.movePaddle();
            this.handleBallCollisions();
            this.moveBalls();
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
            ball.comp.setDirection(ball.comp.getDirection().multiply(ball.newDir));

        // Collisions with paddle
        balls.forEach(ball -> {
            if (ball.isCollision(paddle))
                ball.setDirection(ball.getDirection().multiply(new Pos(1, -1)));
        });

        // Walls, roof and pit of death.
        for (var ball : new ArrayList<>(balls))
        {
            var dir = ball.getDirection();

            if (ball.getPosition().x() <= 0) // Left wall
            {
                ball.setDirection(dir.multiply(new Pos(-1, 1)));
                continue;
            }

            if (ball.getPosition().x() + ball.getWidth() >= bf.width() - RIGHT_OFFSET) // Right wall
            {
                ball.setDirection(dir.multiply(new Pos(-1, 1)));
                continue;
            }

            if (ball.getPosition().y() <= 0) // Roof
            {
                ball.setDirection(new Pos(1, -1));
                continue;
            }

            if (ball.getPosition().y() >= bf.height()) // Floor
            {
                balls.remove(ball);

                if (balls.isEmpty())
                    t.stop();
            }
        }
    }

    /**
     * Moves the ball.
     * */
    private void moveBalls()
    {
        for (BreakoutBall ball : balls)
        {
            var movement = ball.getDirection().multiply(2);
            ball.move(movement.x(), movement.y());
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

    /**
     * Spawns a new ball!
     * */
    private void spawnBall()
    {
        var ball = new BreakoutBall(new Pos(bf.width() / 2 - 5, bf.height() / 2), 10, 10, id++);
        ball.setDirection(1, 1);
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

    private record NewDirection(BreakoutBall comp, Pos newDir) {}
}
