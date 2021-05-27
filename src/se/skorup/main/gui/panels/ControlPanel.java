package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.AddGroupFrame;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.GroupManager;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Objects;

/**
 * The control panel at the top of the MainFrame.
 * */
public class ControlPanel extends JPanel implements ItemListener
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

    /**
     * Updates the group managers.
     * */
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
        cbManagers.addItemListener(this);

        this.updateManagers();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(cbManagers);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        var index = cbManagers.getSelectedIndex();
        if (index != -1 && e.getStateChange() == ItemEvent.SELECTED)
        {
            // Check for adding
            if (index == managers.size())
            {
                SwingUtilities.invokeLater(() -> {
                    var add = new AddGroupFrame();
                });
                return;
            }

            ((GroupManager) Objects.requireNonNull(cbManagers.getSelectedItem()))
                    .getAllPersons().forEach(x -> DebugMethods.log(x.toString(), DebugMethods.LogType.DEBUG));

            mf.setCurrentGroupManager(index);
        }
    }
}
