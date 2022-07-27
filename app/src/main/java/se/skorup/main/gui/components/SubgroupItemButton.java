package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.helper.hover.HoverEffectEnter;
import se.skorup.main.gui.helper.hover.HoverEffectExit;

import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A button that has the ability to flash the button text
 * in different colors. It also has the ability to add a hover
 * effect.
 * */
public class SubgroupItemButton extends JButton implements MouseListener
{
    private Timer t;
    private Color lastColor;
    private HoverEffectEnter<SubgroupItemButton> hoverEnter;
    private HoverEffectExit<SubgroupItemButton> hoverExit;

    /**
     * Creates a new button with the
     * provided argument as text.
     *
     * @param text the text of the button.
     * */
    public SubgroupItemButton(String text)
    {
        this(text, Utils.FOREGROUND_COLOR);
    }

    public SubgroupItemButton(String text, Color foreground)
    {
        super(text);

        this.lastColor = foreground;
        this.setForeground(foreground);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        this.addMouseListener(this);
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
        this.lastColor = this.getForeground();

        if (speedMS < 25)
            throw new IllegalArgumentException("The passed value cannot be smaller than 25ms.");
        else if (flashColor == null || flashColor.length == 0)
            throw new IllegalArgumentException("You must pass at least on color.");

        final var i = new AtomicInteger();
        final var list = new ArrayList<>(Arrays.asList(flashColor)); // To prevent the immutable list.
        list.add(this.getForeground());

        t = new Timer(speedMS, (e) -> {
            this.setForeground(list.get(i.getAndIncrement() % list.size()));
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
            this.setForeground(lastColor);
        }
    }

    /**
     * Set the code to execute on hover exit.
     *
     * @param hoverExit the new effect that will happen
     *                  on hover exit.
     * */
    public void setHoverExit(HoverEffectExit<SubgroupItemButton> hoverExit)
    {
        this.hoverExit = hoverExit;
    }

    /**
     * Set the code to execute on hover enter.
     *
     * @param hoverEnter the new effect that will happen
     *                   on hover enter.
     * */
    public void setHoverEnter(HoverEffectEnter<SubgroupItemButton> hoverEnter)
    {
        this.hoverEnter = hoverEnter;
    }

    @Override
    public void setForeground(Color c)
    {
        this.lastColor = c;
        super.setForeground(c);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (hoverEnter != null)
            hoverEnter.onEnter(this);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (hoverExit != null)
            hoverExit.onExit(this);
    }
}
