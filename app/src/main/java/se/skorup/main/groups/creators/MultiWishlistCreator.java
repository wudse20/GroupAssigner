package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * This generates a lot of groups and select the best
 * according to a given formula.
 * */
public class MultiWishlistCreator implements GroupCreator
{
    private final GroupManager gm;

    /**
     * Creates a new GroupCreator.
     *
     * @param gm the creator used to create the groups.
     * */
    public MultiWishlistCreator(GroupManager gm)
    {
        this.gm = gm;
    }

    /**
     * Calculates the score of a group result.
     *
     * @param groups the groups.
     * @return the score of the group.
     * */
    private int getScore(List<Set<Integer>> groups)
    {
        var candidates = new ImmutableHashSet<>(gm.getAllOfRoll(Person.Role.CANDIDATE));
        var n = candidates.size();
        var x = new int[n];

        for (var p : candidates)
        {
            var wishes = new ImmutableHashSet<>(Tuple.imageOf(gm.getWishGraph(), p.getId()));
            ImmutableHashSet<Integer> group = null;
            for (var g : groups)
            {
                if (g.contains(p.getId()))
                {
                    group = new ImmutableHashSet<>(g);
                    break;
                }
            }

            assert group != null;
            x[wishes.intersection(group).size()] += 1;
        }

        var numerator = 0;
        for (var i = 1; i < x.length; i++)
            numerator += Math.pow(x[i], n - i);

        var psi = (numerator / n) - 100 * x[0];
        printStatistics(n, x, psi);
        return psi;
    }

    /**
     * Prints the statistics of this generation.
     *
     * @param n the number of persons in the group.
     * @param x {@code x[i]} corresponds to number of people
     *          that got {@code i} wishes.
     * @param psi the result of the value that were calculated
     *            for this generation.
     * */
    private void printStatistics(int n, int[] x, int psi)
    {
        var sb = new StringBuilder();
        sb.append("Antal personer: ").append(n).append('\n');
        sb.append("Beräknad konstant (PSI): ").append(psi).append("\n".repeat(2));
        sb.append("Antal personer som fått antal önskningar");

        for (int i = 0; i < x.length; i++)
            sb.append(i).append(" önskningar: ").append(x[i]).append('\n');

        var f = new File(Utils.getFolderName() + "/group_data/");
        f.mkdirs();
        var f2 = new File(
        f.getAbsolutePath() + "/" +
            DebugMethods.getCurrentDateAndTime().replace(':', '-') + ".txt"
        );

        int i = 1;
        var name = f2.getAbsolutePath().substring(0, f2.getAbsolutePath().lastIndexOf('.'));
        while (f2.exists())
            f2 = new File(name + "(" + i++ + ").txt");

        Utils.writeToFile(sb.toString(), f2);
    }

    /**
     * Finds the best group according to a given score.
     *
     * @param ga the action used to generate the group.
     * */
    private List<Set<Integer>> getBestGroup(GroupAction ga) throws NoGroupAvailableException
    {
        var allGroups = new ArrayList<List<Set<Integer>>>();

        for (var p : gm.getAllOfRoll(Person.Role.CANDIDATE))
        {
            var gc = new AlternateWishlistGroupCreator(gm, p);
            var res = ga.action(gc);
            allGroups.add(res);
        }

        return allGroups.stream()
                .map(x -> new IntermediateResult(x, getScore(x)))
                .max(Comparator.comparingInt(a -> a.score))
                .orElse(new IntermediateResult(allGroups.get(0), 0))
                .groups();
    }

    @Override
    public List<Set<Integer>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        return getBestGroup(a -> a.generateGroup(size, overflow));
    }

    @Override
    public List<Set<Integer>> generateGroup(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        return getBestGroup(a -> a.generateGroup(sizes));
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar alternativ 3";
    }

    private record IntermediateResult(List<Set<Integer>> groups, int score) {}
    private interface GroupAction { List<Set<Integer>> action(AlternateWishlistGroupCreator awgc); }
}
