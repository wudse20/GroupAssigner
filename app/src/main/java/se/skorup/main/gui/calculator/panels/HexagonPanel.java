package se.skorup.main.gui.calculator.panels;

import se.skorup.gui.components.Panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * A panel with hexagons.
 * */
public class HexagonPanel extends Panel
{
    /**
     * Creates a new panel.
     * */
    public HexagonPanel()
    {
        super(null);
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

        for (var i = 0; i < x0s.length; i++)
        {
            var x = new int[6];
            var y = new int[6];
            buildPolygon(x, y, x0s[i], y0s[i]);
//            g.setColor(new ArrayList<>(Parser.colorMap().values()).get(new Random().nextInt(Parser.colorMap().size())));
            g.setColor(new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)));
            g.fillPolygon(x, y, 6);
        }
    }
}
