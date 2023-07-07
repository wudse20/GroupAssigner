package se.skorup.gui.helper.ui;

import se.skorup.util.Utils;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A custom combobox UI to better fit the
 * aesthetic of the program.
 * */
public class MyComboBoxUI extends BasicComboBoxUI
{
    @Override
    public JButton createArrowButton()
    {
        return new CustomArrowButton();
    }

    @Override
    protected ListCellRenderer<Object> createRenderer()
    {
        return new CustomListCellRenderer();
    }

    @Override
    public void paintCurrentValueBackground(Graphics gOld, Rectangle bounds, boolean hasFocus)
    {
        super.paintCurrentValueBackground(gOld, bounds, hasFocus);
        var g = (Graphics2D) gOld;

        // Border
        g.setColor(Utils.FOREGROUND_COLOR);
        g.setStroke(new BasicStroke(1));
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Background
        g.setColor(Utils.BACKGROUND_COLOR);
        g.fillRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
    }

    private static class CustomArrowButton extends JButton
    {
        public CustomArrowButton()
        {
            super("<html></html>");
            setPreferredSize(new Dimension(20, 20));
            setFocusable(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics gOld)
        {
            super.paintComponent(gOld);

            var arrowSize = 8;
            var x = getWidth() / 2 - arrowSize / 2;
            var y = getHeight() / 2 - arrowSize / 2;

            var g = (Graphics2D) gOld;

            g.setColor(Utils.BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Utils.FOREGROUND_COLOR);
            g.drawRect(0, 0, getWidth(), getHeight());

            g.setColor(Utils.COMPONENT_BACKGROUND_COLOR);
            g.fillPolygon(
                new int[] { x, x + arrowSize / 2, x + arrowSize },
                new int[] { y, y + arrowSize, y },
                3
            );
        }
    }

    private static class CustomListCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus
        )
        {
            var renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (isSelected)
            {
                renderer.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
                renderer.setForeground(Utils.FOREGROUND_COLOR);
            }
            else
            {
                renderer.setBackground(Utils.BACKGROUND_COLOR);
                renderer.setForeground(Utils.FOREGROUND_COLOR);
            }

            return renderer;
        }
    }
}
