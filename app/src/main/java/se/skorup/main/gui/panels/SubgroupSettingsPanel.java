package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.groups.creators.GroupCreator;
import se.skorup.main.groups.creators.RandomGroupCreator;
import se.skorup.main.groups.creators.WishesGroupCreator;
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
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

/**
 * The settings for the subgroup generation.
 * */
public final class SubgroupSettingsPanel extends SettingsPanel
{
    private final JRadioButton radioMainGroup1 = new JRadioButton("Huvudgrupp 1");
    private final JRadioButton radioMainGroup2 = new JRadioButton("Huvudgrupp 2");

    private final ButtonGroup bgMainGroups = new ButtonGroup();

    private final JCheckBox boxOneMainGroup = new JCheckBox("Använd en huvudgrupp");
    private final JCheckBox boxMainGroups = new JCheckBox("Använd huvudgrupper");

    private final JComboBox<GroupCreator> cbCreators = new JComboBox<>();

    private final JPanel pMainGroups = new JPanel();
    private final JPanel pGroupCreator = new JPanel();

    // Bad names I know and will regret sometime in the future.
    private final JPanel p1 = new JPanel();
    private final JPanel p2 = new JPanel();
    private final JPanel p3 = new JPanel();

    private SettingsPanel pSettings;
    private final SettingsPanel mainGroupSettings;
    private final SettingsPanel defaultSettings;

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupSettingsPanel(GroupFrame gf)
    {
        super(gf);

        mainGroupSettings = new MainGroupSizeSettingsPanel(gf);
        defaultSettings = new SizeSettingsPanel(gf);
        pSettings = defaultSettings;

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
        cbCreators.addItem(new RandomGroupCreator());
        cbCreators.addItem(new WishesGroupCreator());

        boxOneMainGroup.setBackground(Utils.BACKGROUND_COLOR);
        boxOneMainGroup.setForeground(Utils.FOREGROUND_COLOR);
        boxOneMainGroup.addActionListener(e -> {
            radioMainGroup1.setEnabled(boxOneMainGroup.isSelected());
            radioMainGroup2.setEnabled(boxOneMainGroup.isSelected());
            gf.shouldUseOneMainGroup(boxOneMainGroup.isSelected());
            boxMainGroups.setSelected(false);
            displayCorrectSizeSettingsPanel();
            displayCorrectSizeInfo();
        });

        boxMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        boxMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        boxMainGroups.addActionListener(e -> {
            radioMainGroup1.setEnabled(false);
            radioMainGroup2.setEnabled(false);
            gf.shouldUseMainGroups(boxMainGroups.isSelected());
            boxOneMainGroup.setSelected(false);
            displayCorrectSizeSettingsPanel();
            displayCorrectSizeInfo();
        });

        radioMainGroup1.setForeground(Utils.MAIN_GROUP_1_COLOR);
        radioMainGroup1.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup1.setEnabled(false);
        radioMainGroup1.setSelected(true);
        radioMainGroup1.addActionListener(e -> displayCorrectSizeInfo());

        radioMainGroup2.setForeground(Utils.MAIN_GROUP_2_COLOR);
        radioMainGroup2.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup2.setEnabled(false);
        radioMainGroup2.addActionListener(e -> displayCorrectSizeInfo());

        bgMainGroups.add(radioMainGroup1);
        bgMainGroups.add(radioMainGroup2);

        p1.setBackground(Utils.BACKGROUND_COLOR);
        p1.setLayout(new BorderLayout());

        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.setBackground(Utils.BACKGROUND_COLOR);

        p3.setLayout(new FlowLayout(FlowLayout.LEFT));
        p3.setBackground(Utils.BACKGROUND_COLOR);

        displayCorrectSizeInfo();
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        p1.removeAll();
        p2.removeAll();
        p3.removeAll();

        p2.add(boxOneMainGroup);
        p2.add(radioMainGroup1);
        p2.add(radioMainGroup2);

        p3.add(boxMainGroups);

        pMainGroups.add(p2);
        pMainGroups.add(p3);

        pGroupCreator.add(cbCreators);

        p1.add(pGroupCreator, BorderLayout.PAGE_START);
        p1.add(pSettings, BorderLayout.CENTER);
        p1.add(pMainGroups, BorderLayout.PAGE_END);

        this.add(new JLabel("   "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(p1, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel("   "), BorderLayout.PAGE_END);

        p1.revalidate();
        p2.revalidate();
        p3.revalidate();
    }

    /**
     * Displays the correct size settings
     * in the GUI.
     * */
    private void displayCorrectSizeSettingsPanel()
    {
        if (boxMainGroups.isSelected())
            pSettings = mainGroupSettings;
        else
            pSettings = defaultSettings;

        pSettings.reset();

        removeAll();
        addComponents();
        revalidate();
        repaint();
    }

    /**
     * Displays the correct size info.
     * */
    private void displayCorrectSizeInfo()
    {
        if (boxOneMainGroup.isSelected())
        {
            updateSizeData(
                gf.getManager()
                  .getAllOfMainGroupAndRoll(
                      Person.Role.CANDIDATE,
                      radioMainGroup1.isSelected()  ?
                      Person.MainGroup.MAIN_GROUP_1 :
                      Person.MainGroup.MAIN_GROUP_2
                 ).size(),
                  radioMainGroup1.isSelected() ?
                  Utils.MAIN_GROUP_1_COLOR     :
                  Utils.MAIN_GROUP_2_COLOR
            );
        }
        else
        {
            updateSizeData(gf.getManager().getMemberCountOfRole(Person.Role.CANDIDATE), Utils.FOREGROUND_COLOR);
        }
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

    @Override
    public void updateSizeData(int size, Color c)
    {
        defaultSettings.updateSizeData(size, c);
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
