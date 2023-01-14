package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;
import java.awt.Font;

/**
 * A quick wrapper class for the progress bar.
 * <br><br>
 * This will initialize with {@code super(0, 1_000_000);}.
 * */
public class PPMProgressBar extends JProgressBar
{
    /**
     * This will create a new PPMProgressBar.
     * */
    public PPMProgressBar()
    {
        super(0, 1_000_000);
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.MAIN_GROUP_1_COLOR);
        this.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        this.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground()
            {
                return Utils.FOREGROUND_COLOR; // string color over the background
            }

            protected Color getSelectionForeground()
            {
                return Utils.BACKGROUND_COLOR; // string color over the foreground
            }
        });
        this.setStringPainted(true);
    }
}
