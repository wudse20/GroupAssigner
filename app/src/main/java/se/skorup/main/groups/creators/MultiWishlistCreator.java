package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.groups.exceptions.NoGroupAvailableException;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

    private int n(int i)
    {
        return (int) (0.5d * i * i + 2.5d * i);
    }

    private double omega(int x)
    {
        return x <= 0 ? 0 : 10 * Math.exp(x);
    }

    /**
     * Calculates the score of a group result.
     *
     * @param groups the groups.
     * @return the score of the group.
     * */
    private double getScore(Collection<Set<Integer>> groups)
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

        var w = 0; // Highest wish count
        for (int i = 0; i < x.length; i++)
            if (x[i] != 0)
                w = i;

        var numerator = 0d;
        for (var i = 0; i < w; i++)
            numerator += Math.pow(x[i + 1], i) / (Math.pow(2, n(i)));

        var psi = numerator - omega(x[0]);
        DebugMethods.log("PSI: %f".formatted(psi), DebugMethods.LogType.DEBUG);
        printStatistics(n, x, w, psi);
        return psi;
    }

    /**
     * Prints the statistics of this generation.
     *
     * @param n the number of persons in the group.
     * @param x {@code x[i]} corresponds to number of people
     *          that got {@code i} wishes.
     * @param w the maximum amount of wishes a person got granted.
     * @param psi the result of the value that were calculated
     *            for this generation.
     * */
    private void printStatistics(int n, int[] x, int w, double psi)
    {
        var sb = new StringBuilder();
        sb.append("Antal personer: ").append(n).append('\n');
        sb.append("Beräknad konstant (PSI): ").append(psi).append('\n');
        sb.append("Maximalt antal önskningar: ").append(w).append("\n".repeat(2));
        sb.append("Antal personer som fått antal önskningar: \n");

        for (int i = 0; i < w + 1 && i < x.length; i++)
            sb.append(i).append("st önskningar: ").append(x[i]).append('\n');

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
     * @return the best groups that has the same and highest score.
     * */
    private List<List<Set<Integer>>> getBestGroups(GroupAction ga) throws NoGroupAvailableException
    {
        var allGroups = new HashSet<Set<Set<Integer>>>();

        for (var p : gm.getAllOfRoll(Person.Role.CANDIDATE))
        {
            var gc = new WishlistGroupCreator(gm, p);
            var res = new HashSet<>(ga.action(gc));
            allGroups.add(res);
        }

        var max =
            allGroups.stream()
                     .mapToDouble(this::getScore)
                     .max()
                     .orElse(Double.MIN_VALUE);

        var res = new ArrayList<List<Set<Integer>>>();
        allGroups.stream()
                 .map(g -> new IntermediateResult(new ArrayList<>(g), getScore(g)))
                 .forEach(ir -> {
                     if (ir.score == max)
                         res.add(ir.groups);
                 });

        DebugMethods.log("Score of group (PSI): %f".formatted(max), DebugMethods.LogType.DEBUG);

        return res;
    }

    @Override
    public List<List<Set<Integer>>> generateGroup(int size, boolean overflow) throws NoGroupAvailableException
    {
        return getBestGroups(a -> a.generateGroup(size, overflow).get(0));
    }

    @Override
    public List<List<Set<Integer>>> generateGroupNbrGroups(List<Integer> sizes) throws IllegalArgumentException, NoGroupAvailableException
    {
        return getBestGroups(a -> a.generateGroupNbrGroups(sizes).get(0));
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar";
    }

    private record IntermediateResult(List<Set<Integer>> groups, double score) {}
    private interface GroupAction { List<Set<Integer>> action(WishlistGroupCreator awgc); }
}
