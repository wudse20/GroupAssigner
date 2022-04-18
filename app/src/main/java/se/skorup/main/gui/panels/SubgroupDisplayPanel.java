package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.FlashingButton;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridLayout;
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
    private final SubgroupPanel sgp;

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

        this.removeAll();
        var rows = (int) Math.ceil(subgroups.getGroupCount() / 2.0);
        this.setLayout(new GridLayout(rows, 2, sgp.getWidth() / 3, longestNameLength));

        int groupIndex = 0;
        for (var group : subgroups)
        {
            var cont = new JPanel();
            cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
            cont.setBackground(Utils.BACKGROUND_COLOR);
            cont.add(new FlashingButton(
                Utils.padString(subgroups.getLabel(groupIndex++) + ':', ' ', longestNameLength),
                Utils.GROUP_NAME_COLOR
            ));

            for (var p : group)
            {
                var lbl = new JLabel(" ");
                lbl.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                cont.add(lbl);

                cont.add(new FlashingButton(
                    Utils.padString(manager.getPersonFromId(p).getName(), ' ', longestNameLength)
                ));
            }

            this.add(cont);
        }

        this.revalidate();
        this.repaint();
    }
}
