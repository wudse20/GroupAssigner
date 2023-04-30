package se.skorup.group;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGroup
{
    @Test
    public void testRegisterSingleThread()
    {
        var cnt = 10000;
        var al = new ArrayList<String>();
        var ids = new HashSet<Integer>();
        var gr = new Group("Test");

        for (var i = 0; i < cnt; i++)
        {
            var name = UUID.randomUUID().toString();
            al.add(name);
            ids.add(gr.registerPerson(name));
        }

        assertEquals(
            al.size(), gr.getNames().size(),
            "There should be exactly %d names, got: %d"
            .formatted(cnt, gr.getNames().size())
        );

        assertEquals(al, gr.getNames(), "All names should be there.");

        assertEquals(
            al.size(), gr.getIds().size(),
            "There should be exactly %d ids, got: %d"
            .formatted(cnt, gr.getIds().size())
        );

        assertEquals(ids, gr.getIds(), "All ids should be there.");
    }

    @Test
    public void testRegisterMultiThreaded() throws InterruptedException
    {
        var cnt = 10000;
        var al = Collections.synchronizedList(new ArrayList<String>());
        Set<Integer> ids = ConcurrentHashMap.newKeySet();
        var gr = new Group("Test");

        var t1 = new Thread(() -> {
            for (var i = 0; i < cnt / 2; i++)
            {
                var name = UUID.randomUUID().toString();
                al.add(name);
                ids.add(gr.registerPerson(name));
            }
        }, "testRegisterMultiThreaded-1");

        var t2 = new Thread(() -> {
            for (var i = cnt/2; i < cnt; i++)
            {
                var name = UUID.randomUUID().toString();
                al.add(name);
                ids.add(gr.registerPerson(name));
            }
        }, "testRegisterMultiThreaded-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(
            al.size(), gr.getNames().size(),
            "There should be exactly %d names, got: %d"
            .formatted(cnt, gr.getNames().size())
        );

        al.sort(String::compareTo);
        var names = gr.getNames().stream().sorted(String::compareTo).toList();
        assertEquals(al, names, "All names should be there.");

        assertEquals(
            al.size(), gr.getIds().size(),
            "There should be exactly %d ids, got: %d"
            .formatted(cnt, gr.getIds().size())
        );

        assertEquals(ids, gr.getIds(), "All ids should be there.");
    }

    @Test
    public void testDenySingleThreaded()
    {
        var gr = new Group("Test");
        var deny = new HashMap<Integer, Set<Integer>>();
        var cnt = 10000;

        // Register the persons
        for (var i = 0; i < cnt; i++)
            gr.registerPerson(UUID.randomUUID().toString());

        // Add deny items
        var random = new Random("kaka".hashCode()); // Seed to be the same each time.
        for (var i = 0; i < cnt; i++)
        {
            var id1 = random.nextInt(0, cnt);
            var id2 = random.nextInt(0, cnt);
            do
            {
                id2 = random.nextInt(0, cnt);
            } while(id1 == id2);

            gr.addDenyItem(id1, id2);
            var s1 = deny.getOrDefault(id1, new HashSet<>());
            var s2 = deny.getOrDefault(id2, new HashSet<>());

            s1.add(id2);
            s2.add(id1);

            deny.put(id1, s1);
            deny.put(id2, s2);
        }

        // Test with isDenied
        for (var e : deny.entrySet())
        {
            var id = e.getKey();
            assertFalse(gr.isDenied(id, id), "%d should not be denied with itself.".formatted(id));

            for (var p : e.getValue())
            {
                assertTrue(gr.isDenied(id, p), "%d should be on %d's denylist".formatted(p, id));
                assertTrue(gr.isDenied(p, id), "%d should be on %d's denylist".formatted(id, p));
            }
        }
    }

    @Test
    public void testDenyMultiThreaded() throws InterruptedException
    {
        var gr = new Group("Test");
        var deny = new ConcurrentHashMap<Integer, Set<Integer>>();
        var cnt = 10000;

        // Register the persons
        for (var i = 0; i < cnt; i++)
            gr.registerPerson(UUID.randomUUID().toString());

        // Add deny items
        var random = new Random("kaka".hashCode()); // Seed to be the same each time.
        for (var i = 0; i < cnt; i++)
        {
            var id1 = random.nextInt(0, cnt);
            var id2 = random.nextInt(0, cnt);
            do
            {
                id2 = random.nextInt(0, cnt);
            } while(id1 == id2);

            gr.addDenyItem(id1, id2);
            var s1 = deny.getOrDefault(id1, new HashSet<>());
            var s2 = deny.getOrDefault(id2, new HashSet<>());

            s1.add(id2);
            s2.add(id1);

            deny.put(id1, s1);
            deny.put(id2, s2);
        }

        var threads = new ArrayList<Thread>();
        for (var i = 0; i < 4; i++)
        {
            threads.add(new Thread(() -> {
                // Test with isDenied
                for (var e : deny.entrySet())
                {
                    var id = e.getKey();
                    assertFalse(gr.isDenied(id, id), "%d should not be denied with itself.".formatted(id));

                    for (var p : e.getValue())
                    {
                        assertTrue(gr.isDenied(id, p), "%d should be on %d's denylist".formatted(p, id));
                        assertTrue(gr.isDenied(p, id), "%d should be on %d's denylist".formatted(id, p));
                    }
                }
            }, "testDenyMultiThreaded-" + i));
        }

        threads.forEach(Thread::start);
        for (var t : threads)
            t.join();
    }

    @Test
    public void testWishSingleThreaded()
    {
        var gr = new Group("Test");
        var wish = new HashMap<Integer, Set<Integer>>();
        var cnt = 10000;

        // Register the persons
        for (var i = 0; i < cnt; i++)
            gr.registerPerson(UUID.randomUUID().toString());

        // Add wish items
        var random = new Random("kaka".hashCode()); // Seed to be the same each time.
        for (var i = 0; i < cnt; i++)
        {
            var id1 = random.nextInt(0, cnt);
            var id2 = random.nextInt(0, cnt);
            do
            {
                id2 = random.nextInt(0, cnt);
            } while(id1 == id2);

            var s1 = wish.getOrDefault(id1, new HashSet<>());
            s1.add(id2);
            wish.put(id1, s1);
            gr.addWishItem(id1, id2);
        }

        // Test with getWishedIds
        for (var e : wish.entrySet())
        {
            var id = e.getKey();
            assertFalse(gr.getWishedIds(id).contains(id), "%d should not wish for themselves.".formatted(id));
            assertEquals(gr.getWishedIds(id), e.getValue(), "These should match if everything is correct.");
        }
    }

    @Test
    public void testWishMultiThreaded() throws InterruptedException
    {
        var gr = new Group("Test");
        var wish = new HashMap<Integer, Set<Integer>>();
        var cnt = 10000;

        // Register the persons
        for (var i = 0; i < cnt; i++)
            gr.registerPerson(UUID.randomUUID().toString());

        // Add wish items
        var random = new Random("kaka".hashCode()); // Seed to be the same each time.
        for (var i = 0; i < cnt; i++)
        {
            var id1 = random.nextInt(0, cnt);
            var id2 = random.nextInt(0, cnt);
            do
            {
                id2 = random.nextInt(0, cnt);
            } while(id1 == id2);

            var s1 = wish.getOrDefault(id1, new HashSet<>());
            s1.add(id2);
            wish.put(id1, s1);
            gr.addWishItem(id1, id2);
        }

        var threads = new ArrayList<Thread>();
        for (var i = 0; i < 4; i++)
        {
            threads.add(new Thread(() -> {
                // Test with getWishedIds
                for (var e : wish.entrySet())
                {
                    var id = e.getKey();
                    assertFalse(gr.getWishedIds(id).contains(id), "%d should not wish for themselves.".formatted(id));
                    assertEquals(gr.getWishedIds(id), e.getValue(), "These should match if everything is correct.");
                }
            }, "testWishMultiThreaded-" + i));
        }

        threads.forEach(Thread::start);
        for (var t : threads)
            t.join();
    }
}
