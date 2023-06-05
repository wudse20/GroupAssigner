package se.skorup.gui.components;

import se.skorup.gui.helper.MyCheckboxUI;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

/**
 * A wrapper class for the checkbox.
 * */
public class CheckBox extends JCheckBox
{
    /**
     * Creates a new checkbox with a string from localization.
     *
     * @param localizationKey the key that is supposed to be the name of this button.
     * */
    public CheckBox(String localizationKey)
    {
        super(Localization.getValue(localizationKey));
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setUI(new MyCheckboxUI());
    }
}
