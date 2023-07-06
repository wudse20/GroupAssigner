package se.skorup.gui.components;

import se.skorup.util.Utils;

import javax.swing.JPanel;
import java.awt.LayoutManager;

/**
 * A wrapper class for the JPanel.
 * */
public class Panel extends JPanel
{
    /**
     * Creates a new panel.
     *
     * @param layout The layout of the panel.
     * */
    public Panel(LayoutManager layout)
    {
        super(layout);
        this.setBackground(Utils.BACKGROUND_COLOR);
    }
}
