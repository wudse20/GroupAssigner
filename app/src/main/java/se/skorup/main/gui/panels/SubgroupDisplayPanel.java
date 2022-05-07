package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.SubgroupItemButton;
import se.skorup.main.gui.helper.DoubleColumnGenerator;
import se.skorup.main.gui.helper.LayoutGenerator;
import se.skorup.main.gui.helper.SingleColumnGenerator;
import se.skorup.main.gui.helper.TripleColumnGenerator;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The panel that displays all the generated
 * subgroups to the frame. This is also responsible
 * for updating the Subgroups.
 * */
public class SubgroupDisplayPanel extends JPanel
{
    /** The state for the buttons. */
    enum State { NOTHING_SELECTED, NAME_SELECTED }

    private final SubgroupPanel sgp;
    private LayoutGenerator gen;
    private State state = State.NOTHING_SELECTED;

    /**
     * Creates a new SubgroupDisplayPanel
     * and initializes it.
     * */
    public SubgroupDisplayPanel(SubgroupPanel sgp)
    {
        this.sgp = sgp;
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
     * Builds a FlashingButton with a text padded with
     * spaces to a given length and with the default
     * foreground color set as it's color.
     *
     * @param text the text of the button, i.e. the label.
     * @param length the length the text will be padded to.
     * */
    private SubgroupItemButton buildButton(String text, int length)
    {
        return buildButton(text, length, Utils.FOREGROUND_COLOR);
    }

    /**
     * Selects the LayoutGenerator to be used.
     *
     * @param vgap the vgap to be used.
     * */
    private void selectGen(int vgap)
    {
        if (sgp.getWidth() < 600)
            gen = new SingleColumnGenerator(sgp.getWidth(), vgap);
        else if (sgp.getWidth() < 1100)
            gen = new DoubleColumnGenerator(sgp.getWidth(), vgap);
        else
            gen = new TripleColumnGenerator(sgp.getWidth(), vgap);
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
            case NAME_SELECTED -> { /* TODO */ }
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
        sgp.repaint();
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
        this.selectGen(longestNameLength);

        this.removeAll();
        var container = new JPanel();
        container.setLayout(gen.generateLayout(subgroups.getGroupCount()));
        container.setBackground(Utils.BACKGROUND_COLOR);

        var groupIndex = 0;
        for (var group : subgroups)
        {
            var cont = new JPanel();
            cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
            cont.setBackground(Utils.BACKGROUND_COLOR);
            cont.add(new JLabel(" "));

            var btn = buildButton(subgroups.getLabel(groupIndex++) + ':', longestNameLength, Utils.GROUP_NAME_COLOR);
            btn.setHoverEnter(b -> hover(b, Utils.FOREGROUND_COLOR, Utils.COMPONENT_BACKGROUND_COLOR, Utils.SELECTED_COLOR));
            btn.setHoverExit(b -> hover(b, Utils.BACKGROUND_COLOR, Utils.BACKGROUND_COLOR, Utils.GROUP_NAME_COLOR));
            final var finalGroupIndex = groupIndex - 1;
            btn.addActionListener((e) -> groupNameButtonAction(finalGroupIndex, subgroups));
            cont.add(btn);

            for (var p : group)
            {
                var lbl = new JLabel(" ");
                lbl.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                cont.add(lbl);

                var btn2 = buildButton(manager.getPersonFromId(p).getName(), longestNameLength);
                btn2.setHoverEnter(b -> hover(b, Utils.FOREGROUND_COLOR, Utils.COMPONENT_BACKGROUND_COLOR, Utils.SELECTED_COLOR));
                btn2.setHoverExit(b -> hover(b, Utils.BACKGROUND_COLOR, Utils.BACKGROUND_COLOR, Utils.FOREGROUND_COLOR));
                cont.add(btn2);
            }

            cont.add(new JLabel(" "));
            container.add(cont);
        }

        this.add(container);
        this.revalidate();
        this.repaint();
    }
}
