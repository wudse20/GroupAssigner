package se.skorup.main.gui.group.panels;

import se.skorup.gui.components.containers.Panel;
import se.skorup.util.Log;
import se.skorup.util.Utils;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

/**
 * A panel that holds a bar chart.
 * */
public class BarChartPanel extends Panel
{
    private final List<Integer> data;
    private final int totalElements;

    private final Color[] colors = {
        Color.GREEN, Color.BLUE, Color.MAGENTA,
        Color.ORANGE, Color.CYAN, Color.RED,
        Color.PINK, Color.YELLOW, new Color(108, 31, 153)
    };

    /** Margin top, bottom. */
    private int marginTB;

    /** Margin left, right. */
    private int marginLR;

    /** The space between the pillars. */
    private int spacer;

    /** The width of the pillars. */
    private int pillarWidth;

    /** The width of the axes. */
    private int axesWidth = 1;

    /** The delta between each step in pixels in y.*/
    private int singleStepYDelta;

    /**
     * Creates a new panel.
     *
     * @param data the data that will be drawn.
     * */
    public BarChartPanel(List<Integer> data)
    {
        super(new BorderLayout());
        this.data = data;
        this.totalElements = data.stream().reduce(0, Integer::sum);
    }

    /**
     * Draws the pillars.
     *
     * @param g the instance of the Graphics object drawing.
     * */
    private void drawPillars(Graphics2D g)
    {
        for (var i = 0; i < data.size(); i++)
        {
            // Coloring
            g.setColor(colors[i % colors.length]);

            // Calculating pillar values.
            var x1 = spacer * (i + 1) + marginLR + pillarWidth * i;
            var x2 = x1 + pillarWidth;
            var y1 = this.getHeight() - marginTB;
            var y2 = y1 - (singleStepYDelta * data.get(i));

            // Drawing pillar.
            g.drawRect(x1, y1, x2 - x1, y2 - y1);

            // Drawing values
            var yVal = Integer.toString(data.get(i));
            var xVal = Integer.toString(i);
            var fm = g.getFontMetrics();
            var fontHeight = fm.getHeight();
            var fontWidth = fm.stringWidth(yVal);

            // Coloring
            g.setColor(Utils.FOREGROUND_COLOR);

            // Drawing y-value string.
            g.drawString(yVal, x1 + (x2 - x1) / 2 - fontWidth / 2, y2 - fontHeight);

            // Updating data
            fontWidth = fm.stringWidth(xVal);

            // Drawing x-value string.
            g.drawString(xVal, x1 + (x2 - x1) / 2 - fontWidth / 2, y1 + fontHeight);
        }
    }

    /**
     * Draws the axes.
     *
     * @param g the instance of the graphics object drawing.
     * */
    private void drawAxes(Graphics2D g)
    {
        // Coloring and sizing
        g.setColor(Utils.FOREGROUND_COLOR);
        g.setStroke(new BasicStroke(axesWidth));

        // Horizontal line
        g.drawLine(
            marginLR, this.getHeight() - marginTB,
            this.getWidth() - marginLR, this.getHeight() - marginTB
        );

        // Vertical line
        g.drawLine(marginLR, marginTB, marginLR, this.getHeight() - marginTB);
    }


    @Override
    public void paintComponent(Graphics gOld)
    {
        Log.debug("Drawing!");
        var g = (Graphics2D) gOld;

        g.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

        // Set parameters
        this.marginTB = (int) (this.getHeight() * 0.1D);
        this.marginLR = (int) (this.getWidth() * 0.05D);
        this.spacer = (int) (this.getWidth() * 0.025D);
        this.pillarWidth = (this.getWidth() - 2 * marginLR - spacer * (data.size() + 1)) / data.size();
        this.singleStepYDelta = (this.getHeight() - 2 * marginTB) / totalElements;

        // Drawing :)
        drawPillars(g);
        drawAxes(g);
    }
}
