package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.GroupManager;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.List;

/**
 * The control panel at the top of the MainFrame.
 * */
public class ControlPanel extends JPanel
{
    /** The reference to the managers. */
    private final List<GroupManager> managers;

    /** The instance of the MainFrame in use. */
    private final MainFrame mf;

    /** The combo box holding the group managers. */
    private final JComboBox<GroupManager> cbManagers = new JComboBox<>();

    /** The layout of the panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);

    /**
     * Creates a new ControlPanel
     *
     * @param mf the instance of the mainframe.
     * @param managers the instance of the managers.
     * */
    public ControlPanel(MainFrame mf, List<GroupManager> managers)
    {
        this.mf = mf;
        this.managers = managers;

        this.setProperties();
        this.addComponents();
    }

    private void updateManagers()
    {
        // Clears the list.
        cbManagers.removeAllItems();

        // Adds the items.
        managers.forEach(cbManagers::addItem);

        // Adds dummy item used to detect clicks.
        cbManagers.addItem(new GroupManager("Lägg till en grupp"));
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(layout);

        cbManagers.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbManagers.setForeground(Utils.FOREGROUND_COLOR);

        this.updateManagers();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(cbManagers);
    }
}
