package se.skorup.snake;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * The snake panel that is the game itself.
 * */
public final class SnakePanel extends JPanel implements KeyListener
{
    private final Deque<SnakeBlock> snake = new ArrayDeque<>();

    private final int blockSize = 20;
    private final int blocksX = SnakeFrame.WIDTH / blockSize;
    private final int blocksY = SnakeFrame.HEIGHT / blockSize;

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;

    private Timer t;
    private SnakeBlock head;
    private int appleX;
    private int appleY;

    /**
     * Creates a snake panel.
     * */
    public SnakePanel()
    {
        head = new SnakeBlock(blocksX / 2, blocksY / 2 - 1);

        // Starting snake
        snake.addFirst(new SnakeBlock(blocksX / 2 - 4, blocksY / 2 - 1));
        snake.addFirst(new SnakeBlock(blocksX / 2 - 3, blocksY / 2 - 1));
        snake.addFirst(new SnakeBlock(blocksX / 2 - 2, blocksY / 2 - 1));
        snake.addFirst(new SnakeBlock(blocksX / 2 - 1, blocksY / 2 - 1));
        snake.addFirst(head);

        this.findApplePos();

        t = new Timer(1000 / 24, (e) -> {
            snakeGame();
            this.repaint();
        });

        t.start();
    }

    /**
     * The game itself.
     * */
    private void snakeGame()
    {
        direction = nextDirection;
        snake.removeLast(); // Removes the last.
        this.addNewHead();

        if (head.equals(new SnakeBlock(appleX, appleY)))
        {
            DebugMethods.log("Ate apple!", DebugMethods.LogType.DEBUG);
            this.addNewHead();
            this.findApplePos();
            DebugMethods.log(
                "New apple generated: (%d, %d)".formatted(appleX, appleY),
                DebugMethods.LogType.DEBUG
            );
        }
    }

    /**
     * Adds a new head.
     * */
    private void addNewHead()
    {
        head = switch (direction) {
            case UP -> new SnakeBlock(
                    Math.floorMod(head.x() + Direction.UP.xMod, blocksX),
                    Math.floorMod(head.y() + Direction.UP.yMod, blocksY)
            );
            case DOWN -> new SnakeBlock(
                    Math.floorMod(head.x() + Direction.DOWN.xMod, blocksX),
                    Math.floorMod(head.y() + Direction.DOWN.yMod, blocksY)
            );
            case LEFT -> new SnakeBlock(
                    Math.floorMod(head.x() + Direction.LEFT.xMod, blocksX),
                    Math.floorMod(head.y() + Direction.LEFT.yMod, blocksY)
            );
            case RIGHT -> new SnakeBlock(
                    Math.floorMod(head.x() + Direction.RIGHT.xMod, blocksX),
                    Math.floorMod(head.y() + Direction.RIGHT.yMod, blocksY)
            );
        };

        snake.addFirst(head);
    }

    /**
     * Finds a new apple position.
     * */
    private void findApplePos()
    {
        do
        {
            appleX = new Random().nextInt(blocksX);
            appleY = new Random().nextInt(blocksY);
        } while (snake.contains(new SnakeBlock(appleX, appleY)));
    }

    /**
     * Draws the background.
     *
     * @param g the Graphics2D object in use to draw.
     * */
    private void drawBackground(Graphics2D g)
    {
        g.setColor(Utils.BACKGROUND_COLOR);
        g.fillRect(0, 0, SnakeFrame.WIDTH, SnakeFrame.HEIGHT);
    }

    /**
     * Draws the snake.
     *
     * @param g the Graphics2D object in use to draw.
     * */
    private void drawSnake(Graphics2D g)
    {
        var i = 0;
        for (var b : snake)
        {
            g.setColor(i++ % 2 == 0 ? Utils.MAIN_GROUP_1_COLOR : Utils.MAIN_GROUP_2_COLOR);
            g.fillRect(b.x() * blockSize, b.y() * blockSize, blockSize, blockSize);
        }
    }

    /**
     * Draws the apple.
     *
     * @param g the Graphics2D object in use to draw.
     * */
    private void drawApple(Graphics2D g)
    {
        g.setColor(Utils.GROUP_NAME_COLOR);
        g.fillRect(appleX * blockSize, appleY * blockSize, blockSize, blockSize);
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld; // Use the newer graphics class.

        this.drawBackground(g);
        this.drawApple(g);
        this.drawSnake(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        var keyChar = e.getKeyCode();

        if (keyChar == KeyEvent.VK_W || keyChar == KeyEvent.VK_UP && !direction.equals(Direction.DOWN))
            nextDirection = Direction.UP;
        else if (keyChar == KeyEvent.VK_S || keyChar == KeyEvent.VK_DOWN && !direction.equals(Direction.UP))
            nextDirection = Direction.DOWN;
        else if (keyChar == KeyEvent.VK_A || keyChar == KeyEvent.VK_LEFT && !direction.equals(Direction.RIGHT))
            nextDirection = Direction.LEFT;
        else if (keyChar == KeyEvent.VK_D || keyChar == KeyEvent.VK_RIGHT && !direction.equals(Direction.LEFT))
            nextDirection = Direction.RIGHT;

        DebugMethods.log("New direction: %s".formatted(direction), DebugMethods.LogType.DEBUG);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private enum Direction
    {
        UP(0 , -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final int xMod;
        public final int yMod;

        Direction(int xMod, int yMod)
        {
            this.xMod = xMod;
            this.yMod = yMod;
        }
    }
}
