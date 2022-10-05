package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A panel that creates a graph of the data provided to it.
 * */
public class DataGraphPanel extends JPanel
{
    private int[] data;

    /** Margin top, bottom. */
    private int marginTB;

    /** Margin left, right. */
    private int marginLR;


    /**
     * Creates a new DataGraphPanel with
     * the data used to draw.
     * */
    public DataGraphPanel(int[] data)
    {
        this.data = data;
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

    private void drawAxes(Graphics2D g)
    {
        // Coloring
        g.setColor(Utils.FOREGROUND_COLOR);

        // Horizontal line
        g.drawLine(
            marginLR, this.getHeight() - marginTB,
            this.getWidth() - marginLR, this.getHeight() - marginTB
        );

        // Vertical line
        g.drawLine(marginLR, marginTB, marginLR, this.getHeight() - marginTB);
    }

    public void setData(int[] data)
    {
        this.data = data;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics gOld)
    {
        // Setup
        super.paintComponent(gOld);
        var g = (Graphics2D) gOld;

        // Set parameters
        this.marginTB = (int) (this.getHeight() * 0.1D);
        this.marginLR = (int) (this.getWidth() * 0.05D);

        // Drawing :)
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
