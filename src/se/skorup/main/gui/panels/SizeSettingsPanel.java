package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.objects.Person;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The default size settings in the SubgrouthisPanel.
 * */
public final class SizeSettingsPanel extends SettingsPanel
{
    private final GroupFrame gf;

    private final ButtonGroup bgSettings = new ButtonGroup();

    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

    private final SettingPanel pNbrGroups =
        new SettingPanel("%-35s".formatted("Antal grupper"), null, 4, true);
    private final SettingPanel pLeaders =
            new SettingPanel("Para grupper med ledare", null, 0, false);
    private final SettingPanel pNbrMembers =
            new SettingPanel("%-35s".formatted("Antal personer/grupp"), null, 4, true);
    private final SettingPanel pDifferentSizes =
            new SettingPanel("%-35s".formatted("Olika antal personer/grupp"), null, 4, true);

    /**
     * Creates a new SizeSettingsPanel.
     * */
    public SizeSettingsPanel(GroupFrame gf)
    {
        super();
        this.gf = gf;

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties of the panel.
     * */
    private void setProperties()
    {
        var settingsBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR), "Inställningar"
        );

        settingsBorder.setTitleColor(Utils.FOREGROUND_COLOR);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setBorder(settingsBorder);

        pNbrGroups.setRadioSelected(true);
        pNbrGroups.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_GROUPS));
        pNbrMembers.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_PERSONS));
        pLeaders.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.PAIR_WITH_LEADERS));
        pDifferentSizes.getRadio().addActionListener(e -> gf.setSizeState(GroupFrame.State.DIFFERENT_GROUP_SIZES));

        bgSettings.add(pNbrGroups.getRadio());
        bgSettings.add(pNbrMembers.getRadio());
        bgSettings.add(pLeaders.getRadio());
        bgSettings.add(pDifferentSizes.getRadio());

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);
        boxOverflow.addActionListener(e -> gf.setOverflow(boxOverflow.isSelected()));
    }

    /**
     * Adds the components to the panel.
     * */
    private void addComponents()
    {
        var p = new JPanel();
        p.setBackground(Utils.BACKGROUND_COLOR);
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(boxOverflow);

        this.add(pNbrGroups);
        this.add(pNbrMembers);
        this.add(pDifferentSizes);
        this.add(pLeaders);
        this.add(p);
    }

    @Override
    public List<Integer> getUserInput()
    {
        try
        {
            // Leader mode
            if (pLeaders.isRadioSelected())
                return Collections.singletonList(gf.getManager().getAllOfRoll(Person.Role.LEADER).size());

            // Number of groups mode
            if (pNbrGroups.isRadioSelected())
                return Collections.singletonList(Integer.parseInt(pNbrGroups.getTextFieldData()));

            // Number of members mode
            if (pNbrMembers.isRadioSelected())
                return Collections.singletonList(Integer.parseInt(pNbrMembers.getTextFieldData()));

            // Different sizes mode.
            return Arrays.stream(pDifferentSizes.getTextFieldData().split(",")) // Splitting
                    .map(String::trim) // Trimming
                    .map(Integer::parseInt) // Parsing
                    .collect(Collectors.toList()); // Convert stream into list.
        }
        catch (NumberFormatException e)
        {
            DebugMethods.log(e, DebugMethods.LogType.ERROR);

            JOptionPane.showMessageDialog(
                    this, "Felaktig indata: %s".formatted(e.getLocalizedMessage()),
                    "Felaktig indata", JOptionPane.ERROR_MESSAGE
            );

            return List.of();
        }
    }
}
