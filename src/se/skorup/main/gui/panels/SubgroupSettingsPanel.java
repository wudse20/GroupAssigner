package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.groups.creators.AlternateWishlistGroupCreator;
import se.skorup.main.groups.creators.GroupCreator;
import se.skorup.main.groups.creators.RandomGroupCreator;
import se.skorup.main.groups.creators.WishlistGroupCreator;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/**
 * The settings for the subgroup generation.
 * */
public final class SubgroupSettingsPanel extends SettingsPanel
{
    private final GroupFrame gf;

    private final JRadioButton radioMainGroup1 = new JRadioButton("Huvudgrupp 1");
    private final JRadioButton radioMainGroup2 = new JRadioButton("Huvudgrupp 2");

    private final ButtonGroup bgMainGroups = new ButtonGroup();

    private final JCheckBox boxOneMainGroup = new JCheckBox("Använd en huvudgrupp");
    private final JCheckBox boxMainGroups = new JCheckBox("Använd Huvudgrupper");

    private final JComboBox<GroupCreator> cbCreators = new JComboBox<>();

    private final JPanel pMainGroups = new JPanel();
    private final SettingsPanel pSettings;
    private final JPanel pGroupCreator = new JPanel();

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupSettingsPanel(GroupFrame gf)
    {
        super();

        this.gf = gf;

        pSettings = new SizeSettingsPanel(gf);

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
        this.setLayout(new BorderLayout());

        var mainGroupsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Huvudgrupper"
        );

        mainGroupsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        var groupCreatorBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Gruppgenerator"
        );

        groupCreatorBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        pMainGroups.setLayout(new BoxLayout(pMainGroups, BoxLayout.Y_AXIS));
        pMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        pMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        pMainGroups.setBorder(mainGroupsBorder);

        pGroupCreator.setLayout(new FlowLayout(FlowLayout.CENTER));
        pGroupCreator.setForeground(Utils.FOREGROUND_COLOR);
        pGroupCreator.setBackground(Utils.BACKGROUND_COLOR);
        pGroupCreator.setBorder(groupCreatorBorder);

        cbCreators.setForeground(Utils.FOREGROUND_COLOR);
        cbCreators.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        cbCreators.addItem(new RandomGroupCreator(gf.getManager()));
        cbCreators.addItem(new WishlistGroupCreator(gf.getManager()));
        cbCreators.addItem(new AlternateWishlistGroupCreator(gf.getManager()));

        boxOneMainGroup.setBackground(Utils.BACKGROUND_COLOR);
        boxOneMainGroup.setForeground(Utils.FOREGROUND_COLOR);
        boxOneMainGroup.addActionListener(e -> {
            radioMainGroup1.setEnabled(boxOneMainGroup.isSelected());
            radioMainGroup2.setEnabled(boxOneMainGroup.isSelected());
            gf.shouldUseOneMainGroup(boxOneMainGroup.isSelected());
            boxMainGroups.setSelected(false);
        });

        boxMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        boxMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        boxMainGroups.addActionListener(e -> {
            radioMainGroup1.setEnabled(false);
            radioMainGroup2.setEnabled(false);
            gf.shouldUseMainGroups(boxMainGroups.isSelected());
            boxOneMainGroup.setSelected(false);
            // TODO: SWAP presentation for the sizes.
        });

        radioMainGroup1.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup1.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup1.setEnabled(false);
        radioMainGroup1.setSelected(true);

        radioMainGroup2.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup2.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup2.setEnabled(false);

        bgMainGroups.add(radioMainGroup1);
        bgMainGroups.add(radioMainGroup2);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var p2 = new JPanel();
        p2.setBackground(Utils.BACKGROUND_COLOR);
        p2.setLayout(new BorderLayout());

        var p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.setBackground(Utils.BACKGROUND_COLOR);

        var p4 = new JPanel();
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        p4.setBackground(Utils.BACKGROUND_COLOR);

        p3.add(boxOneMainGroup);
        p3.add(radioMainGroup1);
        p3.add(radioMainGroup2);

        p4.add(boxMainGroups);

        pMainGroups.add(p3);
        pMainGroups.add(p4);

        pGroupCreator.add(cbCreators);

        p2.add(pGroupCreator, BorderLayout.PAGE_START);
        p2.add(pSettings, BorderLayout.CENTER);
        p2.add(pMainGroups, BorderLayout.PAGE_END);

        this.add(new JLabel("   "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(p2, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel("   "), BorderLayout.PAGE_END);
    }

    /**
     * Gets the current group selector.
     *
     * @return the currently selected group selector.
     * */
    public GroupCreator getGroupSelectedGroupCreator()
    {
        return (GroupCreator) cbCreators.getSelectedItem();
    }

    /**
     * Gets the currently selected MainGroup.
     *
     * @return the currently selected MainGroup;
     * */
    public Person.MainGroup getMainGroup()
    {
        return radioMainGroup1.isSelected()  ?
               Person.MainGroup.MAIN_GROUP_1 :
               Person.MainGroup.MAIN_GROUP_2;
    }

    /**
     * Gets the user input from the program,
     * to determine sizes.
     *
     * @return a list containing the sizes of
     *         the groups.
     * */
    @Override
    public List<Integer> getUserInput()
    {
        return pSettings.getUserInput();
    }

    /**
     * Getter for: cbCreators
     *
     * @return the instance of the cbCreators.
     * */
    public JComboBox<GroupCreator> getCbCreators()
    {
        return cbCreators;
    }
}
