package se.skorup.main.gui.group.components;

import se.skorup.gui.components.buttons.Button;
import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A button used for displaying a person in the GUI.
 * */
public class PersonButton extends Button implements MouseListener
{
    public final int id;
    public int groupIndex;

    public boolean hoverEffect = true;
    private boolean selected;

    private Color foreground = Utils.FOREGROUND_COLOR;

    /**
     * Creates a new person button.
     *
     * @param text the text of the button.
     * @param id the id of the person.
     * @param groupIndex the index of the group that the person is in.
     * */
    public PersonButton(String text, int id, int groupIndex)
    {
        super(text, false);

        this.id = id;
        this.groupIndex = groupIndex;
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.addMouseListener(this);
    }

    /**
     * Swaps the selected status and rendering on the button.
     * */
    public void setSelected()
    {
        selected = true;
        super.setForeground(Utils.SELECTED_COLOR);
    }

    /**
     * Clears the selection status of the button
     * and resets the rendering.
     * */
    public void clearSelection()
    {
        selected = false;
        super.setForeground(foreground);
    }

    @Override
    public void setForeground(Color c)
    {
        this.foreground = c;
        super.setForeground(c);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (hoverEffect)
            super.setForeground(Utils.SELECTED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (hoverEffect)
            super.setForeground(selected ? Utils.SELECTED_COLOR : foreground);
    }
}
