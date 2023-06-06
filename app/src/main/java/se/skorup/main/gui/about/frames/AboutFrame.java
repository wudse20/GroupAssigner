package se.skorup.main.gui.about.frames;

import se.skorup.gui.components.Frame;
import se.skorup.gui.components.TabbedPane;
import se.skorup.main.gui.about.panels.AboutPanel;
import se.skorup.main.gui.about.panels.AttributionPanel;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * The frame that contains everything in the about section.
 * */
public class AboutFrame extends Frame
{
    private final TabbedPane tabs = new TabbedPane();

    private final AttributionPanel attrPanel = new AttributionPanel();
    private final AboutPanel abtPanel = new AboutPanel();

    /**
     * Creates a new AboutFrame.
     */
    public AboutFrame()
    {
        super("ui.title.about");
        super.init();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        cp.setLayout(new BorderLayout());
        tabs.add("ui.tab.about.about", abtPanel);
        tabs.add("ui.tab.about.attribution", attrPanel);
    }

    @Override
    protected void addComponents()
    {
        cp.add(tabs, BorderLayout.CENTER);
        this.pack();
    }
}
