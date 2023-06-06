package se.skorup.gui.components;

import se.skorup.gui.helper.MyTabbedPaneUI;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import java.awt.Component;

/**
 * A wrapper for the JTabbedPane.
 * */
public class TabbedPane extends JTabbedPane
{
    /**
     * Creates a new TabbedPane.
     * */
    public TabbedPane()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setUI(new MyTabbedPaneUI());
    }

    @Override
    public Component add(String localizationKey, Component c)
    {
        return super.add(Localization.getValue(localizationKey), c);
    }
}
