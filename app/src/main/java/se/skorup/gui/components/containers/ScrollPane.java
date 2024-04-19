package se.skorup.gui.components.containers;

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
        this(c, true);
    }

    /**
     * Creates a new ScrollPane with a component
     * inside it. You can also decide weather or not
     * a border will be display depending on the parameter
     * <i>useBorder</i>.
     *
     * @param c the component to be contained by the ScrollPane.
     * @param useBorder if {@code true} then the border will be
     *                  displayed, else it will be an empty
     *                  border.
     * */
    public ScrollPane(Component c, boolean useBorder)
    {
        super(c);

        this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.setBorder(
            useBorder ? BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR) : BorderFactory.createEmptyBorder()
        );

        var scrBar = this.getVerticalScrollBar();
        scrBar.setUI(new MyScrollBarUI());
    }
}
