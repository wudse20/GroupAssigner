package se.skorup.main.gui.panels;

import se.skorup.API.util.Utils;
import se.skorup.main.gui.components.FlashingButton;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Subgroups;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * The panel that displays all the generated
 * subgroups to the frame. This is also responsible
 * for updating the Subgroups.
 * */
public class SubgroupDisplayPanel extends JPanel
{
    /**
     * Creates a new SubgroupDisplayPanel
     * and initializes it.
     * */
    public SubgroupDisplayPanel()
    {
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
        this.removeAll();
        var rows = (int) Math.ceil(subgroups.getGroupCount() / 2.0);
        this.setLayout(new GridLayout(rows, 2));

        int i = 0;
        for (var group : subgroups)
        {
            var cont = new JPanel();
            cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
            cont.setBackground(Utils.BACKGROUND_COLOR);
            cont.add(new FlashingButton(subgroups.getLabel(i++), Utils.GROUP_NAME_COLOR));

            for (var p : group)
            {
                cont.add(new FlashingButton(manager.getPersonFromId(p).getName()));
            }

            this.add(cont);
        }

        this.revalidate();
        this.repaint();
    }
}
