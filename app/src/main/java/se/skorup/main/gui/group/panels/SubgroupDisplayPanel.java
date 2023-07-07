package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.containers.ScrollPane;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.layouts.DoubleColumnGenerator;
import se.skorup.gui.layouts.NColumnGenerator;
import se.skorup.gui.layouts.SingleColumnGenerator;
import se.skorup.gui.layouts.TripleColumnGenerator;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.Set;

/**
 * The panel responsible for displaying the Subgroups.
 * */
public class SubgroupDisplayPanel extends Panel
{
    private final JComponent parent;

    /**
     * Creates a new panel.
     *
     * @param parent the parent component of this panel.
     * */
    public SubgroupDisplayPanel(JComponent parent)
    {
        super(new BorderLayout());
        this.parent = parent;
    }

    /**
     * Figures out which layout manager should be used.
     *
     * @param width the width of the parent component.
     * @param vgap the desired vertical gap.
     * */
    private NColumnGenerator getLayout(int width, int vgap)
    {
        return width < 600 ? new SingleColumnGenerator(width, vgap) :
               width < 1600 ? new DoubleColumnGenerator(width, vgap) :
               new TripleColumnGenerator(width, vgap);
    }

    /**
     * Displays groups to the GUI.
     *
     * @param g the group used to create the groups.
     * @param groups the groups to be displayed.
     * */
    public void displayGroups(Group g, List<Set<Integer>> groups)
    {
        this.removeAll();
        var longestName = g.getNames()
                           .stream()
                           .mapToInt(String::length)
                           .max()
                           .orElse(0);

        var cont = new Panel(
            getLayout(parent.getWidth(), longestName * 2).generateLayout(groups.size())
        );

        var cnt = 1;
        for (var group : groups)
        {
            var p = new Panel(null);
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

            var header = new Label(Localization.getValuef("ui.label.subgroup", cnt++));
            header.setForeground(Utils.GROUP_NAME_COLOR);
            header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
            p.add(header);

            for (var id : group)
            {
                var spacer = new Label(" ");
                spacer.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                p.add(spacer);

                var lbl = new Label(g.getFromId(id).name());
                lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
                p.add(lbl);
            }

            cont.add(p);
        }

        var scr = new ScrollPane(cont);
        scr.setBorder(BorderFactory.createEmptyBorder());
        this.add(new ComponentContainer(scr), BorderLayout.CENTER);
        this.repaint();
        this.revalidate();
    }
}
