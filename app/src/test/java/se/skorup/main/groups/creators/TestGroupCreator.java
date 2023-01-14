package se.skorup.main.groups.creators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.FormsParser;
import se.skorup.API.util.MyFileReader;
import se.skorup.main.manager.Group;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.io.File;
import java.util.List;
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
        List<Group> tmp = List.of(
            new GroupManager(""),
            new GroupManager("")
        );

        try
        {
            tmp = List.of(
                new GroupManager("Kaka"),
                new GroupManager("Kaka2")
            );

            FormsParser.parseFormData(
                MyFileReader.readFile(new File("./src/test/testData/test_data.csv")),
                tmp.get(0)
            );

            FormsParser.parseFormData(
                MyFileReader.readFile(new File("./src/test/testData/test_data_some_what_bigger.csv")),
                tmp.get(1)
            );
        }
        catch (Exception e)
        {
            fail("Buggy test code! (Or missing files :()");
        }
        finally
        {
            gms = tmp;
        }
    }

    public static Stream<Arguments> getTestData()
    {
        return Stream.of(
            Arguments.of(new RandomGroupCreator(), 3, gms.get(0)),
            Arguments.of(new WishlistGroupCreator(), 3, gms.get(0)),
            Arguments.of(new WishlistGroupCreator(0), 3, gms.get(0)),
            Arguments.of(new WishesGroupCreator(a -> {}), 3, gms.get(0)),
            Arguments.of(new RandomGroupCreator(), 10, gms.get(1)),
            Arguments.of(new WishlistGroupCreator(), 10, gms.get(1)),
            Arguments.of(new WishlistGroupCreator(0), 10, gms.get(1)),
            Arguments.of(new WishesGroupCreator(a -> {}), 10, gms.get(1))
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
               gm.getAllIdsOfRoll(Person.Role.CANDIDATE).size(), all.size(),
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
