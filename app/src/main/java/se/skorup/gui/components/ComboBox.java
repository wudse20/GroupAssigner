package se.skorup.gui.components;

import se.skorup.gui.helper.ui.MyComboBoxUI;
import se.skorup.util.Utils;

import javax.swing.JComboBox;

/**
 * A wrapper for the JComboBox class.
 *
 * @param <E> the type of the list items.
 * */
public class ComboBox<E> extends JComboBox<E>
{
    /**
     * Creates a new Combobox.
     * */
    public ComboBox()
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setUI(new MyComboBoxUI());
    }
}
