package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.AddGroupFrame;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.GroupManager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Objects;

/**
 * The control panel at the top of the MainFrame.
 * */
public class ControlPanel extends JPanel implements ItemListener, ActionListener
{
    /** The enum for the different buttons. */
    private enum Buttons
    {
        /** The label for the add button. */
        ADD,

        /** The label for the edit button. */
        EDIT,

        /** The label for the delete button. */
        DELETE
    }

    /** The reference to the managers. */
    private final List<GroupManager> managers;

    /** The instance of the MainFrame in use. */
    private final MainFrame mf;

    /** The combo box holding the group managers. */
    private final JComboBox<GroupManager> cbManagers = new JComboBox<>();

    /** The button used in adding. */
    private final JButton btnAdd = new JButton("Skapa en ny grupp");

    /** The button used for editing. */
    private final JButton btnEdit = new JButton("Ändra denna grupp");

    /** The button for deleting a group. */
    private final JButton btnDelete = new JButton("Ta bort denna grupp");

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

        btnAdd.setForeground(Utils.FOREGROUND_COLOR);
        btnAdd.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAdd.setActionCommand(Buttons.ADD.toString());
        btnAdd.addActionListener(this);

        btnEdit.setForeground(Utils.FOREGROUND_COLOR);
        btnEdit.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnEdit.setActionCommand(Buttons.EDIT.toString());
        btnEdit.addActionListener(this);

        btnDelete.setForeground(Utils.FOREGROUND_COLOR);
        btnDelete.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDelete.setActionCommand(Buttons.DELETE.toString());
        btnDelete.addActionListener(this);

        this.updateManagers();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(cbManagers);
        this.add(btnAdd);
        this.add(btnEdit);
        this.add(btnDelete);
    }

    /**
     * Updates the group managers.
     * */
    public void updateManagers()
    {
        // Clears the list.
        cbManagers.removeAllItems();

        // Adds the items.
        managers.forEach(cbManagers::addItem);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        var index = cbManagers.getSelectedIndex();
        if (index != -1 && e.getStateChange() == ItemEvent.SELECTED)
        {
            ((GroupManager) Objects.requireNonNull(cbManagers.getSelectedItem()))
                    .getAllPersons().forEach(x -> DebugMethods.log(x.toString(), DebugMethods.LogType.DEBUG));

            mf.setCurrentGroupManager(index);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        var cmd = e.getActionCommand();

        if (cmd.equals(Buttons.ADD.toString()))
        {
            SwingUtilities.invokeLater(() -> {
                var add = new AddGroupFrame();
                add.addAddListener((event) -> {
                    mf.addGroupManager(event.result());
                    event.frame().dispose();
                    this.updateManagers();
                });
            });
        }
        else if (cmd.equals(Buttons.EDIT.toString()))
        {
            if (mf.getCurrentGroup() == null)
            {
                JOptionPane.showMessageDialog(
                    mf, "Det finns inga grupper",
                    "Inga grupper", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            final var gm = mf.getCurrentGroup();

            SwingUtilities.invokeLater(() -> {
                var add = new AddGroupFrame(gm);
                add.addAddListener((event) -> {
                    event.frame().dispose();
                    mf.refreshSidePanel();
                });
            });
        }
        else if (cmd.equals(Buttons.DELETE.toString()))
        {
            if (mf.getCurrentGroup() == null)
            {
                JOptionPane.showMessageDialog(
                    mf, "Det finns inga grupper att tabort",
                    "Inga grupper!", JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            var input =
                JOptionPane.showConfirmDialog(
                    mf, "Är du säker på att du vill tabort gruppen?",
                    "Tabort en grupp", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
                );

            if (input == JOptionPane.YES_OPTION)
                mf.removeCurrentGroupManager();
        }
    }
}
