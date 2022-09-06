package se.skorup.API.collections.mutable_collections;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.collections.immutable_collections.ImmutableArray;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestBlockingQueue
{
    public static Stream<Arguments> getTestData()
    {
        var mediumList = ImmutableArray.fill(10000, () -> UUID.randomUUID().toString()).toList();
        var bigList = ImmutableArray.fill(100000, () -> UUID.randomUUID().toString()).toList();
        var hugeList = ImmutableArray.fill(1000000, () -> UUID.randomUUID().toString()).toList();

        return Stream.of(
            Arguments.of(List.of("Hej", "Hopp", "Kaka", "Banan")),
            Arguments.of(List.of("Äpple", "Päron", "Banan", "Krokodil", "Senap", "Ketchup")),
            Arguments.of(mediumList),
            Arguments.of(bigList),
            Arguments.of(hugeList)
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testBlockingQueueSingleThreadEnqueue(List<String> data) throws InterruptedException
    {
        var bq = new BlockingQueue<String>();

        for (var elem : data)
            bq.enqueue(elem);

        singleThreadTester(data, bq);
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testBlockingQueueSingleThreadAddAll(List<String> data) throws InterruptedException
    {
        var bq = new BlockingQueue<String>();
        bq.addAll(data);

        singleThreadTester(data, bq);
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testBlockingQueueMultiThreadedEnqueue(List<String> data) throws InterruptedException
    {
        var bq = new BlockingQueue<String>();
        var l1 = data.subList(0, data.size() / 2);
        var l2 = data.subList(data.size() / 2, data.size());

        var t1 = new Thread(() -> threadBuilder(bq, l1), "L1");
        var t2 = new Thread(() -> threadBuilder(bq, l2), "L2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        var count = 0;
        var set = new HashSet<>(data);
        while(!bq.isEmpty()) // Safe, since all other threads using the queue has died.
        {
            var elem = bq.dequeue();
            assertTrue(set.contains(elem), "Element not in input: %s".formatted(elem));
            count++;
        }

        assertEquals(data.size(), count, "Missing elements...");
    }

    private static void singleThreadTester(List<String> data, BlockingQueue<String> bq) throws InterruptedException
    {
        var count = 0;
        for (var elem : data)
        {
            var q = bq.dequeue();
            count++;
            assertEquals(elem, q, "Removing not in correct order.");
        }

        assertEquals(data.size(), count, "Missing elements...");
        assertTrue(bq.isEmpty(), "The queue is not EMPTY!!! :("); // Safe, since the queue is
    }

    private static void threadBuilder(BlockingQueue<String> bq, List<String> l2)
    {
        try
        {
            for (var elem : l2)
                bq.enqueue(elem);
        }
        catch (InterruptedException e)
        {
            fail(e.getLocalizedMessage());
        }
    }
}
