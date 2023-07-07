package se.skorup.gui.components.progress;

import se.skorup.gui.helper.ui.MyProgressBarUI;
import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import java.awt.Font;

/**
 * A wrapper for the JProgressBar that styles it
 * according to the theme of this program.
 * */
public class ProgressBar extends JProgressBar
{
    public ProgressBar()
    {
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        this.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.LIGHT_BLUE);
        this.setUI(new MyProgressBarUI());
        this.setStringPainted(true);
    }
}
