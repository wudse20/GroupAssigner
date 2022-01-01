package se.skorup.main.groups.creators;

import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.List;
import java.util.Set;

/**
 * The class used to generate random groups.
 * */
public class RandomGroupCreator extends GroupCreatorTemplate
{
    /**
     * Creates a random group creator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
    public RandomGroupCreator(GroupManager gm)
    {
        super(gm);
    }

    @Override
    protected Person getPerson(
        Set<Integer> current, List<Person> candidates, Set<Tuple> wish,
        Set<Tuple> deny, Set<Integer> added, Person p
    ) throws NoGroupAvailableException
    {
        return getAllowedPerson(deny, current, candidates, getRandomPerson(candidates));
    }

    @Override
    public String toString()
    {
        return "Slumpmässig grupp";
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof RandomGroupCreator gc &&
               this.toString().equals(gc.toString());
    }
}
