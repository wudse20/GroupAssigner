package se.skorup.main.gui.components;

import java.awt.Dimension;

/**
 * The component that holds the output of the console aka calculator.
 * */
public final class TerminalOutput extends TerminalPane
{
    /**
     * Setup for the component.
     *
     * @param d the size of the component.
     * */
    public TerminalOutput(Dimension d)
    {
        super(d, false);
    }

    @Override
    public boolean appendColoredString(String s)
    {
        var res = super.appendColoredString(s);
        this.appendLine();
        return res;
    }
}
