package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
        // Coloring
        g.setColor(Utils.MAIN_GROUP_1_COLOR); // TODO: Random color for each pillar.

        for (var i = 0; i < data.length; i++)
        {
            var x1 = spacer * (i + 1) + marginLR + pillarWidth * i;
            var x2 = x1 + pillarWidth;
            var y1 = this.getHeight() - marginTB;
            var y2 = y1 - (singleStepYDelta * data[i]);

            g.drawRect(x1, y1, x2 - x1, y2 - y1);
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
            var data = new int[] { 6, 3, 2, 1 };
            var p = new DataGraphPanel(data);
            var frame = new JFrame("Graph Demo");

            frame.add(p, BorderLayout.CENTER);
            frame.setSize(new Dimension(400, 300));
            frame.setVisible(true);
        });
    }
}
