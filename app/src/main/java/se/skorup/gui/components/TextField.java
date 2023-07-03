package se.skorup.gui.components;

import se.skorup.util.Utils;

import javax.swing.JTextField;

/**
 * A wrapper for the textfield.
 * */
public class TextField extends JTextField
{
    /**
     * Creates a new TextField.
     *
     * @param columns the number of columns in the textfield.
     * */
    public TextField(int columns)
    {
        super(columns);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Clears the textfield, i.e., removes all text.
     * */
    public void clear()
    {
        this.setText("");
    }
}
