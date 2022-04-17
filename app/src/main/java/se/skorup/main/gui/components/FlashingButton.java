package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A button that has the ability to flash the button text
 * in different colors.
 * */
public class FlashingButton extends JButton
{
    private Timer t;
    private Color lastColor;

    /**
     * Creates a new flashing button with the
     * provided argument as text.
     *
     * @param text the text of the button.
     * */
    public FlashingButton(String text)
    {
        super(text);

        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
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
        final var list = new ArrayList<>(Arrays.asList(flashColor)); // To prevent hte immutable list.
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
}
