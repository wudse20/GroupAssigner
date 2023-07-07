package se.skorup.gui.components.buttons;

import se.skorup.gui.helper.ui.MyRadioButtonUI;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.JRadioButton;

/**
 * A wrapper for the class JRadioButton.
 * */
public class RadioButton extends JRadioButton
{
    /**
     * Creates a new radio button with a
     * localization key to set the label
     * of the button.
     * */
    public RadioButton(String localizationKey)
    {
        super(Localization.getValue(localizationKey));
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setFocusPainted(false);
        this.setUI(new MyRadioButtonUI());
    }
}
