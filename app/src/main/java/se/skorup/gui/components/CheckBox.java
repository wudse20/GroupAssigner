package se.skorup.gui.components;

import se.skorup.gui.helper.ui.MyCheckboxUI;
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
     * Creates a new checkbox with a label.
     *
     * @param localizationKey the key that is supposed to be the name of this box.
     * @param localization if {@code true} it will treat it as a localization key,
     *                     else if {@code false} it will treat it as a plain string.
     * */
    public CheckBox(String localizationKey, boolean localization)
    {
        this(localization ? Localization.getValue(localizationKey) : localizationKey);
    }

    /**
     * Creates a new checkbox with a label.
     *
     * @param label the label of the box.
     * */
    public CheckBox(String label)
    {
        super(label);
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setUI(new MyCheckboxUI());
    }
}
