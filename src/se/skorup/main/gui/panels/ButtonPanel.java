package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The panel with all the buttons!
 * */
public class ButtonPanel extends JPanel implements ActionListener
{
    /** The different button types. */
    private enum Buttons
    {
        SAVE, PRINT, ABOUT, HELP
    }

    /** The instance of the MainFrame. */
    private final MainFrame mf;

    /** The layout of the panel. */
    private final FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);

    /** The button for saving. */
    private final JButton btnSave = new JButton("Spara");

    /** The button for printing. */
    private final JButton btnPrint = new JButton("Skriv ut");

    /** The button for the about information. */
    private final JButton btnAbout = new JButton("Om");

    /** The button for the help information. */
    private final JButton btnHelp = new JButton("Hjälp!");

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

        this.add(btnHelp);
        this.add(btnAbout);
        this.add(btnPrint);
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

        btnPrint.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPrint.setForeground(Utils.FOREGROUND_COLOR);
        btnPrint.setActionCommand(Buttons.PRINT.toString());
        btnPrint.addActionListener(this);
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
        else if (cmd.equals(Buttons.PRINT.toString()))
        {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        }
        else if (cmd.equals(Buttons.SAVE.toString()))
        {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
