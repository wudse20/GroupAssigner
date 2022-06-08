package se.skorup.main.gui.components;

import se.skorup.API.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A button in the shape of a hexagon.
 * */
public class HexButton extends JButton implements MouseListener
{
    private final int n = 6;
    private final int[] x = new int[n];
    private final int[] y = new int[n];

    private Polygon polygon;

    /**
     * Creates a new HexButton
     *
     * @param label the label of the button.
     * */
    public HexButton(String label)
    {
        super(label);

        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.addMouseListener(this);
    }

    /**
     * Builds the polygon to be the correct size.
     * */
    private void buildPolygon()
    {
        var x0 = getSize().width / 2;
        var y0 = getSize().height / 2;

        for(int i = 0; i < n; i++)
        {
            double angle = (2 * Math.PI) / n;
            var v = i * angle;
            x[i] = x0 + (int) Math.round((getWidth() / 2d) * Math.cos(v));
            y[i] = y0 + (int) Math.round((getHeight() / 2d) * Math.sin(v));
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        buildPolygon();
        g.setColor(this.getBackground());
        g.fillPolygon(x, y, n);
        super.paintComponent(g);
    }

    @Override
    public void paintBorder(Graphics g)
    {
        g.setColor(borderColor);
        buildPolygon();
        g.drawPolygon(x, y, n);
    }

    @Override
    public boolean contains(int x1, int y1)
    {
        if (polygon == null || !polygon.getBounds().equals(getBounds()))
        {
            buildPolygon();
            polygon = new Polygon(x,y,n);
        }

        return polygon.contains(x1, y1);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
}
