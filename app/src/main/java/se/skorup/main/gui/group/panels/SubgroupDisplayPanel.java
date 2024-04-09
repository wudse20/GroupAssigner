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
import se.skorup.util.Utils;
import se.skorup.util.collections.ImmutableHashSet;
import se.skorup.util.localization.Localization;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The panel responsible for displaying the Subgroups.
 * */
public class SubgroupDisplayPanel extends Panel
{
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
    public SubgroupDisplayPanel(JComponent parent)
    {
        super(new BorderLayout());
        this.parent = parent;
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

        for (var p : persons)
        {
            var cnt = 0;
            var wished = ImmutableHashSet.fromCollection(g.getWishedIds(p));

            for (var gr : groups)
            {
                if (gr.contains(p))
                {
                    cnt += wished.intersection(gr).size();
                }
            }

            highestCount = Math.max(cnt, highestCount);
            x[cnt]++;
        }

        for (var i = 0; i < x.length; i++)
            maxWishes = x[i] != 0 ? i : maxWishes;

        var data = new ArrayList<Integer>();
        for (var i = 0; i < Math.min(maxWishes + 2, x.length); i++)
            data.add(x[i]);

        var cont = new Panel(null);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(new Label(Localization.getValuef("ui.wishes.score", score)));
        cont.add(new Label(Localization.getValuef("ui.wishes.max", highestCount)));

        for (var i = 0; i < maxWishes; i++)
        {
            var nbrWishes = x[i];
            cont.add(new Label(Localization.getValuef("ui.wishes.number", i, nbrWishes)));
        }

        cont.add(new BarChartPanel(data));

        return new ComponentContainer(cont);
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
                Utils.pad(Localization.getValuef("ui.label.subgroup", cnt), ' ', longestName + 2),
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
