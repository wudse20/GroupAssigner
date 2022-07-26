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

    private Timer t;
    private SnakeBlock head;

    /**
     * Creates a snake panel.
     * */
    public SnakePanel()
    {
        head = new SnakeBlock(blocksX / 2, blocksY / 2 - 1);

        // Starting snake
        snake.addFirst(new SnakeBlock(blocksX / 2 - 2, blocksY / 2 - 1));
        snake.addFirst(new SnakeBlock(blocksX / 2 - 1, blocksY / 2 - 1));
        snake.addFirst(head);

        t = new Timer(1000 / 15, (e) -> {
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
        snake.removeLast(); // Removes the last.
        head = switch (direction) {
            case UP -> new SnakeBlock(head.x() + Direction.UP.xMod, head.y() + Direction.UP.yMod);
            case DOWN -> new SnakeBlock(head.x() + Direction.DOWN.xMod, head.y() + Direction.DOWN.yMod);
            case LEFT -> new SnakeBlock(head.x() + Direction.LEFT.xMod, head.y() + Direction.LEFT.yMod);
            case RIGHT -> new SnakeBlock(head.x() + Direction.RIGHT.xMod, head.y() + Direction.RIGHT.yMod);
        };

        snake.addFirst(head);
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

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld; // Use the newer graphics class.

        this.drawBackground(g);
        this.drawSnake(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        var keyChar = e.getKeyCode();

        if (keyChar == KeyEvent.VK_W || keyChar == KeyEvent.VK_UP && !direction.equals(Direction.DOWN))
            direction = Direction.UP;
        else if (keyChar == KeyEvent.VK_S || keyChar == KeyEvent.VK_DOWN && !direction.equals(Direction.UP))
            direction = Direction.DOWN;
        else if (keyChar == KeyEvent.VK_A || keyChar == KeyEvent.VK_LEFT && !direction.equals(Direction.RIGHT))
            direction = Direction.LEFT;
        else if (keyChar == KeyEvent.VK_D || keyChar == KeyEvent.VK_RIGHT && !direction.equals(Direction.LEFT))
            direction = Direction.RIGHT;

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
