package se.skorup.main.gui.panels;

import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.SubgroupItemButton;
import se.skorup.main.gui.helper.Selection;
import se.skorup.main.gui.helper.layout.DoubleColumnGenerator;
import se.skorup.main.gui.helper.layout.LayoutGenerator;
import se.skorup.main.gui.helper.layout.SingleColumnGenerator;
import se.skorup.main.gui.helper.layout.TripleColumnGenerator;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * The panel that displays all the generated
 * subgroups to the frame. This is also responsible
 * for updating the Subgroups.
 * */
public class SubgroupDisplayPanel extends JPanel
{
    /** The state for the buttons. */
    enum State { NOTHING_SELECTED, NAME_SELECTED }

    private final JComponent parent;
    private final boolean disableActions;

    private LayoutGenerator gen;
    private State state = State.NOTHING_SELECTED;
    private Selection selected = Selection.empty();
    private ImmutableArray<SubgroupItemButton> groupButtons = ImmutableArray.empty();
    private boolean shouldDisplayMainGroups = false;

    /**
     * Creates a new SubgroupDisplayPanel
     * and initializes it. The actions aren't
     * disabled by using this constructor.
     *
     * @param parent the parent component of this panel.
     * */
    public SubgroupDisplayPanel(JComponent parent)
    {
        this(parent, false);
    }

    /**
     * Creates a new SubgroupDisplayPanel and initializes it.
     * The actions will be disabled based on the parameter
     * disable actions.
     *
     * @param parent the parent component of this panel.
     * @param disableActions if {@code false} then the actions won't be disabled,
     *                       else if {@code true} then the actions will be disabled.
     * */
    public SubgroupDisplayPanel(JComponent parent, boolean disableActions)
    {
        this.parent = parent;
        this.disableActions = disableActions;
        this.setProperties();
    }

    /**
     * This sets the properties of all the
     * components.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
    }


    /**
     * Builds a FlashingButton with a text padded with
     * spaces to a given length and with the passed
     * foreground color as its foreground color.
     *
     * @param text the text of the button, i.e. the label.
     * @param length the length the text will be padded to.
     * @param foreground the foreground color of the button.
     * */
    private SubgroupItemButton buildButton(String text, int length, Color foreground)
    {
        var fb = new SubgroupItemButton(Utils.padString(text, ' ', length), foreground);
        fb.setBackground(Utils.BACKGROUND_COLOR);
        fb.setBorder(BorderFactory.createLineBorder(Utils.BACKGROUND_COLOR));
        return fb;
    }

    /**
     * Selects the LayoutGenerator to be used.
     *
     * @param vgap the vgap to be used.
     * @param width the width of the container.
     * */
    private void selectGen(int vgap, int width)
    {
        if (width < 600)
            gen = new SingleColumnGenerator(width, vgap);
        else if (width < 1600)
            gen = new DoubleColumnGenerator(width, vgap);
        else
            gen = new TripleColumnGenerator(width, vgap);
    }

    /**
     * The method to hover effects
     *
     * @param b                The affected button.
     * @param backgroundColor  The color of the border.
     * @param backgroundColor1 The color of the background.
     * @param groupNameColor   The color of the group names.
     * */
    private void hover(SubgroupItemButton b, Color backgroundColor, Color backgroundColor1, Color groupNameColor)
    {
        b.setBorder(BorderFactory.createLineBorder(backgroundColor));
        b.setForeground(groupNameColor);
        b.setBackground(backgroundColor1);
    }

    /**
     * The action of a groupName.
     *
     * @param groupIndex the index of the group the action
     *                   is applied to.
     * @param sgs the subgroups.
     * */
    private void groupNameButtonAction(int groupIndex, Subgroups sgs)
    {
        switch (state)
        {
            case NOTHING_SELECTED -> swapGroupName(groupIndex, sgs);
            case NAME_SELECTED -> addPersonToGroup(groupIndex, sgs);
        }
    }

    /**
     * Asks the user for a new name and sets the new name to the
     * group.
     *
     * @param groupIndex the index of the group.
     * @param sgs the subgroups.
     * */
    private void swapGroupName(int groupIndex, Subgroups sgs)
    {
        var newName = JOptionPane.showInputDialog(
            this, "Vad ska %s heta?".formatted(sgs.getLabel(groupIndex)),
            "Nytt namn", JOptionPane.INFORMATION_MESSAGE
        );

        if (newName == null || newName.trim().isEmpty())
            return;

        sgs.setLabel(groupIndex, newName);
        parent.repaint();
    }

    /**
     * Adds a person to a group and removes it from its
     * current group.
     *
     * @param groupIndex the index of the group being added to.
     * @param sgs the subgroups.
     * */
    private void addPersonToGroup(int groupIndex, Subgroups sgs)
    {
        if (selected.isEmpty() || selected.group() == groupIndex)
            return;

        sgs.addPersonToGroup(selected.id(), groupIndex);
        sgs.removePersonFromGroup(selected.id(), selected.group());
        selected = Selection.empty();
        state = State.NOTHING_SELECTED;
        parent.repaint();
    }

    /**
     * The action of a person.
     *
     * @param groupIndex the index of the persons group
     * @param id the id of the person.
     * */
    private void personAction(int groupIndex, int id)
    {
        var selection = new Selection(id, groupIndex);

        if (selected.equals(selection))
        {
            selected = Selection.empty();
            state = State.NOTHING_SELECTED;
            parent.repaint();
            return;
        }

        state = State.NAME_SELECTED;
        selected = selection;
        parent.repaint();
    }

    /**
     * Gets the correct color for a person with an id.
     *
     * @param id the id of the person.
     * @param gm the group manager in use.
     * @return the proper color of a person.
     * */
    private Color getColor(int id, GroupManager gm)
    {
        if (!shouldDisplayMainGroups)
            return Utils.FOREGROUND_COLOR;

        if (gm.getPersonFromId(id).getMainGroup().equals(Person.MainGroup.MAIN_GROUP_1))
            return Utils.MAIN_GROUP_1_COLOR;
        else
            return Utils.MAIN_GROUP_2_COLOR;
    }

    /**
     * Displays the subgroups to the panel.
     *
     * @param subgroups the subgroups to be displayed.
     * @param manager the group manager in use.
     * */
    public void displaySubgroup(Subgroups subgroups, GroupManager manager)
    {
        if (subgroups == null || manager == null)
            return;

        // Wrapping in ArrayList to prevent crashes, since .toList gives an immutable list.
        var nameList = new ArrayList<>(
            subgroups.groups()
                     .parallelStream()
                     .flatMap(Collection::parallelStream)
                     .map(manager::getPersonFromId)
                     .map(Person::getName)
                     .toList()
        );

        nameList.sort(Comparator.comparingInt(String::length));
        var longestNameLength = nameList.get(nameList.size() - 1).length();
        this.selectGen(longestNameLength, parent.getWidth());

        this.removeAll();
        var container = new JPanel();
        container.setLayout(gen.generateLayout(subgroups.getGroupCount()));
        container.setBackground(Utils.BACKGROUND_COLOR);

        var groupBtnList = new ArrayList<SubgroupItemButton>();
        var groupIndex = 0;
        for (var group : subgroups)
        {
            var cont = new JPanel();
            cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
            cont.setBackground(Utils.BACKGROUND_COLOR);
            cont.add(new JLabel(" "));

            if (disableActions)
            {
                var lbl = new JLabel(Utils.padString(subgroups.getLabel(groupIndex++), ' ', longestNameLength));
                lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
                lbl.setForeground(Utils.GROUP_NAME_COLOR);
                cont.add(lbl);

                for (final var p : group)
                {
                    var lblSpacer = new JLabel(" ");
                    lblSpacer.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                    cont.add(lblSpacer);

                    var label = getLabel(p, manager, subgroups);
                    var lbl2 = new JLabel(Utils.padString(label, ' ', longestNameLength));
                    lbl2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
                    lbl2.setForeground(getColor(p, manager));
                    cont.add(lbl2);
                }
            }
            else
            {
                var btn = buildButton(subgroups.getLabel(groupIndex++) + ':', longestNameLength, Utils.GROUP_NAME_COLOR);
                btn.setHoverEnter(b -> hover((SubgroupItemButton) b, Utils.FOREGROUND_COLOR, Utils.COMPONENT_BACKGROUND_COLOR, Utils.SELECTED_COLOR));
                btn.setHoverExit(b -> hover((SubgroupItemButton) b, Utils.BACKGROUND_COLOR, Utils.BACKGROUND_COLOR, Utils.GROUP_NAME_COLOR));
                final var finalGroupIndex = groupIndex - 1;
                btn.addActionListener((e) -> groupNameButtonAction(finalGroupIndex, subgroups));
                cont.add(btn);
                groupBtnList.add(btn);

                for (final var p : group)
                {
                    var lbl = new JLabel(" ");
                    lbl.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                    cont.add(lbl);

                    var txtColor = selected.id() == p ? Utils.SELECTED_COLOR : getColor(p, manager);
                    var txtColor2 = selected.id() == p ? getColor(p, manager) : Utils.SELECTED_COLOR;
                    var label = getLabel(p, manager, subgroups);
                    var btn2 = buildButton(label, longestNameLength, txtColor);
                    btn2.setHoverEnter(b -> hover((SubgroupItemButton) b, Utils.FOREGROUND_COLOR, Utils.COMPONENT_BACKGROUND_COLOR, txtColor2));
                    btn2.setHoverExit(b -> hover((SubgroupItemButton) b, Utils.BACKGROUND_COLOR, Utils.BACKGROUND_COLOR, txtColor));
                    btn2.addActionListener(e -> personAction(finalGroupIndex, p));
                    cont.add(btn2);
                }
            }

            cont.add(new JLabel(" "));
            container.add(cont);
        }

        this.groupButtons = ImmutableArray.fromList(groupBtnList);
        this.add(container);
        this.revalidate();
        this.repaint();

        if (state.equals(State.NAME_SELECTED))
        {
            groupButtons.dropMatching(groupButtons.get(selected.group()))
                        .forEach(b -> b.startFlashing(
                            500, Utils.FLASH_COLOR,
                            Utils.MAIN_GROUP_1_COLOR, Utils.MAIN_GROUP_2_COLOR
                        ));
        }
    }

    private String getLabel(int p, GroupManager manager, Subgroups sg)
    {
        if (sg.isWishListMode())
            return "%s (%d)".formatted(
                manager.getPersonFromId(p).getName(),
                sg.getNumberOfWishes(p, manager)
            );

        return manager.getPersonFromId(p).getName();
    }

    /**
     * Resets the panel.
     * */
    public void reset()
    {
        groupButtons.forEach(SubgroupItemButton::stopFlashing);

        this.removeAll();
        this.selected = Selection.empty();
        this.state = State.NOTHING_SELECTED;
        this.groupButtons = ImmutableArray.empty();
        this.revalidate();
    }

    /**
     * Shows and hides the colors of the names from the main groups.
     *
     * @param shouldDisplayMainGroups if {@code true} it will display the colors,
     *                               else if {@code false} it will display the standard forground color.
     * */
    public void setMainGroupDisplay(boolean shouldDisplayMainGroups)
    {
        this.shouldDisplayMainGroups = shouldDisplayMainGroups;
        parent.repaint();
    }
}
