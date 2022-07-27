package se.skorup.main.gui.components;

import se.skorup.main.gui.helper.hover.HoverEffectEnter;
import se.skorup.main.gui.helper.hover.HoverEffectExit;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A CSVLabel in the GUI for CSV editing.
 * */
public class CSVLabel extends JLabel implements MouseListener
{
    private final int x;
    private final int y;

    private final List<HoverEffectEnter<CSVLabel>> enterList = new ArrayList<>();
    private final List<HoverEffectExit<CSVLabel>> exitList = new ArrayList<>();
    private final List<ActionCallbackWithParam<CSVLabel>> callbacks = new ArrayList<>();

    private boolean selected = false;

    private Color savedBackground;

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
     * If {@code true} the label is selected,
     * else {@code false}.
     *
     * @return {@code true} if selected, else {@code false}.
     * */
    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e)
    {
        callbacks.forEach(c -> c.action(this));
        this.selected = !this.selected;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        enterList.forEach(ef -> ef.onEnter(this));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        exitList.forEach(ef -> ef.onExit(this));
    }
}
