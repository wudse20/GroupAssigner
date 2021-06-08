package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.ImmutableArray;
import se.skorup.API.Utils;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Person;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The panel with all the buttons!
 * */
public class ButtonPanel extends JPanel implements ActionListener
{
    /** The different button types. */
    private enum Buttons
    {
        /** The save button. */
        SAVE,
        /** The create groups button. */
        CREATE_GROUPS,
        /** The about button.*/
        ABOUT,
        /** The help button.*/
        HELP,
        /** The button for displaying different sizes. */
        SIZES
    }

    /** The instance of the MainFrame. */
    private final MainFrame mf;

    /** The layout of the panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);

    /** The button for saving. */
    private final JButton btnSave = new JButton("Spara");

    /** The button for creating groups. */
    private final JButton btnCreateGroup = new JButton("Skapa grupper");

    /** The button for the about information. */
    private final JButton btnAbout = new JButton("Om");

    /** The button for the help information. */
    private final JButton btnHelp = new JButton("Hjälp!");

    /** The button for displaying the even group constellations. */
    private final JButton btnSizes = new JButton("Jämna konstalationer");

    /**
     * Creates a new ButtonPanel.
     *
     * @param mf the instance of the MainFrame.
     * */
    public ButtonPanel(MainFrame mf)
    {
        this.mf = mf;

        this.setProperties();
        this.addComponents();
    }

    /** Adds the components. */
    private void addComponents()
    {
        DebugMethods.log("Adding components to button panel.", DebugMethods.LogType.DEBUG);

        this.add(btnSizes);
        this.add(btnHelp);
        this.add(btnAbout);
        this.add(btnCreateGroup);
        this.add(btnSave);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(layout);

        btnAbout.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAbout.setForeground(Utils.FOREGROUND_COLOR);
        btnAbout.setActionCommand(Buttons.ABOUT.toString());
        btnAbout.addActionListener(this);

        btnHelp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnHelp.setForeground(Utils.FOREGROUND_COLOR);
        btnHelp.setActionCommand(Buttons.HELP.toString());
        btnHelp.addActionListener(this);

        btnSave.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnSave.setForeground(Utils.FOREGROUND_COLOR);
        btnSave.setActionCommand(Buttons.SAVE.toString());
        btnSave.addActionListener(this);

        btnCreateGroup.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCreateGroup.setForeground(Utils.FOREGROUND_COLOR);
        btnCreateGroup.setActionCommand(Buttons.CREATE_GROUPS.toString());
        btnCreateGroup.addActionListener(this);

        btnSizes.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnSizes.setForeground(Utils.FOREGROUND_COLOR);
        btnSizes.setActionCommand(Buttons.SIZES.toString());
        btnSizes.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        var cmd = e.getActionCommand();

        if (cmd.equals(Buttons.ABOUT.toString()))
        {
            JOptionPane.showMessageDialog(mf, Utils.ABOUT, "Om programet!", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(Buttons.HELP.toString()))
        {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        }
        else if (cmd.equals(Buttons.CREATE_GROUPS.toString()))
        {
            if (mf.getCurrentGroup() != null)
            {
                SwingUtilities.invokeLater(() -> {
                    var gf = new GroupFrame(mf.getCurrentGroup());
                    mf.setVisible(false);
                    gf.addActionCallback(() -> mf.setVisible(true));
                });
            }
            else
            {
                JOptionPane.showMessageDialog(
                    mf, "Du kan inte skapa undergrupper, ty det inte är någon grupp vald.",
                    "Ingen grupp vald", JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else if (cmd.equals(Buttons.SAVE.toString()))
        {
            var res = mf.saveGroupManagers();

            if (res)
            {
                JOptionPane.showMessageDialog(
                    mf, "Du har sparat!",
                "Sparat!", JOptionPane.INFORMATION_MESSAGE
                );

                DebugMethods.log("Saved groups", DebugMethods.LogType.DEBUG);
            }
            else
            {
                JOptionPane.showMessageDialog(
                        mf, "Sparningen misslyckades!",
                        "Error!", JOptionPane.ERROR_MESSAGE
                );

                DebugMethods.log("Save was unsuccessful", DebugMethods.LogType.ERROR);
            }
        }
        else if (cmd.equals(Buttons.SIZES.toString()))
        {
            var size = mf.getCurrentGroup().getMemberCountOfRole(Person.Role.CANDIDATE);
            var res = new ArrayList<Integer>();

            for (int i = 1; i <= size; i++)
                if (size % i == 0)
                    res.add(i);

            JOptionPane.showMessageDialog(
                mf, "Jämna storlekar: %s".formatted(ImmutableArray.fromList(res).mkString(", ")),
                "Jämna storlekar", JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
