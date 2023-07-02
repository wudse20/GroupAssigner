package se.skorup.main.gui.main.frames;

import se.skorup.gui.components.Frame;
import se.skorup.main.gui.main.panels.MainPanel;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * The MainFrame of the program
 * */
public class MainFrame extends Frame
{
    private final MainPanel mp = new MainPanel(this);

    /**
     * Creates a new MainFrame. With a localization key
     * that will be used for the title.
     * */
    public MainFrame()
    {
        super("ui.title.main");
        super.init();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    protected void addComponents()
    {
        cp.add(mp, BorderLayout.CENTER);
        this.pack();
    }
}
