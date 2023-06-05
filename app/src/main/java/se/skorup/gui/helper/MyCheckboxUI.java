package se.skorup.gui.helper;

import se.skorup.util.Utils;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.LookAndFeel;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A UI for the checkbox that matches with the rest of the program.
 * */
public class MyCheckboxUI extends BasicCheckBoxUI
{
    @Override
    protected void installDefaults(AbstractButton btn)
    {
        super.installDefaults(btn);
        LookAndFeel.installProperty(btn, "opaque", false);
    }

    @Override
    protected void paintFocus(Graphics g, Rectangle t, Dimension d)
    {
        // Do nothing to prevent the focus rectangle from being painted
    }

    @Override
    public Icon getDefaultIcon()
    {
        return new CustomCheckboxIcon();
    }

    /** A custom icon for the custom checkbox. */
    private static class CustomCheckboxIcon implements Icon
    {
        @Override
        public void paintIcon(Component c, Graphics gOld, int x, int y)
        {
            var g = (Graphics2D) gOld;
            var btn = (AbstractButton) c;
            var model = btn.getModel();
            var boxSize = Math.min(this.getIconWidth(), this.getIconHeight());
            var xPos = btn.getInsets().left + 2;
            var yPos = (c.getHeight() - boxSize) / 2;

            // Background
            g.setColor(model.isEnabled() ? Utils.COMPONENT_BACKGROUND_COLOR : Utils.COMPONENT_BACKGROUND_COLOR.darker());
            g.fillRect(xPos, yPos, boxSize, boxSize);

            // Border
            g.setColor(model.isEnabled() ? Utils.FOREGROUND_COLOR : Utils.FOREGROUND_COLOR.darker());
            g.drawRect(xPos, yPos, boxSize, boxSize - 1);

            // Selection
            if (!model.isSelected())
                return;

            g.setColor(Utils.BLACK);
            g.fillRect(xPos + 2, yPos + 2, boxSize - 3, boxSize - 4);
        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight()
        {
            return 16;
        }
    }
}
