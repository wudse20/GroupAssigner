package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.GroupCreationFailedException;
import se.skorup.main.gui.helper.progress.Progress;
import se.skorup.main.manager.Group;
import se.skorup.main.objects.Person;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * The group creator for totally random groups. This generator
 * will not generate multiple subgroup alternatives. It will
 * pick an id whilst respecting the denylist.
 * */
public class RandomGroupCreator extends GroupCreatorTemplate
{
    private final Progress p;
    private int delta = -1;

    /**
     * Creates a new RandomGroupCreator. Does nothing, actually.
     *
     * @param p the progress tracker.
     * */
    public RandomGroupCreator(Progress p)
    {
        this.p = p;
    }

    @Override
    protected int getNextPerson(
            Group gm, Set<Integer> left,
            Set<Integer> current, int lastId
    ) throws GroupCreationFailedException
    {
        if (delta == -1)
            delta = 1_000_000 / gm.getMemberCountOfRole(Person.Role.CANDIDATE);

        var leftList = new ArrayList<>(left);

        while (!leftList.isEmpty())
        {
            var p = leftList.get(new Random().nextInt(leftList.size()));

            if (isPersonAllowed(p, current, gm))
            {
                left.remove(p);
                this.p.onProgress(delta);
                return p;
            }
            else
            {
                leftList.remove(p);
            }
        }

        // If we are here we have failed.
        this.p.onProgress(delta);
        throw new GroupCreationFailedException("Too many denylist items");
    }

    @Override
    public String toString()
    {
        return "Slumpmässig grupp";
    }
}
