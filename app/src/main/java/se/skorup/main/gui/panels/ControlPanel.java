package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.FormsParser;
import se.skorup.API.util.MyFileReader;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.AddGroupFrame;
import se.skorup.main.gui.frames.CSVFrame;
import se.skorup.main.gui.frames.EditGroupFrame;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.manager.GroupManager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
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
        ADD, EDIT, DELETE, IMPORT_FORMS, IMPORT_CSV
    }

    private final List<GroupManager> managers;

    private final MainFrame mf;

    private final JComboBox<GroupManager> cbManagers = new JComboBox<>();

    private final JButton btnAdd = new JButton("Skapa en ny grupp");
    private final JButton btnEdit = new JButton("Ändra denna grupp");
    private final JButton btnDelete = new JButton("Ta bort denna grupp");
    private final JButton btnImportForms = new JButton("Importera från Google Forms");
    private final JButton btnImportCSV = new JButton("Importera från CSV-fil");

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

        btnImportForms.setForeground(Utils.FOREGROUND_COLOR);
        btnImportForms.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnImportForms.setActionCommand(Buttons.IMPORT_FORMS.toString());
        btnImportForms.addActionListener(this);

        btnImportCSV.setForeground(Utils.FOREGROUND_COLOR);
        btnImportCSV.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnImportCSV.setActionCommand(Buttons.IMPORT_CSV.toString());
        btnImportCSV.addActionListener(this);

        this.updateManagers();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        this.add(cbManagers);
        this.add(btnAdd);
        this.add(btnImportCSV);
        this.add(btnImportForms);
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
                var edit = new EditGroupFrame(gm);
                edit.addAddListener((event) -> {
                    event.frame().dispose();
                    mf.refreshSidePanel();
                    this.updateManagers();
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
        else if (cmd.equals(Buttons.IMPORT_FORMS.toString()))
        {
            var fc = new JFileChooser(".");
            fc.setMultiSelectionEnabled(true);
            var selection = fc.showDialog(mf, "Välj");

            if (selection == JFileChooser.APPROVE_OPTION)
            {
                var sb = new StringBuilder();
                for (var f : fc.getSelectedFiles())
                    sb.append(f.getName()).append(" + ");

                var result = new GroupManager(sb.substring(0, sb.length() - 3));

                try
                {
                    for (var str : MyFileReader.readFiles(fc.getSelectedFiles()))
                        FormsParser.parseFormData(str, result);
                }
                catch (IOException ex)
                {
                    DebugMethods.log(ex, DebugMethods.LogType.ERROR);
                    JOptionPane.showMessageDialog(
                        this,
                        "Kunde inte läsa fil!\nFelmeddelande%s".formatted(ex.getLocalizedMessage()),
                        "Kunde inte läsa fil", JOptionPane.ERROR_MESSAGE
                    );
                }

                mf.addGroupManager(result);
                this.updateManagers();
            }
        }
        else if (cmd.equals(Buttons.IMPORT_CSV.toString()))
        {
            var frame = new CSVFrame();
            frame.addActionCallback(gm -> {
                mf.addGroupManager(gm);
                this.updateManagers();
                frame.dispose();
            });
        }
    }
}
