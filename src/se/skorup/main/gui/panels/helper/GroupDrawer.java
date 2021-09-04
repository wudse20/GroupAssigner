package se.skorup.main.gui.panels.helper;

import se.skorup.API.ImmutableArray;
import se.skorup.API.ImmutableHashSet;
import se.skorup.API.Utils;
import se.skorup.main.gui.objects.PersonBox;
import se.skorup.main.gui.objects.TextBox;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Subgroups;

import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import static se.skorup.main.gui.panels.SubgroupPanel.SPACER;

/**
 * A class that draws the subgroups in
 * the SubgroupPanel.
 * */
public class GroupDrawer
{
    private final SubgroupPanel sgp;

    private final GroupManager gm;

    private final Subgroups current;

    private final FontMetrics fm;

    /**
     * Creates a new GroupDrawer.
     *
     * @param sgp the instance of the SubgroupPanel.
     * @param gm the currently used GroupManager.
     * @param current the instance of the current subgroups.
     * @param fm the font metrics of the frame.
     * */
    public GroupDrawer(SubgroupPanel sgp, GroupManager gm, Subgroups current, FontMetrics fm)
    {
        this.sgp = sgp;
        this.gm = gm;
        this.current = current;
        this.fm = fm;
    }

    /**
     * Calculates the height of the panel.
     *
     * @return the calculated height of the panel.
     * */
    public int height()
    {
        if (current == null || fm == null)
            return 0;

        int max = Collections.max(current.groups().stream().map(Set::size).collect(Collectors.toList()));
        return (int) ((3 * SPACER + SPACER * current.groups().size() + max * (SPACER + fm.getHeight())) * 1.5F);
    }

    /**
     * Prepares the drawing of the Groups.
     *
     * @return all the text boxes.
     * */
    public ImmutableArray<TextBox> initGroups()
    {
        var tb = new Vector<TextBox>();

        var groups =
                current.groups()
                        .stream()
                        .map(x -> x.stream().map(gm::getPersonFromId))
                        .map(x -> x.collect(Collectors.toList()))
                        .collect(Collectors.toList());

        var max = Collections.max(current.groups().stream().map(Set::size).collect(Collectors.toList()));

        for (var i = 0; i < groups.size(); i++)
        {
            var x = (i % 2 == 0) ? sgp.getWidth() / 10 : 3 * (sgp.getWidth() / 4);
            var y = 3 * SPACER + SPACER * (i % groups.size() / 2) * (max + 2);

            tb.add(new TextBox(current.getLabel(i), x, y, Utils.GROUP_NAME_COLOR));

            var gr = groups.get(i);
            for (var p : gr)
            {
                var wishes = Arrays.stream(p.getWishlist()).boxed().collect(Collectors.toSet());
                var nbrWishes = new ImmutableHashSet<>(current.groups().get(i)).intersection(wishes).size();

                var name =
                        current.isWishListMode() ?
                                "%s (Önskningar: %d)".formatted(p.getName(), nbrWishes) :
                                p.getName();

                y += SPACER / 5 + fm.getHeight();
                tb.add(new PersonBox(name, x, y, Utils.FOREGROUND_COLOR, p.getId()));
            }
        }

        return ImmutableArray.fromList(tb);
    }
}
