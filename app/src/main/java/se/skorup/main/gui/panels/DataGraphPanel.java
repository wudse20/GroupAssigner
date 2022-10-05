package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

/**
 * A panel that creates a graph of the data provided to it.
 * */
public class DataGraphPanel extends JPanel
{
    private int[] data;
    private int totalElements;

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

    private final Color[] colors = {
        Color.GREEN, Color.BLUE, Color.MAGENTA,
        Color.ORANGE, Color.CYAN, Color.RED,
        Color.PINK, Color.YELLOW, new Color(108, 31, 153)
    };

    /**
     * Creates a new DataGraphPanel with
     * the data used to draw.
     * */
    public DataGraphPanel(int[] data)
    {
        this.setData(data);
        this.setProperties();
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
    }

    /**
     * Draws the axes.
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

    private void drawPillars(Graphics2D g)
    {
        for (var i = 0; i < data.length; i++)
        {
            // Coloring
            g.setColor(colors[i % colors.length]);

            // Calculating pillar values.
            var x1 = spacer * (i + 1) + marginLR + pillarWidth * i;
            var x2 = x1 + pillarWidth;
            var y1 = this.getHeight() - marginTB;
            var y2 = y1 - (singleStepYDelta * data[i]);

            // Drawing pillar.
            g.drawRect(x1, y1, x2 - x1, y2 - y1);

            // Drawing values
            var yVal = Integer.toString(data[i]);
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
     * Setter for: data <br><br>
     * Causes a repaint.
     *
     * @param data the new data of the frame.
     * */
    public void setData(int[] data)
    {
        this.data = data;
        this.totalElements = Arrays.stream(data).sum();
        this.repaint();
    }

    /**
     * Resets the frame, i.e. draws nothing.
     * Causes a repaint.
     * */
    public void reset()
    {
        setData(null);
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        // Setup
        super.paintComponent(gOld);
        var g = (Graphics2D) gOld;
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

        // When empty do nothing.
        if (data == null || data.length == 0)
            return;

        // Set parameters
        this.marginTB = (int) (this.getHeight() * 0.1D);
        this.marginLR = (int) (this.getWidth() * 0.05D);
        this.spacer = (int) (this.getWidth() * 0.025D);
        this.pillarWidth = (this.getWidth() - 2 * marginLR - spacer * (data.length + 1)) / data.length;
        this.singleStepYDelta = (this.getHeight() - 2 * marginTB) / totalElements;

        // Drawing :)
        drawPillars(g);
        drawAxes(g);
    }

    /**
     * Starts a demo frame with some basic data.
     *
     * @param args the arguments passed to when starting
     *             the program.
     * */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            var data = new int[] { 12, 2, 2, 2, 0 };
            var p = new DataGraphPanel(data);
            var frame = new JFrame("Graph Demo");

            frame.add(p, BorderLayout.CENTER);
            frame.setSize(new Dimension(400, 300));
            frame.setVisible(true);
        });
    }
}
