package se.skorup.gui.helper.ui;

import se.skorup.util.Utils;

import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;

/**
 * A UI for the progressbar that matches with the rest of the program.
 * */
public class MyProgressBarUI extends BasicProgressBarUI
{
    @Override
    protected Color getSelectionBackground()
    {
        return Utils.FOREGROUND_COLOR; // string color over the background
    }

    @Override
    protected Color getSelectionForeground()
    {
        return Utils.BLACK; // string color over the foreground
    }
}
