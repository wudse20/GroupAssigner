package se.skorup.util.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.util.collections.ImmutableHashSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestImmutableHashSet
{
    @Test
    public void testEqualsHashCode()
    {
        var set1 = new ImmutableHashSet<>(1, 2, 3);
        var set2 = new ImmutableHashSet<>(1, 2, 3);
        var set3 = new ImmutableHashSet<>(1, 2, 3, 4);

        assertEquals(set1, set2);
        assertNotEquals(set1, "hej");
        assertNotEquals(set1, null);
        assertNotEquals(set2, set3);

        assertEquals(set1.hashCode(), set2.hashCode());
    }

    @Test
    public void testIntersection()
    {
        var set1 = new ImmutableHashSet<>(1, 2, 3, 4, 5);
        var set2 = new ImmutableHashSet<>(2, 3, 4, 7, 8, 9, 12);
        var set3 = new ImmutableHashSet<>(-1, -2, -3);
        var ctr = new ImmutableHashSet<>(2, 3, 4);

        assertEquals(ctr, set1.intersection(set2));
        assertEquals(ctr, set2.intersection(set1));
        assertEquals(new ImmutableHashSet<Integer>(new HashSet<>()), set1.intersection(set3));
        assertEquals(new ImmutableHashSet<Integer>(new HashSet<>()), set3.intersection(set1));
        assertEquals(set1, set1.intersection(set1));

        assertThrows(NullPointerException.class, () -> {
            ImmutableHashSet<Integer> tmp = null;
            set1.intersection(tmp);
        });

        assertThrows(NullPointerException.class, () -> {
            Set<Integer> tmp = null;
            set1.intersection(tmp);
        });
    }

    @Test
    public void testUnion()
    {
        var set1 = new ImmutableHashSet<>(1, 2, 3, 4, 5);
        var set2 = new ImmutableHashSet<>(3, 4, 5, 6, 7);
        var ctr = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6, 7);

        assertEquals(ctr, set1.union(set2));
        assertEquals(ctr, set2.union(set1));

        assertThrows(NullPointerException.class, () -> {
            ImmutableHashSet<Integer> tmp = null;
            set1.union(tmp);
        });

        assertThrows(NullPointerException.class, () -> {
            HashSet<Integer> tmp = null;
            set1.union(tmp);
        });
    }

    @Test
    public void testSize1()
    {
        var myStringArray = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(4, myStringArray.size());
    }

    @Test
    public void testSize2()
    {
        var arr = new ImmutableHashSet<String>();
        assertEquals(0, arr.size());
    }

    @Test
    public void testEmpty()
    {
        assertTrue(new ImmutableHashSet<String>().isEmpty());
        assertFalse(new ImmutableHashSet<>("hello").isEmpty());
    }

    @Test
    public void testToList()
    {
        var set1 = new ImmutableHashSet<>("a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), set1.toList());
    }

    @Test
    public void testForAll1()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertTrue(set.forAll(x -> x > 0));
    }

    @Test
    public void testForAll2()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertFalse(set.forAll(x -> x < 0));
    }

    @Test
    public void testForAll3()
    {
        var set = new ImmutableHashSet<Integer>(null, null, null, null);
        assertFalse(set.forAll(x -> x < 0));
    }

    @Test
    public void testMap1()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");

        var ctrSet = new ImmutableHashSet<>("test1", "test2", "test3", "test4");
        var newSet = myStringSet.map(String::toLowerCase);

        var controlIterator = ctrSet.iterator();
        var newArrIterator = newSet.iterator();

        while (controlIterator.hasNext() && newArrIterator.hasNext())
        {
            assertEquals(controlIterator.next(), newArrIterator.next());
        }
    }

    @Test
    public void testMap2()
    {
        var myStringArray = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");

        var ctrSet = new ImmutableHashSet<>(1, 2, 3, 4);
        myStringArray = new ImmutableHashSet<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertEquals(ctrSet, newArr);
    }

    @Test
    public void testMkString()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "ImmutableHashSet[1, 2, 3, 4]";
        assertEquals(ctr, set.mkString(", "));
    }

    @Test
    public void testMkString2()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "ImmutableHashSet[1:;:2:;:3:;:4]";
        assertEquals(ctr, set.mkString(":;:"));
    }

    @Test
    public void testMkString3()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "ImmutableHashSet[1234]";
        assertEquals(ctr, set.mkString(""));
    }

    @Test
    public void testMkStringEmpty()
    {
        var set = new ImmutableHashSet<>();
        var ctr = "";
        assertEquals(ctr, set.mkString(", "));
    }

    @Test
    public void testDropMatching()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching("Test1");
        var ctr = new ImmutableHashSet<>("Test2", "Test3", "Test4");

        assertEquals(ctr.size(), res.size());
        assertEquals(ctr, res);
        assertNotEquals(myStringSet, res);
    }

    @Test
    public void testDropMatchingNonExistent()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching("Not in the list");
        assertEquals(myStringSet, res);
        assertEquals(myStringSet.size(), res.size());
    }

    @Test
    public void testDropMatchingEmptyInput()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching();
        assertEquals(myStringSet, res);
        assertEquals(myStringSet.size(), res.size());
    }

    @Test
    public void testContains()
    {
        var ctr = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertFalse(ctr.contains(123));
        assertTrue(ctr.contains(2));
    }

    @Test
    public void testDiff()
    {
        var a = new ImmutableHashSet<>("A", "B", "C", "D");
        var b = new ImmutableHashSet<>("C", "D", "E");
        var ctr = new ImmutableHashSet<>("A", "B");
        assertEquals(ctr, a.diff(b));

        var a2 = new ImmutableHashSet<>(10, 12, 14, 16, 18);
        var b2 = new ImmutableHashSet<>(15, 16, 17, 18, 19);
        var ctr2 = new ImmutableHashSet<>(10, 12, 14);
        assertEquals(ctr2, a2.diff(b2));

        var b3 = new ImmutableHashSet<>('c', 'd', 'e');
        assertEquals(new ImmutableHashSet<Character>(), new ImmutableHashSet<Character>().diff(b3));

        // Cast to not confuse compiler
        assertThrows(IllegalArgumentException.class, () -> a2.diff((Set<Integer>) null));
    }

    @SuppressWarnings("unchecked")
    public static Stream<Arguments> toSetData()
    {
        var s1 = new ImmutableHashSet<String>();
        var es1 = new HashSet<String>();

        var s2 = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        var es2 = new HashSet<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        var s3 = new ImmutableHashSet<>("1", "2", "3", "4", "5");
        var es3 = new HashSet<>(List.of("1", "2", "3", "4", "5"));

        var s4 = (ImmutableHashSet<HashSet<String>>) new ImmutableHashSet<>(es1, es3); // Why JAVA? WHY!
        var es4 = (HashSet<HashSet<String>>) new HashSet<>(List.of(es1, es3));

        return Stream.of(
                Arguments.of(s1, es1),
                Arguments.of(s2, es2),
                Arguments.of(s3, es3),
                Arguments.of(s4, es4)
        );
    }

    @ParameterizedTest
    @MethodSource("toSetData")
    public <E> void testToSet(ImmutableHashSet<E> set, Set<E> expected)
    {
        assertEquals(expected.size(), set.toSet().size(), "Set size off");
        assertEquals(expected, set.toSet(), "Set equality");
    }

    @Test
    public void testFromCollection()
    {
        var set = new HashSet<Integer>();
        var r = new Random("kaka".hashCode());

        for (var i = 0; i < 10000; i++)
            set.add(r.nextInt(10000));

        var imSet = ImmutableHashSet.fromCollection(set);
        assertEquals(set.size(), imSet.size(), "The sizes are supposed to match :(");

        for (var n : set)
        {
            assertTrue(imSet.contains(n), "ImmutableHashSet is supposed to have %d in it, but doesn't".formatted(n));
        }
    }

    @Test
    public void testStaticEmpty()
    {
        var empty = ImmutableHashSet.empty();
        assertNotNull(empty, "Empty should not be null.");
        assertEquals(0, empty.size(), "The empty set should have size 0.");
        assertTrue(empty.isEmpty(), "ImmutableHashSet#empty should return true for the empty set.");
    }

    @Test
    public void testStaticEmptyNotSame()
    {
        var empties = List.of(ImmutableHashSet.empty(), ImmutableHashSet.empty(), ImmutableHashSet.empty());

        for (var empty : empties)
        {
            assertNotNull(empty, "Empty should not be null.");
            assertEquals(0, empty.size(), "The empty set should have size 0.");
            assertTrue(empty.isEmpty(), "ImmutableHashSet#empty should return true for the empty set.");
        }

        for (var i = 0; i < empties.size(); i++)
        {
            for (var ii = i + 1; ii < empties.size(); ii++)
            {
                assertNotSame(empties.get(i), empties.get(ii), "They should never be the same instance.");
            }
        }
    }
}