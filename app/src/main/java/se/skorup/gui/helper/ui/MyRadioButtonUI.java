package se.skorup.gui.helper.ui;

import se.skorup.util.Utils;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A custom UI for radio buttons to make them fit more with
 * the overall theme.
 * */
public class MyRadioButtonUI extends BasicRadioButtonUI
{
    @Override
    public void paint(Graphics gOld, JComponent c)
    {
        AbstractButton button = (AbstractButton) c;
        var model = button.getModel();
        var g = (Graphics2D) gOld;
        var dotDiameter = 7;
        var indicatorSize = 13;

        super.paint(gOld, c);

        // Border
        g.setColor(Utils.FOREGROUND_COLOR);
        g.fillOval(3, button.getHeight() / 2 - indicatorSize / 2 - 1, indicatorSize, indicatorSize);

        // Indicator
        g.setColor(Utils.COMPONENT_BACKGROUND_COLOR);
        g.fillOval(4, button.getHeight() / 2 - indicatorSize / 2, indicatorSize - 2, indicatorSize - 2);

        if (model.isSelected())
        {
            var dotX = indicatorSize / 2;
            var dotY = button.getHeight() / 2 - dotDiameter / 2 - 1;

            g.setColor(Utils.LIGHT_BLUE);
            g.fillOval(dotX, dotY, dotDiameter, dotDiameter);
        }
    }
}
