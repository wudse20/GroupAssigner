package se.skorup.snake;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.manager.helper.SerializationManager;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * The snake panel that is the game itself.
 * */
public final class SnakePanel extends JPanel implements KeyListener
{
    /** The path of the score. */
    public static final String SNAKE_SCORE_PATH = Utils.getFolderName() + "snake/score.data";

    private final SnakeFrame sf;
    private final Timer t;

    private final Deque<SnakeBlock> snake = new ArrayDeque<>();

    private final int blockSize = 20;
    private int score = 0;
    private final int blocksX = SnakeFrame.WIDTH / blockSize;
    private final int blocksY = SnakeFrame.HEIGHT / blockSize;

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;
    private boolean hasStarted = false;

    private SnakeBlock head;
    private Score highscore;
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

        this.loadHighscore();

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
     * Loads the highscore.
     * */
    private void loadHighscore()
    {
        Score hs = null;

        try
        {
            hs = (Score) SerializationManager.deserializeObject(SNAKE_SCORE_PATH);
        }
        catch (IOException | ClassNotFoundException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
        }

        this.highscore = hs == null ? new Score(0) : hs;
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
            score += 100;
            this.addNewHead();
            snake.addFirst(head);
            this.findApplePos();
            DebugMethods.log(
                "New apple generated: (%d, %d)".formatted(appleX, appleY),
                DebugMethods.LogType.DEBUG
            );
        }

        score += 1;
    }

    /**
     * The game is over! :(
     * */
    private void gameOver(String message)
    {
        t.stop();
        DebugMethods.log(message, DebugMethods.LogType.DEBUG);
        sf.setVisible(false);

        if (highscore.isScoreBeaten(score))
        {
            highscore = new Score(score);
            JOptionPane.showMessageDialog(
                this, "<html>GAME OVER!<br>New highscore: %d<br>Cause: %s".formatted(score, message),
                "GAME OVER :(", JOptionPane.INFORMATION_MESSAGE
            );
        }
        else
        {
            JOptionPane.showMessageDialog(
                sf, "<html>GAME OVER!<br>Cause: %s<br>Score: %d</html>".formatted(message, score),
            "GAME OVER! :(", JOptionPane.INFORMATION_MESSAGE
            );
        }

        try
        {
            SerializationManager.createFileIfNotExists(new File(SNAKE_SCORE_PATH));
            SerializationManager.serializeObject(SNAKE_SCORE_PATH, highscore);
        }
        catch (IOException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
        }

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
        g.drawString("Score: %d".formatted(score), 10, 30);

        var hs = highscore.isScoreBeaten(score) ? score : highscore.score();
        g.drawString("Highscore: %d".formatted(hs), 10, 30 + g.getFontMetrics().getHeight());
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
            return;
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
            nextDirection = direction.equals(Direction.DOWN) ? Direction.DOWN : Direction.UP;
        else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
            nextDirection = direction.equals(Direction.UP) ? Direction.UP : Direction.DOWN;
        else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
            nextDirection = direction.equals(Direction.RIGHT) ? Direction.RIGHT : Direction.LEFT;
        else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
            nextDirection = direction.equals(Direction.LEFT) ? Direction.LEFT : Direction.RIGHT;
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
