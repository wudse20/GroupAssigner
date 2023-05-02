package se.skorup.group.generation;

import se.skorup.group.Group;
import se.skorup.group.progress.Progress;
import se.skorup.util.collections.ImmutableHashSet;
import se.skorup.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public AtomicBoolean hasStarted = new AtomicBoolean(false);
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
        var persons = gm.getIds();
        var x = new int[persons.size()];
        var highestCount = 0;

        for (var p : persons)
        {
            var cnt = 0;
            var wished = ImmutableHashSet.fromCollection(gm.getWishedIds(p));

            for (var g : groups)
            {
                if (g.contains(p))
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
        Log.debugf(
            "Starting generation of subgroups: %d producers, %d consumers%n",
            producers, consumers
        );

        final var cl = new CountDownLatch(gm.size());
        var tpProd = Executors.newFixedThreadPool(producers);
        var process = new LinkedBlockingQueue<Result>();
        var persons = gm.getIds();
        var delta = 1_000_000 / (persons.size() * 2); // The delta that should be added each time.

        for (var id : persons)
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
                    Log.errorf("Error in creating groups: %s", e.getLocalizedMessage());
                    throw e;
                }
                finally
                {
                    // Updates the progress and adds it to the progress bar.
                    progress.onProgress(delta);
                }

                Log.debugf("Starting with: %d, Found: %s%n", id, res);
                var score = getScore(res, gm);
                Log.debugf("Score: %s%n", score);
                process.add(new Result(res, score));
                Log.debug("Produced a candidate for a group");
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
                        Log.debug(cl.getCount());
                    }
                }
                catch (InterruptedException e)
                {
                    return; // Just want it to exit, so has returned to make it more readable.
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
                Log.error("GroupCreation timed out!");
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
    public List<List<Set<Integer>>> generate(Group gm, List<Integer> sizes) throws GroupCreationFailedException
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