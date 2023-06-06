package se.skorup.gui.components;

import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Container;

/**
 * A wrapper class for the JFrame
 * */
public abstract class Frame extends JFrame
{
    private static ImageIcon defaultIcon;

    /** The content pane of the frame. */
    protected final Container cp = this.getContentPane();

    /**
     * Creates a new Frame. With a localization key
     * that will be used for the title.
     *
     * @param localizationKey the key that is used for determining the title.
     * */
    protected Frame(String localizationKey)
    {
        super(Localization.getValue(localizationKey));
    }

    /**
     * Initializes the frame.
     * */
    protected final void init()
    {
        cp.setBackground(Utils.BACKGROUND_COLOR);
        this.setProperties();
        this.addComponents();
        this.setVisible(true);
        this.setIconImage(defaultIcon.getImage());
    }

    /** Sets the properties of the frame. */
    protected abstract void setProperties();

    /** Adds the components to the frame. */
    protected abstract void addComponents();

    /**
     * Sets the icon that will be the default icon
     * of all frames inheriting from frame.
     *
     * @param icon the icon that will be the default icon.
     * */
    public static void setDefaultIcon(ImageIcon icon)
    {
        Frame.defaultIcon = icon;
    }
}
