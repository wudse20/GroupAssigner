package se.skorup.snake;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
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
    private final SnakeFrame sf;
    private final Timer t;

    private final Deque<SnakeBlock> snake = new ArrayDeque<>();

    private final int blockSize = 20;
    private final int blocksX = SnakeFrame.WIDTH / blockSize;
    private final int blocksY = SnakeFrame.HEIGHT / blockSize;

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;
    private boolean hasStarted = false;

    private SnakeBlock head;
    private int appleX;
    private int appleY;

    /**
     * Creates a snake panel.
     *
     * @param sf the snake frame.
     * */
    public SnakePanel(SnakeFrame sf)
    {
        this.sf = sf;
        this.head = new SnakeBlock(blocksX / 2, blocksY / 2 - 1);

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
    }

    /**
     * The game itself.
     * */
    private void snakeGame()
    {
        direction = nextDirection;
        snake.removeLast(); // Removes the last.
        this.addNewHead();

        if (snake.contains(head))
        {
            gameOver("Self collision!");
            return;
        }
        else if (head.x() > blocksX - 2 || head.x() < 0 || head.y() > blocksY - 3 || head.y() < 0)
        {
            gameOver("Wall collision!");
            return;
        }

        snake.addFirst(head);

        if (head.equals(new SnakeBlock(appleX, appleY)))
        {
            DebugMethods.log("Ate apple!", DebugMethods.LogType.DEBUG);
            this.addNewHead();
            snake.addFirst(head);
            this.findApplePos();
            DebugMethods.log(
                "New apple generated: (%d, %d)".formatted(appleX, appleY),
                DebugMethods.LogType.DEBUG
            );
        }
    }

    /**
     * The game is over! :(
     * */
    private void gameOver(String message)
    {
        t.stop();
        DebugMethods.log(message, DebugMethods.LogType.DEBUG);
        sf.setVisible(false);
        JOptionPane.showMessageDialog(
            sf, "<html>GAME OVER!<br>Cause: %s<br>Score: %d</html>".formatted(message, (snake.size() - 4) * 100),
            "GAME OVER!", JOptionPane.INFORMATION_MESSAGE
        );
        sf.dispose();
    }

    /**
     * Adds a new head.
     * */
    private void addNewHead()
    {
        head = switch (direction) {
            case UP -> new SnakeBlock(
                head.x() + Direction.UP.xMod,
                head.y() + Direction.UP.yMod
            );
            case DOWN -> new SnakeBlock(
                head.x() + Direction.DOWN.xMod,
                head.y() + Direction.DOWN.yMod
            );
            case LEFT -> new SnakeBlock(
                head.x() + Direction.LEFT.xMod,
                head.y() + Direction.LEFT.yMod
            );
            case RIGHT -> new SnakeBlock(
                head.x() + Direction.RIGHT.xMod,
                head.y() + Direction.RIGHT.yMod
            );
        };
    }

    /**
     * Finds a new apple position.
     * */
    private void findApplePos()
    {
        do
        {
            appleX = new Random().nextInt(blocksX - 3) + 1;
            appleY = new Random().nextInt(blocksY - 4) + 1;
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

    private void drawScore(Graphics2D g)
    {
        g.setColor(Utils.SELECTED_COLOR);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        g.drawString("Score: %d".formatted((snake.size() - 5) * 100), 10, 30);
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld; // Use the newer graphics class.
        this.drawBackground(g);

        if (hasStarted)
        {
            this.drawApple(g);
            this.drawSnake(g);
            this.drawScore(g);
            return;
        }

        g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        g.setColor(Utils.SELECTED_COLOR);
        var width = g.getFontMetrics().stringWidth("Press 'Enter' to start!");
        g.drawString("Press 'Enter' to start!", (SnakeFrame.WIDTH - 7) / 2 - width / 2, SnakeFrame.HEIGHT / 2 - 25);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        var keyCode = e.getKeyCode();

        if (!hasStarted && keyCode == KeyEvent.VK_ENTER)
        {
            hasStarted = true;
            t.start();
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP && !direction.equals(Direction.DOWN))
            nextDirection = Direction.UP;
        else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN && !direction.equals(Direction.UP))
            nextDirection = Direction.DOWN;
        else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT && !direction.equals(Direction.RIGHT))
            nextDirection = Direction.LEFT;
        else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT && !direction.equals(Direction.LEFT))
            nextDirection = Direction.RIGHT;
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
