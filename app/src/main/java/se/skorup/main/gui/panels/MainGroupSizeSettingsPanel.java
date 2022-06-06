package se.skorup.main.gui.panels;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.frames.GroupFrame;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The class used to represent the
 * size settings when using main groups.
 * */
public final class MainGroupSizeSettingsPanel extends SettingsPanel
{
    private final JLabel lblSizesMg1 = new JLabel();
    private final JLabel lblSizesMg2 = new JLabel();

    private final JRadioButton radioNbrGroups = new JRadioButton("Antal Grupper");
    private final JRadioButton radioNbrPersons = new JRadioButton("Antal personer/grupp");
    private final JRadioButton radioDifferentSizes = new JRadioButton("Olika antal personer/grupp");
    private final JRadioButton radioLeaderMode = new JRadioButton("Para grupper med ledare");

    private final ButtonGroup bg = new ButtonGroup();

    private final JCheckBox boxOverflow = new JCheckBox("Skapa extra grupper ifall det inte går jämt upp.");

    private final BorderedInputPanel pMG1 =
        new BorderedInputPanel("Huvudgrupp 1", 12, Utils.MAIN_GROUP_1_COLOR);
    private final BorderedInputPanel pMG2 =
        new BorderedInputPanel("Huvudgrupp 2", 12, Utils.MAIN_GROUP_2_COLOR);

    private final JPanel pRadios = new JPanel();
    private final JPanel pRadioContainer = new JPanel();
    private final JPanel pInput = new JPanel();

    private final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

    /**
     * Creates a new MainGroupSettingsPanel.
     *
     * @param gf the instance of the GroupFrame in use.
     * */
    public MainGroupSizeSettingsPanel(GroupFrame gf)
    {
        super(gf);

        this.setProperties();
        this.addComponents();
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setBorder(this.getSettingsBorder());
        this.setLayout(layout);

        pRadios.setBackground(Utils.BACKGROUND_COLOR);
        pRadios.setLayout(new BoxLayout(pRadios, BoxLayout.Y_AXIS));

        pRadioContainer.setBackground(Utils.BACKGROUND_COLOR);
        pRadioContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        radioNbrGroups.setForeground(Utils.FOREGROUND_COLOR);
        radioNbrGroups.setBackground(Utils.BACKGROUND_COLOR);
        radioNbrGroups.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        radioDifferentSizes.setForeground(Utils.FOREGROUND_COLOR);
        radioDifferentSizes.setBackground(Utils.BACKGROUND_COLOR);
        radioDifferentSizes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        radioLeaderMode.setForeground(Utils.FOREGROUND_COLOR);
        radioLeaderMode.setBackground(Utils.BACKGROUND_COLOR);
        radioLeaderMode.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        radioNbrPersons.setForeground(Utils.FOREGROUND_COLOR);
        radioNbrPersons.setBackground(Utils.BACKGROUND_COLOR);
        radioNbrPersons.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        bg.add(radioNbrGroups);
        bg.add(radioDifferentSizes);
        bg.add(radioLeaderMode);
        bg.add(radioNbrPersons);

        radioNbrGroups.addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_GROUPS));
        radioNbrPersons.addActionListener(e -> gf.setSizeState(GroupFrame.State.NUMBER_PERSONS));
        radioLeaderMode.addActionListener(e -> gf.setSizeState(GroupFrame.State.PAIR_WITH_LEADERS));
        radioDifferentSizes.addActionListener(e -> gf.setSizeState(GroupFrame.State.DIFFERENT_GROUP_SIZES));

        boxOverflow.setBackground(Utils.BACKGROUND_COLOR);
        boxOverflow.setForeground(Utils.FOREGROUND_COLOR);
        boxOverflow.addActionListener(e -> gf.setOverflow(boxOverflow.isSelected()));

        pInput.setLayout(new GridLayout(1, 2));
        pInput.setBackground(Utils.BACKGROUND_COLOR);

        updateSizeData(0, null);
    }

    /**
     * Adds the components.
     * */
    private void addComponents()
    {
        pRadios.add(new JLabel(" "));
        pRadios.add(radioNbrGroups);
        pRadios.add(radioNbrPersons);
        pRadios.add(radioDifferentSizes);
        pRadios.add(radioLeaderMode);
        pRadios.add(new JLabel(" "));
        pRadios.add(boxOverflow);
        pRadios.add(new JLabel(" "));
        pRadios.add(lblSizesMg1);
        pRadios.add(lblSizesMg2);

        pRadioContainer.add(pRadios);

        pInput.add(pMG1);
        pInput.add(pMG2);

        this.add(pRadioContainer, BorderLayout.CENTER);
        this.add(pInput, BorderLayout.PAGE_END);
    }

    private Optional<List<Integer>> getUserInput(BorderedInputPanel bip)
    {
        try
        {
            switch (gf.getSizeState())
            {
                case NUMBER_GROUPS:
                case NUMBER_PERSONS:
                    return Optional.of(
                               Collections.singletonList(
                                   Integer.parseInt(
                                       bip.getTextFieldText()
                           )));
                case PAIR_WITH_LEADERS:
                    return Optional.of(
                        Collections.singletonList(
                            gf.getManager().getAllOfRoll(Person.Role.LEADER).size()
                        ));
                case DIFFERENT_GROUP_SIZES:
                    var list =
                        Arrays.stream(bip.getTextFieldText().split(","))
                              .map(Integer::parseInt)
                              .toList();

                    return Optional.of(list);
                default:
                    return Optional.empty();
            }
        }
        catch (NumberFormatException e)
        {
            bip.setTextFieldBackground(Color.RED);
            DebugMethods.log(e, DebugMethods.LogType.ERROR);
            JOptionPane.showMessageDialog(
                this, "Felaktig indata: %s".formatted(e.getLocalizedMessage()),
                "Felaktig indata", JOptionPane.ERROR_MESSAGE
            );
            return Optional.empty();
        }
    }

    @Override
    public List<Integer> getUserInput()
    {
        var mg1 = getUserInput(pMG1);
        var mg2 = getUserInput(pMG2);

        if (mg1.isEmpty() || mg2.isEmpty())
            return List.of();

        var lmg1 = mg1.get();
        var lmg2 = mg2.get();
        var res = new ArrayList<>(lmg1);

        res.add(Integer.MIN_VALUE);
        res.addAll(lmg2);

        return res;
    }

    @Override
    public void updateSizeData(int size, Color c)
    {
        lblSizesMg1.setForeground(Utils.MAIN_GROUP_1_COLOR);
        lblSizesMg1.setText(
            getGroupSizeText(
                gf.getManager()
                    .getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1)
                    .size(),
                "Gruppstorlekar huvudgrupp 1: <br>"
        ));

        lblSizesMg2.setForeground(Utils.MAIN_GROUP_2_COLOR);
        lblSizesMg2.setText(
            getGroupSizeText(
                gf.getManager()
                    .getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2)
                    .size(),
                "<br>Gruppstorlekar huvudgrupp 2: <br>"
        ));
    }

    @Override
    public void reset()
    {
        radioNbrGroups.setSelected(true);
        gf.setSizeState(GroupFrame.State.NUMBER_GROUPS);
    }
}
