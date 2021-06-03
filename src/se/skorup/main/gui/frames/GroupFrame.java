package se.skorup.main.gui.frames;

import se.skorup.API.Utils;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.gui.panels.SettingPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * The frame used to create the groups.
 * */
public class GroupFrame extends JFrame
{
    /** The group manager in use. */
    private final GroupManager gm;

    /** The random group creator. */
    private final GroupCreator randomCreator;

    /** The wishlist group creator. */
    private final GroupCreator wishlistCreator;

    /** This frames's container. */
    private final Container cp = this.getContentPane();

    /** The list with the different creators. */
    private final JComboBox<GroupCreator> cbCreator = new JComboBox<>();

    /** The button to create groups. */
    private final JButton btnCreate = new JButton("Skapa undergrupper");

    /** The button for closing. */
    private final JButton btnClose = new JButton("Stäng");

    /** The button for printing. */
    private final JButton btnPrint = new JButton("Skriv ut");

    /** The button for saving. */
    private final JButton btnSave = new JButton("Spara");

    /** The button for loading. */
    private final JButton btnLoad = new JButton("Ladda");

    /** The button group for the settings. */
    private final ButtonGroup bgSettings = new ButtonGroup();

    /** The panel for pairing a group with the leaders. */
    private final SettingPanel pLeaders =
        new SettingPanel("Para grupper med ledare", null, 0, false);

    /** The panel for setting number of groups. */
    private final SettingPanel pNbrGroups =
        new SettingPanel("%-25s".formatted("Antal grupper"), null, 4, true);

    /** The panel for setting persons/group. */
    private final SettingPanel pNbrMembers =
        new SettingPanel("%-25s".formatted("Antal personer/grupp"), null, 4, true);

    /** The panel for the settings. */
    private final JPanel pSettings = new JPanel();

    /** The top of the panel. */
    private final JPanel pTop = new JPanel();

    /** The container panel of the checkbox. */
    private final JPanel pCBContainer = new JPanel();

    /** The container panel of the checkbox's container panel. */
    private final JPanel pCBContainerContainer = new JPanel();

    /** The JPanel for the buttons. */
    private final JPanel pButtons = new JPanel();

    /** The layout for the button panel. */
    private final FlowLayout pButtonsLayout = new FlowLayout(FlowLayout.RIGHT);

    /** The layout of the container panel for the checkbox's container panel. */
    private final BoxLayout pCBContainerContainerLayout = new BoxLayout(pCBContainerContainer, BoxLayout.Y_AXIS);

    /** The layout of the container panel for the checkbox. */
    private final FlowLayout pCBContainerLayout = new FlowLayout(FlowLayout.LEFT);

    /** The layout of the top panel. */
    private final BoxLayout pTopLayout = new BoxLayout(pTop, BoxLayout.X_AXIS);

    /** The layout for the settings panel. */
    private final BoxLayout pSettingsLayout = new BoxLayout(pSettings, BoxLayout.Y_AXIS);

    /** This is the layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

    /** A spacer in th gui. */
    private final JLabel lblSpacer1 = new JLabel("%-10s".formatted(" "));

    /** A spacer in th gui. */
    private final JLabel lblSpacer2 =
        new JLabel("<html><br><br><br></html>"); // Not hacky at all, good practice :)

    /**
     * Creates a new group frame.
     *
     * @param gm the group manager in use.
     * */
    public GroupFrame(GroupManager gm)
    {
        super("Skapa grupper");
        this.gm = gm;
        this.randomCreator = new RandomGroupCreator(gm);
        this.wishlistCreator = new WishlistGroupCreator(gm);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setSize(new Dimension(1200, 685));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        cbCreator.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbCreator.setForeground(Utils.FOREGROUND_COLOR);
        cbCreator.addItem(randomCreator);
        cbCreator.addItem(wishlistCreator);
        cbCreator.setAlignmentX(Component.LEFT_ALIGNMENT);

        pSettings.setBackground(Utils.BACKGROUND_COLOR);
        pSettings.setLayout(pSettingsLayout);
        pSettings.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pNbrGroups.setRadioSelected(true);

        bgSettings.add(pNbrGroups.getRadio());
        bgSettings.add(pNbrMembers.getRadio());
        bgSettings.add(pLeaders.getRadio());

        pTop.setBackground(Utils.BACKGROUND_COLOR);
        pTop.setLayout(pTopLayout);

        lblSpacer1.setAlignmentX(Component.CENTER_ALIGNMENT);

        pCBContainer.setBackground(Utils.BACKGROUND_COLOR);
        pCBContainer.setLayout(pCBContainerLayout);

        pCBContainerContainer.setBackground(Utils.BACKGROUND_COLOR);
        pCBContainerContainer.setLayout(pCBContainerContainerLayout);
        pCBContainerContainer.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pButtons.setBackground(Utils.BACKGROUND_COLOR);
        pButtons.setLayout(pButtonsLayout);

        btnClose.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnClose.setForeground(Utils.FOREGROUND_COLOR);
        btnClose.addActionListener((e) -> {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        });

        btnCreate.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnCreate.setForeground(Utils.FOREGROUND_COLOR);
        btnCreate.addActionListener((e) -> {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        });

        btnPrint.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnPrint.setForeground(Utils.FOREGROUND_COLOR);
        btnPrint.addActionListener((e) -> {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        });

        btnSave.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnSave.setForeground(Utils.FOREGROUND_COLOR);
        btnSave.addActionListener((e) -> {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        });

        btnLoad.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnLoad.setForeground(Utils.FOREGROUND_COLOR);
        btnLoad.addActionListener((e) -> {
            JOptionPane.showMessageDialog(
                this, "Not Yet Implemented",
                "Not Yet Implemented", JOptionPane.ERROR_MESSAGE
            );
        });
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pSettings.add(pNbrGroups);
        pSettings.add(pNbrMembers);
        pSettings.add(pLeaders);

        pCBContainer.add(cbCreator);

        pCBContainerContainer.add(lblSpacer2);
        pCBContainerContainer.add(pCBContainer);

        pTop.add(pCBContainerContainer);
        pTop.add(lblSpacer1);
        pTop.add(pSettings);

        pButtons.add(btnClose);
        pButtons.add(btnLoad);
        pButtons.add(btnSave);
        pButtons.add(btnPrint);
        pButtons.add(btnCreate);

        this.add(pTop, BorderLayout.PAGE_START);
        this.add(pButtons, BorderLayout.PAGE_END);
    }
}
