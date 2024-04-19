package se.skorup.main.gui.group.panels;

import se.skorup.group.Group;
import se.skorup.group.generation.WishesGroupCreator;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.containers.Panel;
import se.skorup.gui.components.containers.ScrollPane;
import se.skorup.gui.components.containers.TabbedPane;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.layouts.DoubleColumnGenerator;
import se.skorup.gui.layouts.NColumnGenerator;
import se.skorup.gui.layouts.SingleColumnGenerator;
import se.skorup.gui.layouts.TripleColumnGenerator;
import se.skorup.main.gui.group.components.PersonButton;
import se.skorup.util.Log;
import se.skorup.util.Utils;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The panel responsible for displaying the Subgroups.
 * */
public class SubgroupDisplayPanel extends Panel
{
    private final Group g;

    private int selectedId = -1;
    private int selectedGroupIndex = -1;

    private final List<PersonButton> buttons;

    private final JComponent parent;
    private final TabbedPane tabs = new TabbedPane();

    /**
     * Creates a new panel.
     *
     * @param parent the parent component of this panel.
     * */
    public SubgroupDisplayPanel(JComponent parent, Group g)
    {
        super(new BorderLayout());
        this.parent = parent;
        this.g = g;
        this.buttons = new ArrayList<>();
    }

    /**
     * Figures out which layout manager should be used.
     *
     * @param width the width of the parent component.
     * @param vgap the desired vertical gap.
     * */
    private NColumnGenerator getLayout(int width, int vgap)
    {
        var gen = width < 600 ? new SingleColumnGenerator(width, vgap)  :
                  width < 1600 ? new DoubleColumnGenerator(width, vgap) :
                  new TripleColumnGenerator(width, vgap);

        Log.debugf("Width: %s, gen: %s", width, gen);

        return gen;
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
        this.buttons.clear();
        var groupPanel = getGroupPanel(g, groups);
        var statsPanel = getStatsPanel(g, groups);

        tabs.removeAll();
        tabs.add("ui.tab.subgroups", groupPanel);
        tabs.add("ui.tab.statistics", statsPanel);

        this.add(tabs, BorderLayout.CENTER);
        this.repaint();
        this.revalidate();
    }

    /**
     * Sets up the statistics panel that displays
     * statistics over the generated subgroups.
     *
     * @param g the group that the subgroups are generated from.
     * @param groups the generated groups.
     * */
    private Panel getStatsPanel(Group g, List<Set<Integer>> groups)
    {
        var score = WishesGroupCreator.getScore(groups, g);
        var persons = g.getIds();
        var x = new int[persons.size()];
        var highestCount = 0;
        var maxWishes = 0;

        var lbl = new Label("ui.label.nbr-wishes", true);
        lbl.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        var lbl2 = new Label("ui.label.general-stats", true);
        lbl2.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        var personPanel = new Panel(null);
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));
        personPanel.add(lbl);

        for (var p : persons)
        {
            var cnt = getFulfilledWishes(p, groups);
            personPanel.add(new Label(Localization.getValuef(
                "ui.label.fulfilled-wishes", g.getFromId(p), cnt)
            ));
            highestCount = Math.max(cnt, highestCount);
            x[cnt]++;
        }

        for (var i = 0; i < x.length; i++)
            maxWishes = x[i] != 0 ? i : maxWishes;

        var data = new ArrayList<Integer>();
        for (var i = 0; i < Math.min(maxWishes + 2, x.length); i++)
            data.add(x[i]);

        var infoPanel = new Panel(new GridLayout(1, 2));
        var contLeft = new Panel(null);
        contLeft.setLayout(new BoxLayout(contLeft, BoxLayout.Y_AXIS));
        contLeft.add(lbl2);
        contLeft.add(new Label(Localization.getValuef("ui.wishes.score", score)));
        contLeft.add(new Label(Localization.getValuef("ui.wishes.max", highestCount)));

        for (var i = 0; i <= maxWishes; i++)
        {
            var nbrWishes = x[i];
            contLeft.add(new Label(Localization.getValuef("ui.wishes.number", i, nbrWishes)));
        }

        var scr = new ScrollPane(personPanel, false);
        scr.setPreferredSize(new Dimension( this.getWidth(), this.getHeight() / 2));

        infoPanel.add(contLeft);
        infoPanel.add(scr);

        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(infoPanel);
        cont.add(new BarChartPanel(data));

        return new ComponentContainer(cont);
    }

    /**
     * Calculates the current number of wishes fulfilled for an id.
     *
     * @param id the id to count wishes for.
     * @param groups the groups that we want to calculate this for.
     * @return the number of granted wishes for <i>id</i>.
     * */
    private int getFulfilledWishes(int id, List<Set<Integer>> groups)
    {
        Set<Integer> g = null;
        for (var group : groups)
        {
            if (group.contains(id))
            {
                g = new HashSet<>(group);
                break;
            }
        }

        assert g != null : "g should never be null";
        g.retainAll(this.g.getWishedIds(id));
        return g.size();
    }

    /**
     * Sets up the group panel that displays
     * all the subgroups.
     *
     * @param g the group that the subgroups are generated from.
     * @param groups the generated groups.
     * */
    private Panel getGroupPanel(Group g, List<Set<Integer>> groups)
    {
        var longestName = g.getNames()
                           .stream()
                           .mapToInt(String::length)
                           .max()
                           .orElse(0);

        longestName = Math.max(
            Localization.getValuef("ui.label.subgroup", groups.size()).length(), longestName
        );

        var cont = new Panel(
            getLayout(parent.getWidth(), longestName * 4).generateLayout(groups.size())
        );

        var cnt = 1;
        for (var group : groups)
        {
            cnt++;
            var p = new Panel(null);
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

            var header = new PersonButton(
                Utils.pad(Localization.getValuef("ui.label.subgroup", cnt - 1), ' ', longestName + 2),
                cnt - 2, cnt - 2
            );

            header.setBorder(BorderFactory.createEmptyBorder());
            header.setForeground(Utils.GROUP_NAME_COLOR);
            header.hoverEffect = false;
            header.addActionListener(e -> {
                if (selectedId == -1 || selectedGroupIndex == -1 || selectedGroupIndex == header.groupIndex)
                    return;

                if (groups.get(selectedGroupIndex).remove(selectedId))
                    groups.get(header.groupIndex).add(selectedId);

                selectedId = -1;
                selectedGroupIndex = -1;
                buttons.forEach(PersonButton::clearSelection);
                this.displayGroups(g, groups);
            });

            p.add(header);

            for (var id : group)
            {
                var spacer = new Label(" ");
                spacer.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
                p.add(spacer);

                var btn = new PersonButton(
                    Utils.pad(g.getFromId(id).name(), ' ', longestName + 2),
                    id, cnt - 2
                );

                btn.addActionListener(e -> {
                    buttons.forEach(PersonButton::clearSelection);

                    if (selectedId != btn.id)
                    {
                        selectedId = btn.id;
                        selectedGroupIndex = btn.groupIndex;
                        btn.setSelected();
                    }
                    else
                    {
                        selectedId = -1;
                        selectedGroupIndex = -1;
                    }
                });

                p.add(btn);
                buttons.add(btn);
            }

            cont.add(p);
        }

        var scr = new ScrollPane(cont);
        scr.setBorder(BorderFactory.createEmptyBorder());
        return new ComponentContainer(scr);
    }
}
