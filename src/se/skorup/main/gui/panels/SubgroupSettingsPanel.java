package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.GroupFrame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Color;
import java.awt.Component;
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

    /** The checkbox used for overflow. TODO: CHANGE LABEL */
    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

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
     * Creates a new SubgroupSettingsPanel..
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
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var settingsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Inställningar"
        );

        settingsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        pSettings.setLayout(new BoxLayout(pSettings, BoxLayout.Y_AXIS));
        pSettings.setForeground(Utils.FOREGROUND_COLOR);
        pSettings.setBackground(Utils.BACKGROUND_COLOR);
        pSettings.setBorder(settingsBorder);

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);
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

        pSettings.add(pNbrGroups);
        pSettings.add(pNbrMembers);
        pSettings.add(pDifferentSizes);
        pSettings.add(new JLabel(" "));
        pSettings.add(pLeaders);
        pSettings.add(new JLabel(" "));
        pSettings.add(p);

        this.add(pSettings);
    }
}
