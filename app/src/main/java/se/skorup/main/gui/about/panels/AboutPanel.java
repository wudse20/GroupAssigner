package se.skorup.main.gui.about.panels;

import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * The AboutPanel.
 * */
public class AboutPanel extends Panel
{
    /**
     * Creates a new AboutPanel.
     * */
    public AboutPanel()
    {
        super(new FlowLayout());

        var about = Localization.getValuef("ui.info.about", Utils.VERSION);
        var lbl = new Label("<html>%s</html>".formatted(about));
        lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
        this.add(lbl);
    }
}
