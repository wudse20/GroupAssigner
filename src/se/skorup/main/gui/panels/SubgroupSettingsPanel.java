package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * The settings for the subgroup generation.
 * */
public class SubgroupSettingsPanel extends JPanel
{
    /** The instance of the GroupFrame this panel is a part off. */
    private final GroupFrame gf;

    /** The radio button for the first main group. */
    private final JRadioButton radioMainGroup1 = new JRadioButton("Huvudgrupp 1");

    /** The radio button for the first main group. */
    private final JRadioButton radioMainGroup2 = new JRadioButton("Huvudgrupp 2");

    /** The button group for the MainGroups. */
    private final ButtonGroup bgMainGroups = new ButtonGroup();

    /** The button group for the settings. */
    private final ButtonGroup bgSettings = new ButtonGroup();

    /** The checkbox used for overflow. TODO: CHANGE LABEL */
    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

    /** The checkbox used for the MainGroups. */
    private final JCheckBox boxMainGroups = new JCheckBox("Använd huvudgrupper");

    /** The panel containing the MainGroup's settings. */
    private final JPanel pMainGroups = new JPanel();

    /** The panel containing the settings of the group generation. */
    private final JPanel pSettings = new JPanel();

    /** The panel for pairing a group with the leaders. */
    private final SettingPanel pLeaders =
        new SettingPanel("Para grupper med ledare", null, 0, false);

    /** The panel for setting number of groups. */
    private final SettingPanel pNbrGroups =
        new SettingPanel("%-35s".formatted("Antal grupper"), null, 4, true);

    /** The panel for setting persons/group. */
    private final SettingPanel pNbrMembers =
        new SettingPanel("%-35s".formatted("Antal personer/grupp"), null, 4, true);

    /** The panel for setting different sizes. */
    private final SettingPanel pDifferentSizes =
        new SettingPanel("%-35s".formatted("Olika antal personer/grupp"), null, 4, true);

    /**
     * Creates a new SubgroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public SubgroupSettingsPanel(GroupFrame gf)
    {
        this.gf = gf;

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

        var settingsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Inställningar"
        );

        settingsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        var mainGroupsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Huvudgrupper"
        );

        mainGroupsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        pSettings.setLayout(new BoxLayout(pSettings, BoxLayout.Y_AXIS));
        pSettings.setForeground(Utils.FOREGROUND_COLOR);
        pSettings.setBackground(Utils.BACKGROUND_COLOR);
        pSettings.setBorder(settingsBorder);

        pMainGroups.setLayout(new FlowLayout(FlowLayout.LEFT));
        pMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        pMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        pMainGroups.setBorder(mainGroupsBorder);

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);
        boxOverflow.addActionListener(e -> gf.setOverflow(boxOverflow.isSelected()));

        boxMainGroups.setBackground(Utils.BACKGROUND_COLOR);
        boxMainGroups.setForeground(Utils.FOREGROUND_COLOR);
        boxMainGroups.addActionListener(e -> {
            radioMainGroup1.setEnabled(boxMainGroups.isSelected());
            radioMainGroup2.setEnabled(boxMainGroups.isSelected());
            gf.shouldUseMainGroups(boxMainGroups.isSelected());
        });

        radioMainGroup1.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup1.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup1.setEnabled(false);
        radioMainGroup1.setSelected(true);

        radioMainGroup2.setForeground(Utils.FOREGROUND_COLOR);
        radioMainGroup2.setBackground(Utils.BACKGROUND_COLOR);
        radioMainGroup2.setEnabled(false);

        pNbrGroups.setRadioSelected(true);

        pNbrGroups.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_GROUPS));
        pNbrMembers.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_PERSONS));
        pLeaders.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.PAIR_WITH_LEADERS));
        pDifferentSizes.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.DIFFERENT_GROUP_SIZES));

        bgMainGroups.add(radioMainGroup1);
        bgMainGroups.add(radioMainGroup2);

        bgSettings.add(pNbrGroups.getRadio());
        bgSettings.add(pNbrMembers.getRadio());
        bgSettings.add(pLeaders.getRadio());
        bgSettings.add(pDifferentSizes.getRadio());
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        var p = new JPanel();
        p.setBackground(Utils.BACKGROUND_COLOR);
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(boxOverflow);

        var p2 = new JPanel();
        p2.setBackground(Utils.BACKGROUND_COLOR);
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

        pSettings.add(pNbrGroups);
        pSettings.add(pNbrMembers);
        pSettings.add(pDifferentSizes);
        pSettings.add(new JLabel(" "));
        pSettings.add(pLeaders);
        pSettings.add(new JLabel(" "));
        pSettings.add(p);

        pMainGroups.add(boxMainGroups);
        pMainGroups.add(radioMainGroup1);
        pMainGroups.add(radioMainGroup2);

        p2.add(pSettings);
        p2.add(new JLabel(" "));
        p2.add(pMainGroups);

        this.add(new JLabel("   "), BorderLayout.PAGE_START);
        this.add(new JLabel("   "), BorderLayout.LINE_START);
        this.add(p2, BorderLayout.CENTER);
        this.add(new JLabel("   "), BorderLayout.LINE_END);
        this.add(new JLabel("   "), BorderLayout.PAGE_END);
    }
}
