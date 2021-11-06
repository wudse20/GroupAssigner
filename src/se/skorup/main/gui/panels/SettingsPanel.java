package se.skorup.main.gui.panels;

import javax.swing.JPanel;

import java.util.List;

/**
 * The
 * */
public sealed abstract class SettingsPanel extends JPanel permits SizeSettingsPanel
{
    public SettingsPanel()
    {
        super();
    }

    /**
     * Gets the user input from the program,
     * to determine sizes.
     *
     * @return a list containing the sizes of
     *         the groups.
     * */
    public abstract List<Integer> getUserInput();
}
