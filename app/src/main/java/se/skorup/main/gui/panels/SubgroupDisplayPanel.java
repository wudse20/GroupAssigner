package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.FlashingButton;
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
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * The panel that displays all the generated
 * subgroups to the frame. This is also responsible
 * for updating the Subgroups.
 * */
public class SubgroupDisplayPanel extends JPanel implements MouseListener
{
    private final SubgroupPanel sgp;
    private LayoutGenerator gen;

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
     * foreground color as its foreground color. It
     * will also add this as it's mouse listener.
     *
     * @param text the text of the button, i.e. the label.
     * @param length the length the text will be padded to.
     * @param foreground the foreground color of the button.
     * */
    private FlashingButton buildButton(String text, int length, Color foreground)
    {
        var fb = new FlashingButton(Utils.padString(text, ' ', length), foreground);
        fb.setBackground(Utils.BACKGROUND_COLOR);
        fb.setBorder(BorderFactory.createLineBorder(Utils.BACKGROUND_COLOR));
        fb.addMouseListener(this);
        return fb;
    }

    /**
     * Builds a FlashingButton with a text padded with
     * spaces to a given length and with the default
     * foreground color set as it's color. It will also add
     * this as its MouseListener.
     *
     * @param text the text of the button, i.e. the label.
     * @param length the length the text will be padded to.
     * */
    private FlashingButton buildButton(String text, int length)
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
            cont.add(buildButton(
                subgroups.getLabel(groupIndex++) + ':',
                longestNameLength, Utils.GROUP_NAME_COLOR
            ));

            for (var p : group)
            {
                var lbl = new JLabel(" ");
                lbl.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                cont.add(lbl);
                cont.add(buildButton(manager.getPersonFromId(p).getName(), longestNameLength));
            }

            cont.add(new JLabel(" "));
            container.add(cont);
        }

        this.add(container);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e)
    {
        var comp = (FlashingButton) e.getComponent();
        comp.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        comp.setBorder(BorderFactory.createLineBorder(Utils.FOREGROUND_COLOR));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        var comp = (FlashingButton) e.getComponent();
        comp.setBackground(Utils.BACKGROUND_COLOR);
        comp.setBorder(BorderFactory.createLineBorder(Utils.BACKGROUND_COLOR));
    }
}
