package se.skorup.games.breakout;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The panel that is the action breakout game.
 * */
public class BreakoutPanel extends JPanel implements KeyListener
{
    private final BreakoutFrame bf;

    /**
     * Creates the breakout panel.
     *
     * @param bf the instance of the BreakoutFrame in use.
     * */
    public BreakoutPanel(BreakoutFrame bf)
    {
        this.bf = bf;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
