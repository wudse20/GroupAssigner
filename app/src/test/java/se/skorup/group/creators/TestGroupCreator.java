package se.skorup.group.creators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.group.Group;
import se.skorup.group.generation.GroupCreator;
import se.skorup.group.generation.RandomGroupCreator;
import se.skorup.group.generation.WishesGroupCreator;
import se.skorup.group.generation.WishlistGroupCreator;
import se.skorup.util.collections.ImmutableHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestGroupCreator
{
    private static final List<Group> gms;

    static
    {
        gms = new ArrayList<>();

        var gm1 = new Group("Test1");
        var gm2 = new Group("Test2");
        var gm3 = new Group("Test3");

        gms.add(gm1);
        gms.add(gm2);
        gms.add(gm3);

        var r = new Random("Kaka".hashCode());
        setUpGroup(10, gm1, r);
        setUpGroup(50, gm2, r);
        setUpGroup(100, gm3, r);
    }

    private static void setUpGroup(int members, Group gm1, Random r)
    {
        for (var i = 0; i < members; i++)
            gm1.registerPerson("Test-" + i);

        for (var i = 0; i < members; i++) {
            var id = r.nextInt(0, members);
            var id2 = r.nextInt(0, members);
            do id2 = r.nextInt(0, members); while (id == id2);
            gm1.addWishItem(id, id2);
        }
    }

    public static Stream<Arguments> getTestData()
    {
        return Stream.of(
            Arguments.of(new RandomGroupCreator(a -> {}), 2, gms.get(0)),
            Arguments.of(new WishlistGroupCreator(), 2, gms.get(0)),
            Arguments.of(new WishlistGroupCreator(0), 2, gms.get(0)),
            Arguments.of(new WishesGroupCreator(a -> {}), 2, gms.get(0)),
            Arguments.of(new RandomGroupCreator(a -> {}), 5, gms.get(1)),
            Arguments.of(new WishlistGroupCreator(), 5, gms.get(1)),
            Arguments.of(new WishlistGroupCreator(0), 5, gms.get(1)),
            Arguments.of(new WishesGroupCreator(a -> {}), 5, gms.get(1)),
            Arguments.of(new RandomGroupCreator(a -> {}), 2, gms.get(2)),
            Arguments.of(new WishlistGroupCreator(), 2, gms.get(2)),
            Arguments.of(new WishlistGroupCreator(0), 2, gms.get(2)),
            Arguments.of(new WishesGroupCreator(a -> {}), 2, gms.get(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testGroupCreator(GroupCreator gc, int size, Group gm)
    {
        if (gm == null)
            fail("Buggy test code :(");

        var gcString = gc instanceof WishlistGroupCreator wc ? wc.toString() + wc.startingPerson : gc.toString();
        var res = new AtomicReference<List<List<Set<Integer>>>>();

        assertDoesNotThrow(
            () -> res.set(gc.generate(gm, size, false)),
            "Failed group creation with %s%ngm: %s%n".formatted(gcString, gm)
        );

        for (var r : res.get())
        {
            var all = new ImmutableHashSet<Integer>();

            for (var g : r)
                all = all.union(g);

            assertEquals(
                gm.size(), all.size(),
                "The sizes doesn't match up correctly!%n All: %s,%n current: %s,%n all: %s%n creator: %s%n gm: %s%n"
                .formatted(res.get(), r, all, gcString, gm)
            );
        }
    }

    @Test
    public synchronized void testInterrupt() throws ExecutionException, InterruptedException, TimeoutException
    {
        var gc = new WishesGroupCreator(a -> {});
        var tp = Executors.newSingleThreadExecutor();

        var task = tp.submit(() -> gc.generate(gms.get(1), 6, false));

        while (!gc.hasStarted.get())
            wait(10);

        gc.interrupt();
        var res = task.get(30, TimeUnit.SECONDS);

        assertEquals(List.of(), res, "The interrupted result should be empty.");
    }
}