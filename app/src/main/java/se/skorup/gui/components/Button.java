package se.skorup.gui.components;

import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * A wrapper class for the JButton.
 * */
public class Button extends JButton
{
    /**
     * Creates a new button with the text
     * from the localization tag.
     *
     * @param localizationTag the tag used to get the text.
     * */
    public Button(String localizationTag)
    {
        super(Localization.getValue(localizationTag));
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(
            BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
            BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 5)
        ));
    }
}
