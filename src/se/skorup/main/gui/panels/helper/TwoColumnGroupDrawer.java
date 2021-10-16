package se.skorup.main.gui.panels.helper;

import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates positions for two columns of groups.
 * */
public class TwoColumnGroupDrawer extends GroupDrawer
{
    /**
     * Creates a new GroupDrawer.
     *
     * @param sgp the instance of the SubgroupPanel.
     * @param gm the currently used GroupManager.
     * @param current the instance of the current subgroups.
     * @param fm the font metrics of the frame.
     * */
    public TwoColumnGroupDrawer(SubgroupPanel sgp, GroupManager gm, Subgroups current, FontMetrics fm)
    {
        super(sgp, gm, current, fm);
    }

    @Override
    public int height()
    {
        if (current == null || fm == null)
            return 0;

        int max = Collections.max(current.groups().stream().map(Set::size).collect(Collectors.toList()));
        return 3 * sgp.getSpacer() + sgp.getSpacer() * current.groups().size() * (max + 2);
    }

    @Override
    protected List<Tuple> generatePositions(List<List<Person>> groups, int max)
    {
        var res = new ArrayList<Tuple>();
        var offset =
            groups.parallelStream()
                   .flatMapToInt(x ->
                       x.parallelStream()
                        .map(Person::getName)
                        .mapToInt(fm::stringWidth))
                   .max()
                   .orElse(0);

        offset /= 2;

        for (int i = 0; i < groups.size(); i++)
        {
            var x = (i % 2 == 0) ? sgp.getWidth() / 4 - offset: 3 * sgp.getWidth() / 4 - offset;
            var y = 3 * sgp.getSpacer() + sgp.getSpacer() * (i % groups.size() / 2) * (max + 2);

            res.add(new Tuple(x, y));
        }

        return res;
    }
}
