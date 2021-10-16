package se.skorup.main.gui.panels.helper;

import se.skorup.main.gui.panels.SubgroupPanel;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Subgroups;
import se.skorup.main.objects.Tuple;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

public class SingleColumnGroupDrawer extends GroupDrawer {

    /**
     * Creates a new GroupDrawer.
     *
     * @param sgp     the instance of the SubgroupPanel.
     * @param gm      the currently used GroupManager.
     * @param current the instance of the current subgroups.
     * @param fm      the font metrics of the frame.
     */
    public SingleColumnGroupDrawer(SubgroupPanel sgp, GroupManager gm, Subgroups current, FontMetrics fm)
    {
        super(sgp, gm, current, fm);
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
        var x = sgp.getWidth() / 2 - offset;

        for (int i = 0; i < groups.size(); i++)
        {
            var y = sgp.getSpacer() * (i + 4) + i * (sgp.getSpacer() + fm.getHeight()) * groups.get(i).size();
            res.add(new Tuple(x, y));
        }

        return res;
    }

    @Override
    public int height()
    {
        var size = current.groups().size();
        return sgp.getSpacer() * (size + 4) + size * (sgp.getSpacer() + fm.getHeight()) * current.groups().get(0).size();
    }
}
