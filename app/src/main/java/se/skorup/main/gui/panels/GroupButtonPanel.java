package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * The button panel used in the group generation GUI.
 * */
public class GroupButtonPanel extends JPanel
{
    /**
     * The buttons of this panel.
     * */
    public enum Buttons
    {
        CLOSE, HELP, LOAD, SAVE, PRINT,
        TO_FILE, CREATE, TO_DENYLIST
    }

    private final JButton btnClose = new JButton("Stäng");
    private final JButton btnHelp = new JButton("Hjälp");
    private final JButton btnLoad = new JButton("Ladda");
    private final JButton btnSave = new JButton("Spara");
    private final JButton btnToFile = new JButton("Spara som textfil");
    private final JButton btnCreate = new JButton("Genrera grupper");
    private final JButton btnToDenylist = new JButton("Till denylista");
    private final JButton btnPrint = new JButton("Skriv ut");

    /**
     * Creates a new GroupButtonPanel.
     * */
    public GroupButtonPanel()
    {
        this.setProperties();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        btnClose.setForeground(Utils.FOREGROUND_COLOR);
        btnClose.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnCreate.setForeground(Utils.FOREGROUND_COLOR);
        btnCreate.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnHelp.setForeground(Utils.FOREGROUND_COLOR);
        btnHelp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnSave.setForeground(Utils.FOREGROUND_COLOR);
        btnSave.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnToFile.setForeground(Utils.FOREGROUND_COLOR);
        btnToFile.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnToDenylist.setForeground(Utils.FOREGROUND_COLOR);
        btnToDenylist.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnPrint.setForeground(Utils.FOREGROUND_COLOR);
        btnPrint.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
    }

    /**
     * Populates the panel with the buttons, based
     * on the panel.
     *
     * TODO: Refactor
     *
     * @param panel the panel in use.
     * @throws IllegalArgumentException iff panel isn't instance of {@link SubgroupPanel SubgroupPanel}
     *                                  or {@link SubgroupSettingsPanel SubgroupSettingsPanel}.
     * */
    public void populateButtons(JPanel panel) throws IllegalArgumentException
    {
        this.removeAll();

        if (panel instanceof SubgroupSettingsPanel ||
            panel instanceof CalculatorPanel)
        {
            this.add(btnClose);
            this.add(btnHelp);
            this.add(btnCreate);
        }
        else if (panel instanceof SubgroupPanel)
        {
            this.add(btnClose);
            this.add(btnHelp);
            this.add(btnLoad);
            this.add(btnSave);
            this.add(btnToFile);
            this.add(btnPrint);
            this.add(btnToDenylist);
            this.add(btnCreate);
        }
        else
        {
            throw new IllegalArgumentException(
                "%s isn't an accepted class, for the panel. Only SubgroupPanel and SubgroupSettingsPanel are accepted."
                .formatted(panel.getClass())
            );
        }

        this.repaint();
        this.revalidate();
    }

    /**
     * Adds an action listener to a button.
     *
     * @param al the action listener to be added. If
     *           al is {@code null} then it will do
     *           nothing and just return.
     * @param button the button the action listener to
     *               be added.
     * */
    public void addActionListener(ActionListener al, Buttons button)
    {
        if (al == null)
            return;

        switch (button)
        {
            case HELP -> btnHelp.addActionListener(al);
            case CLOSE -> btnClose.addActionListener(al);
            case LOAD -> btnLoad.addActionListener(al);
            case SAVE -> btnSave.addActionListener(al);
            case CREATE -> btnCreate.addActionListener(al);
            case TO_FILE -> btnToFile.addActionListener(al);
            case TO_DENYLIST -> btnToDenylist.addActionListener(al);
            case PRINT -> btnPrint.addActionListener(al);
        }
    }
}
