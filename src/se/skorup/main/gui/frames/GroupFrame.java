package se.skorup.main.gui.frames;

import se.skorup.API.Utils;
import se.skorup.main.groups.GroupCreator;
import se.skorup.main.groups.RandomGroupCreator;
import se.skorup.main.groups.WishlistGroupCreator;
import se.skorup.main.gui.panels.SettingPanel;
import se.skorup.main.manager.GroupManager;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

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

    /** The button group for the settings. */
    private final ButtonGroup bgSettings = new ButtonGroup();

    /** The radio button for pairing a group with the leaders. */
    private final JRadioButton btnLeaders = new JRadioButton("Para grupper med ledare");

    /** The panel for setting number of groups. */
    private final SettingPanel pNbrGroups = new SettingPanel("Antal grupper", null, 2);

    /** The panel for setting persons/group. */
    private final SettingPanel pNbrMembers = new SettingPanel("Antal personer/grupp", null, 2);

    /** The panel for the settings. */
    private final JPanel pSettings = new JPanel();

    /** The layout for the settings panel. */
    private final BoxLayout pSettingsLayout = new BoxLayout(pSettings, BoxLayout.Y_AXIS);

    /** This is the layout of the frame. */
    private final BorderLayout layout = new BorderLayout();

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
        this.setSize(new Dimension(400, 300));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(layout);

        cbCreator.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbCreator.setForeground(Utils.FOREGROUND_COLOR);
        cbCreator.addItem(randomCreator);
        cbCreator.addItem(wishlistCreator);

        pSettings.setBackground(Utils.BACKGROUND_COLOR);
        pSettings.setLayout(pSettingsLayout);

        btnLeaders.setForeground(Utils.FOREGROUND_COLOR);
        btnLeaders.setBackground(Utils.BACKGROUND_COLOR);

        pNbrGroups.setRadioSelected(true);

        bgSettings.add(pNbrGroups.getRadio());
        bgSettings.add(pNbrMembers.getRadio());
        bgSettings.add(btnLeaders);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pSettings.add(pNbrGroups);
        pSettings.add(pNbrMembers);
        pSettings.add(btnLeaders);

        this.add(pSettings, BorderLayout.PAGE_START);
    }
}
