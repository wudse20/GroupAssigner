package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.enums.State;
import se.skorup.main.gui.helper.hover.HoverEffectEnter;
import se.skorup.main.gui.helper.hover.HoverEffectExit;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A CSVLabel in the GUI for CSV editing.
 * */
public class CSVLabel extends JLabel implements MouseListener
{
    /** The background for persons. */
    public static final Color PERSON_COLOR = Utils.LIGHT_GREEN;

    /** The background for wishes. */
    public static final Color WISH_COLOR = Utils.LIGHT_BLUE;

    /** The background for skipping. */
    public static final Color SKIP_COLOR = Utils.LIGHT_RED;

    /** The background for unselected. */
    public static final Color UNSELECTED_COLOR = Color.WHITE;

    private final int x;
    private final int y;

    private final List<HoverEffectEnter<CSVLabel>> enterList = new ArrayList<>();
    private final List<HoverEffectExit<CSVLabel>> exitList = new ArrayList<>();
    private final List<ActionCallbackWithParam<CSVLabel>> callbacks = new ArrayList<>();

    private boolean shouldRunTimer = true;

    private State state = State.UNSELECTED;

    private Color savedBackground;
    private Timer t;

    /**
     * Creates a new CSV label with a label,
     * x and y position and a foreground and
     * background color.
     *
     * @param label the label of the label.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param background the background color.
     * @param foreground the foreground color.
     * */
    public CSVLabel(String label, int x, int y, Color background, Color foreground)
    {
        super(label, SwingConstants.CENTER);

        this.x = x;
        this.y = y;
        this.savedBackground = background;

        this.setForeground(foreground);
        this.setBackground(background);
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(foreground));
        this.addMouseListener(this);
    }

    /**
     * Getter for: x
     *
     * @return the x-coordinate.
     * */
    public int getXCoordinate()
    {
        return x;
    }

    /**
     * Getter for: y
     *
     * @return the y-coordinate.
     * */
    public int getYCoordinate()
    {
        return y;
    }

    /**
     * Adds a hover enter effect.
     *
     * @param enterEffect the effect to be added.
     * */
    public void addEnterEffect(HoverEffectEnter<CSVLabel> enterEffect)
    {
        if (enterEffect != null)
            enterList.add(enterEffect);
    }

    /**
     * Adds a hover exit effect.
     *
     * @param exitEffect the effect to be added.
     * */
    public void addExitEffect(HoverEffectExit<CSVLabel> exitEffect)
    {
        if (exitEffect != null)
            exitList.add(exitEffect);
    }

    /**
     * Adds a callback.
     *
     * @param callback the callback to be added.
     * */
    public void addActionCallback(ActionCallbackWithParam<CSVLabel> callback)
    {
        if (callback != null)
            callbacks.add(callback);
    }

    /**
     * Getter for: savedBackground.
     *
     * @return the color of the last background.
     * */
    public Color getSavedBackground()
    {
        return this.savedBackground;
    }

    /**
     * Setter for: savedBackground
     *
     * @param c the new background color.
     * */
    public void setSavedBackground(Color c)
    {
        if (c != null)
            this.savedBackground = c;
    }

    /**
     * Starts flashing the color of the button text - i.e. the
     * foreground color, with an interval of speedMs in millis seconds.
     * The colors it will flash with are the current base color and the
     * colors passed in the var args.
     *
     * @param speedMS the interval that the code will change color with.
     * @param flashColor the colors that will be cycled through.
     * @throws IllegalArgumentException iff speedMs < 25 or flashColor == {@code null} || flashColor.length = 0
     * */
    public void startFlashing(int speedMS, Color... flashColor) throws IllegalArgumentException
    {
        stopFlashing();

        if (speedMS < 25)
            throw new IllegalArgumentException("The passed value cannot be smaller than 25ms.");
        else if (flashColor == null || flashColor.length == 0)
            throw new IllegalArgumentException("You must pass at least on color.");

        final var i = new AtomicInteger();
        final var list = new ArrayList<>(Arrays.asList(flashColor)); // To prevent the immutable list.

        t = new Timer(speedMS, e -> {
            if (shouldRunTimer)
                this.setBackground(list.get(i.getAndIncrement() % list.size()));
        });

        t.start();
    }

    /**
     * Stops the flashing of the button.
     * */
    public void stopFlashing()
    {
        if (t != null)
        {
            t.stop();
            this.setBackground(savedBackground);
        }
    }

    /**
     * Setter for: State. <br><br>
     *
     * It will update the color of the label.
     *
     * @param newState the new state of the label.
     * */
    public void setState(State newState)
    {
        this.state = newState;
        this.setBackground(state.color);
        this.setSavedBackground(state.color);
    }

    /**
     * Getter for: State.
     *
     * @return the state of the label.
     * */
    public State getState()
    {
        return this.state;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e)
    {
        callbacks.forEach(c -> c.action(this));
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        enterList.forEach(ef -> ef.onEnter(this));
        shouldRunTimer = false;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        exitList.forEach(ef -> ef.onExit(this));
        shouldRunTimer = true;
    }

    @Override
    public int hashCode()
    {
        return getText().hashCode() + Utils.pow(x, 2) + Utils.pow(y, 3);
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof CSVLabel l &&
               l.x == x                &&
               l.y == y                &&
               l.getText().equals(getText());
    }

    @Override
    public String toString()
    {
        return getText();
    }
}
