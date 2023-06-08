package se.skorup.gui.components;

import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
        setProperties();
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the icon to be displayed on the button.
     * */
    public Button(ImageIcon icon)
    {
        super(icon);
        setProperties();
    }

    /**
     * Sets the properties of the button.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setFocusPainted(false);
        this.setBorder(
            BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR),
            BorderFactory.createLineBorder(Utils.COMPONENT_BACKGROUND_COLOR, 5)
        ));
    }
}
