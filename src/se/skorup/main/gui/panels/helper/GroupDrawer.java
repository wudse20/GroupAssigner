package se.skorup.main.gui.panels.helper;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.objects.PersonBox;
import se.skorup.main.gui.objects.TextBox;
import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * A class that draws the subgroups in
 * the SubgroupPanel.
 * */
public abstract class GroupDrawer
{
    protected final SubgroupPanel sgp;

    protected final GroupManager gm;

    protected final Subgroups current;

    protected final FontMetrics fm;


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
     * Generates the positions for the text boxes.
     *
     * @param groups the groups.
     * @param max the max size.
     * @return the positions of the text boxes.
     * */
    protected abstract List<Tuple> generatePositions(List<List<Person>> groups, int max);

    /**
     * Calculates the height of the panel.
     *
     * @return the calculated height of the panel.
     * */
    public abstract int height();

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
        var positions = generatePositions(groups, max);

        for (var i = 0; i < groups.size(); i++)
        {
            var x = positions.get(i).a();
            var y = positions.get(i).b();

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

                y += sgp.getSpacer() / 5 + fm.getHeight();
                tb.add(new PersonBox(name, x, y, Utils.FOREGROUND_COLOR, p.getId()));
            }
        }

        return ImmutableArray.fromList(tb);
    }
}
