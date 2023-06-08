package se.skorup.main.gui.about.panels;

import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
        lbl.setForeground(Color.BLACK);
        this.add(lbl);
    }

    /**
     * Builds the polygon to be the correct size.
     * */
    private void buildPolygon(int[] x, int[] y, int x0, int y0)
    {
        for(int i = 0; i < 6; i++)
        {
            double angle = (2 * Math.PI) / 6;
            var v = i * angle;
            x[i] = x0 + (int) Math.round((getWidth() / 8D) * Math.cos(v));
            y[i] = y0 + (int) Math.round((getHeight() / 4D) * Math.sin(v));
        }
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        var g = (Graphics2D) gOld;

        var x0s = new int[] {
            (int) (getWidth() / 8D),
            (int) ((getWidth() / 8D) * 2.75),
            (int) ((getWidth() / 8D) * 4.5),
            (int) ((getWidth() / 8D) * 6.25)
        };

        var y0s = new int[] {
            (int) (getHeight() / 4D),
            (int) ((getHeight() / 4D) * 2),
            (int) (getHeight() / 4D),
            (int) ((getHeight() / 4D) * 2),
        };

        var colors = new Color[] { Color.ORANGE, Utils.GROUP_NAME_COLOR, Utils.SELECTED_COLOR, Utils.LIGHT_RED };
        for (var i = 0; i < x0s.length; i++)
        {
            var x = new int[6];
            var y = new int[6];
            buildPolygon(x, y, x0s[i], y0s[i]);
            g.setColor(colors[i]);
            g.fillPolygon(x, y, 6);
        }
    }
}
