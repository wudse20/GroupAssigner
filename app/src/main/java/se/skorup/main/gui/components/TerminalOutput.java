package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import java.awt.Dimension;

/**
 * The component that holds the output of the console aka calculator.
 * */
public class TerminalOutput extends TerminalPane
{
    /**
     * Setup for the component.
     *
     * @param d the size of the component.
     * */
    public TerminalOutput(Dimension d)
    {
        super(d, false);
        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
    }

    @Override
    public boolean appendColoredString(String s)
    {
        var res = super.appendColoredString(s);
        this.appendLine();
        return res;
    }
}
