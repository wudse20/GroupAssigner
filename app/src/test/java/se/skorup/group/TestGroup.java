package se.skorup.group;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGroup
{
    public static Stream<Arguments> getEqualsHashCodeData()
    {
        var gm1 = new Group("Kaka");
        gm1.registerPerson("Anton");
        gm1.registerPerson("Sebbe");

        var gm2 = new Group("Kaka");
        gm2.registerPerson("Anton");
        gm2.registerPerson("Sebbe");

        var gm3 = new Group("Kaka");
        var idA = gm3.registerPerson("Anton");
        var idS = gm3.registerPerson("Sebbe");
        gm3.addWishItem(idA, idS);

        var gm4 = new Group("Kaka");
        idA = gm4.registerPerson("Anton");
        idS = gm4.registerPerson("Sebbe");
        gm4.addWishItem(idA, idS);

        var gm5 = new Group("Kaka");
        idA = gm5.registerPerson("Anton");
        idS = gm5.registerPerson("Sebbe");
        gm5.addDenyItem(idA, idS);

        var gm6 = new Group("Kaka");
        idA = gm6.registerPerson("Anton");
        idS = gm6.registerPerson("Sebbe");
        gm6.addDenyItem(idA, idS);

        return Stream.of(
            Arguments.of(new Group("KAKA"), new Group("KAKA"), true),
            Arguments.of(new Group("Kaka"), new Group("KAKA"), false),
            Arguments.of(gm1, gm1, true),
            Arguments.of(gm2, gm2, true),
            Arguments.of(gm1, gm2, true),
            Arguments.of(gm3, gm3, true),
            Arguments.of(gm1, gm3, false),
            Arguments.of(gm4, gm4, true),
            Arguments.of(gm3, gm4, true),
            Arguments.of(gm5, gm5, true),
            Arguments.of(gm4, gm5, false),
            Arguments.of(gm1, gm5, false),
            Arguments.of(gm6, gm6, true),
            Arguments.of(gm5, gm6, true)
        );
    }

    public static Stream<Arguments> getTestSizes()
    {
        return Stream.of(
            Arguments.of(10),
            Arguments.of(100),
            Arguments.of(1000),
            Arguments.of(10000),
            Arguments.of(100000),
            Arguments.of(1000000)
        );
    }

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
            assertEquals(i + 1, gr.size(), "The size should match!");
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
        assertEquals(cnt, gr.size(), "The size must match!");
    }

    @Test
    public void testRegisterMultithreaded() throws InterruptedException
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
        }, "testRegisterMultithreaded-1");

        var t2 = new Thread(() -> {
            for (var i = cnt/2; i < cnt; i++)
            {
                var name = UUID.randomUUID().toString();
                al.add(name);
                ids.add(gr.registerPerson(name));
            }
        }, "testRegisterMultithreaded-2");

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
        assertEquals(cnt, gr.size(), "The size must match!");
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
    public void testDenyMultithreaded() throws InterruptedException
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
            }, "testDenyMultithreaded-" + i));
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
    public void testWishMultithreaded() throws InterruptedException
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
            }, "testWishMultithreaded-" + i));
        }

        threads.forEach(Thread::start);
        for (var t : threads)
            t.join();
    }

    @Test
    public void testModifyIds()
    {
        var gm = new Group("kaka");
        gm.registerPerson("hej");
        gm.registerPerson("på");
        gm.registerPerson("dig");
        var ids = gm.getIds();
        ids.add(4123);
        assertNotEquals(ids, gm.getIds(), "The ids should not match since the ids has been updated");
    }

    @Test
    public void testModifyPerson()
    {
        var gm = new Group("kaka");
        gm.registerPerson("hej");
        gm.registerPerson("på");
        gm.registerPerson("dig");
        var people = gm.getPersons();
        people.add(new Person("Krokodil", 12312));
        assertNotEquals(people, gm.getPersons(), "The people should not match since the people has been updated");
    }

    @Test
    public void testModifyNames()
    {
        var gm = new Group("kaka");
        gm.registerPerson("hej");
        gm.registerPerson("på");
        gm.registerPerson("dig");
        var names = gm.getNames();
        names.add("KAKA");
        assertNotEquals(names, gm.getNames(), "The names should not match since the names has been updated");
    }

    @ParameterizedTest
    @MethodSource("getEqualsHashCodeData")
    public void testEqualsHashCode(Group g1, Group g2, boolean expected)
    {
        assertEquals(expected, g1.hashCode() == g2.hashCode(), "HASHCODE FUNCTION IS WRONG");
        assertEquals(expected, g1.equals(g2), "EQUALS FUNCTION IS WRONG");
    }

    @Test
    public void testGetDeniedIds()
    {
        var gm = new Group("Kaka");
        var id1 = gm.registerPerson("Kaka 1");
        var id2 = gm.registerPerson("Kaka 2");
        var id3 = gm.registerPerson("Kaka 3");
        gm.addDenyItem(id1, id2);

        assertEquals(Set.of(id2), gm.getDeniedIds(id1), "Kaka 1");
        assertEquals(Set.of(id1), gm.getDeniedIds(id2), "Kaka 2");
        assertEquals(Set.of(), gm.getDeniedIds(id3), "Kaka 3");
    }

    @Test
    public void testGetDeniedIdsInstance()
    {
        var gm = new Group("Kaka");
        var id1 = gm.registerPerson("Kaka 1");
        gm.registerPerson("Kaka 2");
        gm.registerPerson("Kaka 3");

        assertNotSame(gm.getDeniedIds(id1), gm.getDeniedIds(id1), "Should be a new instance each time.");
    }

    @Test
    public void testGetWishedIds()
    {
        var gm = new Group("Kaka");
        var id1 = gm.registerPerson("Kaka 1");
        var id2 = gm.registerPerson("Kaka 2");
        var id3 = gm.registerPerson("Kaka 3");
        gm.addWishItem(id1, id2);
        gm.addWishItem(id1, id3);
        gm.addWishItem(id2, id1);

        assertEquals(Set.of(id2, id3), gm.getWishedIds(id1), "Kaka 1");
        assertEquals(Set.of(id1), gm.getWishedIds(id2), "Kaka 2");
        assertEquals(Set.of(), gm.getWishedIds(id3), "Kaka 3");
    }

    @Test
    public void testGetWishedIdsInstance()
    {
        var gm = new Group("Kaka");
        var id1 = gm.registerPerson("Kaka 1");
        gm.registerPerson("Kaka 2");
        gm.registerPerson("Kaka 3");

        assertNotSame(gm.getWishedIds(id1), gm.getWishedIds(id1), "Should be a new instance each time.");
    }

    @ParameterizedTest
    @MethodSource("getTestSizes")
    public void testRemoveSingleThreaded(int testSize)
    {
        var gm = new Group("Kaka");

        var remove = new HashSet<Integer>();
        var rand = new Random("Kaka".hashCode());
        for (var i = 0; i < testSize; i++)
        {
            var id = gm.registerPerson(UUID.randomUUID().toString());

            if (rand.nextBoolean())
                remove.add(id);
        }

        assertEquals(testSize, gm.size(), "The size after registering is wrong.");

        for (var id : remove)
            gm.removePerson(id);

        assertEquals(testSize - remove.size(), gm.size(), "The size after removal is wrong");
    }

    @ParameterizedTest
    @MethodSource("getTestSizes")
    public void testDeleteMultithreaded(int testSize) throws InterruptedException
    {
        var gm = new Group("Kaka");
        Set<Integer> remove = ConcurrentHashMap.newKeySet();

        var t1 = new Thread(() -> {
            var toRemove = new HashSet<Integer>();
            var rand = new Random("testDeleteMultithreaded-1".hashCode());
            for (var i = 0; i < testSize / 2; i++)
            {
                var id = gm.registerPerson(UUID.randomUUID().toString());

                if (rand.nextBoolean())
                {
                    remove.add(id);
                    toRemove.add(id);
                }
            }

            for (var id : toRemove)
                gm.removePerson(id);
        }, "testDeleteMultithreaded-1");

        var t2 = new Thread(() -> {
            var toRemove = new HashSet<Integer>();
            var rand = new Random("testDeleteMultithreaded-2".hashCode());
            for (var i = 0; i < testSize / 2; i++)
            {
                var id = gm.registerPerson(UUID.randomUUID().toString());

                if (rand.nextBoolean())
                {
                    remove.add(id);
                    toRemove.add(id);
                }
            }

            for (var id : toRemove)
                gm.removePerson(id);
        }, "testDeleteMultithreaded-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(testSize - remove.size(), gm.size(), "The size after removing is wrong.");
    }

    @Test
    public void testRemoveDenyItem()
    {
        var gm1 = new Group("Kaka");
        var id1 = gm1.registerPerson("Kalle");
        var id2 = gm1.registerPerson("Liza");
        gm1.addDenyItem(id1, id2);
        assertTrue(gm1.isDenied(id1, id2), "Before removing they should be denied.");
        assertTrue(gm1.isDenied(id2, id1), "Before removing they should be denied.");
        gm1.removeDenyItem(id1, id2);
        assertFalse(gm1.isDenied(id1, id2), "After removing they should be denied.");
        assertFalse(gm1.isDenied(id2, id1), "After removing they should be denied.");
    }

    @Test
    public void testRemoveWishItem()
    {
        var gm1 = new Group("Kaka");
        var id1 = gm1.registerPerson("Kalle");
        var id2 = gm1.registerPerson("Liza");
        gm1.addWishItem(id1, id2);
        assertTrue(gm1.getWishedIds(id1).contains(id2), "Before removing the wish should be registered.");
        assertFalse(gm1.getWishedIds(id2).contains(id1), "wished should not have wisher.");
        gm1.removeWishItem(id1, id2);
        assertFalse(gm1.getWishedIds(id1).contains(id2), "After removing the wish shouldn't be registered.");
        assertFalse(gm1.getWishedIds(id2).contains(id1), "wished should not have wisher.");
    }
}
