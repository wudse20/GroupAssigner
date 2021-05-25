package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The panel responsible for
 * */
public class PersonPanel extends JPanel
{
    /** The instance of the MainFrame in use. */
    private final MainFrame mf;

    /** The person that is being shown at the moment. */
    private Person p;

    /**
     * Creates a new person panel.
     *
     * @param mf the instance of the MainFrame in use.
     * */
    public PersonPanel(MainFrame mf)
    {
        this.mf = mf;

        this.setProperties();
        this.setup();
    }

    /**
     * Sets the properties of the panel and
     * its components.
     * */
    private void setProperties()
    {

    }

    /**
     * Setup for the panel
     * */
    private void setup()
    {
        // If there's no person,
        // then remove everything.
        if (p == null)
        {
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setBackground(Utils.BACKGROUND_COLOR);
        }
        else
        {
            this.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
            this.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        }
    }
}
