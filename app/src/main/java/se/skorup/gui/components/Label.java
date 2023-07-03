package se.skorup.gui.components;

import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.JLabel;

/**
 * A wrapper for the JLabel.
 * */
public class Label extends JLabel
{
    /**
     * Creates a label with some text.
     *
     * @param text the text of the label.
     * */
    public Label(String text)
    {
        this(text, false);
    }

    /**
     * Creates a label with some text.
     *
     * @param localizationKey the localization key used to find the text for the label.
     * @param useLocalization if {@code true} text will be treated as a localization key,
     *                        if {@code false} text will be treated as a plain string of text.
     * */
    public Label(String localizationKey, boolean useLocalization)
    {
        super(useLocalization ? Localization.getValue(localizationKey) : localizationKey);
        this.setForeground(Utils.FOREGROUND_COLOR);
    }
}
