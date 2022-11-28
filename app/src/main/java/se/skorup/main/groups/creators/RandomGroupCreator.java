package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.GroupCreationFailedException;
import se.skorup.main.manager.Group;

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
    /** Creates a new RandomGroupCreator. Does nothing, actually. */
    public RandomGroupCreator() {}

    @Override
    protected int getNextPerson(
            Group gm, Set<Integer> left,
            Set<Integer> current, int lastId
    ) throws GroupCreationFailedException
    {
        var leftList = new ArrayList<>(left);

        while (!leftList.isEmpty())
        {
            var p = leftList.get(new Random().nextInt(leftList.size()));

            if (isPersonAllowed(p, current, gm))
            {
                left.remove(p);
                return p;
            }
            else
            {
                leftList.remove(p);
            }
        }

        // If we are here we have failed.
        throw new GroupCreationFailedException("Too many denylist items");
    }

    @Override
    public String toString()
    {
        return "Slumpmässig grupp";
    }
}
