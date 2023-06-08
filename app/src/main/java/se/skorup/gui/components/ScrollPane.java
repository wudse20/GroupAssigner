package se.skorup.gui.components;

import se.skorup.gui.helper.ui.MyScrollBarUI;
import se.skorup.util.Utils;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Component;

/**
 * A wrapper for JScrollPane.
 * */
public class ScrollPane extends JScrollPane
{
    /**
     * Creates a new ScrollPane with a component
     * inside it.
     *
     * @param c the component to be contained by the ScrollPane.
     * */
    public ScrollPane(Component c)
    {
        super(c);

        this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        var scrBar = this.getVerticalScrollBar();
        scrBar.setUI(new MyScrollBarUI());
    }
}
