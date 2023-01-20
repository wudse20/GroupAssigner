package se.skorup.main.groups.creators;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.DebugMethods;
import se.skorup.main.groups.exceptions.GroupCreationFailedException;
import se.skorup.main.gui.helper.progress.Progress;
import se.skorup.main.manager.Group;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A GroupCreator that creates all the best groups
 * it can find according to the wishes. This group
 * will obey the denylist as well.
 * */
public final class WishesGroupCreator implements GroupCreator
{
    private final Monitor monitor;

    /** Only used for testing of the class. */
    AtomicBoolean hasStarted = new AtomicBoolean(false);
    private final Progress progress;

    /**
     * Creates a new WishesGroupCreator.
     */
    public WishesGroupCreator(Progress progress)
    {
        this.monitor = new Monitor();
        this.progress = progress;
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
    private double getScore(Iterable<Set<Integer>> groups, Group gm)
    {
        var candidates = gm.getAllIdsOfRoll(Person.Role.CANDIDATE);
        var x = new int[candidates.size()];
        var highestCount = 0;

        for (var c : candidates)
        {
            var cnt = 0;
            var wished =
                ImmutableHashSet.fromCollection(Tuple.imageOf(gm.getWishGraph(), c));

            for (var g : groups)
            {
                if (g.contains(c))
                {
                    cnt += wished.intersection(g).size();
                }
            }

            highestCount = Math.max(cnt, highestCount);
            x[cnt]++;
        }

        var psi = 0;
        for (var i = 1; i < highestCount; i++)
        {
            psi += Math.pow(x[i], i) / Math.pow(2, n(i));
        }

        return psi - omega(x[0]) * x[0];
    }

    /**
     * Generates the groups using a multithreaded system for generating the best alternatives.
     *
     * @param gm the ins
     * */
    private List<List<Set<Integer>>> generate(Group gm, List<Integer> sizes, boolean overflow)
    {
        var consumers = 2;
        var producers = Math.max(Runtime.getRuntime().availableProcessors() - consumers - 1, 2);
        DebugMethods.logF(
            DebugMethods.LogType.EMPHASIZE, "Starting generation of subgroups: %d producers, %d consumers%n",
            producers, consumers
        );

        final var cl = new CountDownLatch(gm.getMemberCountOfRole(Person.Role.CANDIDATE));
        var tpProd = Executors.newFixedThreadPool(producers);
        var process = new LinkedBlockingQueue<Result>();
        var candidates = gm.getAllIdsOfRoll(Person.Role.CANDIDATE);
        var delta = 1_000_000 / (candidates.size() * 2); // The delta that should be added each time.

        for (var id : candidates)
        {
            var task = tpProd.submit(() -> {
                List<Set<Integer>> res;

                try
                {
                    if (sizes.size() == 1)
                        res = new WishlistGroupCreator().generate(gm, sizes.get(0), overflow).get(0);
                    else
                        res = new WishlistGroupCreator().generate(gm, sizes).get(0);
                }
                catch (GroupCreationFailedException e)
                {
                    cl.countDown(); // Skip if fails.
                    DebugMethods.errorF("Error in creating groups: %s", e.getLocalizedMessage());
                    throw e;
                }
                finally
                {
                    // Updates the progress and adds it to the progress bar.
                    progress.onProgress(delta);
                }

                DebugMethods.logF(DebugMethods.LogType.DEBUG, "Starting with: %d, Found: %s%n", id, res);
                var score = getScore(res, gm);
                DebugMethods.logF(DebugMethods.LogType.DEBUG, "Score: %s%n", score);
                process.add(new Result(res, score));
                DebugMethods.log("Produced a candidate for a group", DebugMethods.LogType.DEBUG);
            });
            monitor.addTask(task);
        }

        for (var i = 0; i < consumers; i++)
        {
            var t = new Thread(() -> {
                try
                {
                    while (true)
                    {
                        var r = process.take();
                        monitor.updateResult(r.groups, r.score);
                        progress.onProgress(delta);
                        cl.countDown();
                        DebugMethods.log(cl.getCount(), DebugMethods.LogType.DEBUG);
                    }
                }
                catch (InterruptedException e)
                {
                    return; // Just want it to exit, so has return to make it more readable.
                }
            }, "WishesGroupCreator-Consumer: " + i);
            t.start();
            monitor.addThread(t);
        }

        try
        {
            monitor.addClThread(Thread.currentThread());
            hasStarted.set(true);
            var success = cl.await(5, TimeUnit.MINUTES);

            if (!success)
            {
                DebugMethods.error("GroupCreation timed out!");
                tpProd.shutdownNow();
                return List.of();
            }
        }
        catch (InterruptedException e)
        {
            tpProd.shutdownNow();
            return List.of(); // Want it to exit and return nothing.
        }

        progress.onProgress(1_000_000);
        tpProd.shutdownNow();
        return monitor.getResult().stream().toList();
    }

    @Override
    public List<List<Set<Integer>>> generate(
        Group gm, int size, boolean overflow
    ) throws GroupCreationFailedException, IllegalArgumentException
    {
        return generate(gm, Collections.singletonList(size), overflow);
    }

    @Override
    public List<List<Set<Integer>>> generate(
        Group gm, List<Integer> sizes
    ) throws GroupCreationFailedException
    {
        return generate(gm, sizes, false);
    }

    /**
     * This will interrupt the creation and cancel
     * the creation resulting in an empty list being
     * returned.
     */
    @Override
    public void interrupt()
    {
        monitor.interrupt();
    }

    @Override
    public String toString()
    {
        return "Skapa grupper efter önskningar";
    }

    private record Result(List<Set<Integer>> groups, double score) {}

    private static final class Monitor
    {
        private final List<Future<?>> tasks;
        private final List<Thread> threads;
        private final Set<List<Set<Integer>>> result;

        private Thread clThread;

        private double currentBest = Double.NEGATIVE_INFINITY;

        private Monitor()
        {
            this.tasks = new ArrayList<>();
            this.threads = new ArrayList<>();
            this.result = new HashSet<>();
        }

        private synchronized void updateResult(List<Set<Integer>> groups, double score)
        {
            if (score > currentBest)
            {
                result.clear();
                result.add(groups);
                currentBest = score;
            }
            else if (score == currentBest)
            {
                result.add(groups);
            }
        }

        /** NOTE TO SELF: CALL ONLY WHEN FINISHED. */
        private synchronized Set<List<Set<Integer>>> getResult()
        {
            return result;
        }

        private synchronized void addTask(Future<?> task)
        {
            tasks.add(task);
        }

        private synchronized void addThread(Thread t)
        {
            threads.add(t);
        }

        private synchronized void addClThread(Thread t)
        {
            clThread = t;
        }

        private synchronized void interrupt()
        {
            if (clThread != null)
                clThread.interrupt();

            for (var t : threads)
            {
                t.interrupt();
            }

            for (var t : tasks)
            {
                t.cancel(true);
            }
        }
    }
}
