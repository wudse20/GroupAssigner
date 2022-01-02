package se.skorup.API.immutable_collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests the immutable hash set.
 * */
public class ImmutableHashSetTester
{
    /**
     * Tests the equals method and hashCode method.
     * */
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

    /**
     * Tests the intersection method.
     * */
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

    /**
     * Test the union method.
     * */
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

    /**
     * This method test the size method.
     * */
    @Test
    public void testSize1()
    {
        var myStringArray = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(4, myStringArray.size());
    }

    /**
     * This method test the size method.
     * */
    @Test
    public void testSize2()
    {
        var arr = new ImmutableHashSet<String>();
        assertEquals(0, arr.size());
    }

    /**
     * Tests the isEmpty method.
     * */
    @Test
    public void testEmpty()
    {
        assertTrue(new ImmutableHashSet<String>().isEmpty());
        assertFalse(new ImmutableHashSet<>("hello").isEmpty());
    }

    /**
     * Tests the to array method.
     * */
    @Test
    public void testToArray()
    {
        var set1 = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6, 7);
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5, 6, 7}, set1.toArray());
    }

    /**
     * Tests the toList method.
     * */
    @Test
    public void testToList()
    {
        var set1 = new ImmutableHashSet<>("a", "b", "c");
        assertEquals(Arrays.asList("a", "b", "c"), set1.toList());
    }

    /**
     * Tests the get from index method.
     * */
    @Test
    public void testGetIndex()
    {
        var set = new ImmutableHashSet<>(1, 2, 3);
        assertThrows(UnsupportedOperationException.class, () -> set.get(0));
    }

    /**
     * Tests the get item method.
     * */
    @Test
    public void testGetItem()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5);
        assertEquals(Integer.valueOf(2), set.get(Integer.valueOf(2))); // Manual boxing to not confuse compiler.
        assertNull(set.get(Integer.valueOf(-123)));
    }

    /**
     * This method tests the forAll method.
     * */
    @Test
    public void testForAll1()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertTrue(set.forAll(x -> x > 0));
    }

    /**
     * This method tests the forAll method.
     * */
    @Test
    public void testForAll2()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertFalse(set.forAll(x -> x < 0));
    }

    /**
     * This method tests the forAll method.
     * */
    @Test
    public void testForAll3()
    {
        var set = new ImmutableHashSet<Integer>(null, null, null, null);
        assertFalse(set.forAll(x -> x < 0));
    }

    /**
     * This method tests the map method.
     * */
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

    /**
     * This method tests the map method.
     * */
    @Test
    public void testMap2()
    {
        var myStringArray = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");

        var ctrSet = new ImmutableHashSet<>(1, 2, 3, 4);
        myStringArray = new ImmutableHashSet<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertArrayEquals(ctrSet.toArray(), newArr.toArray());
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "1, 2, 3, 4";
        assertEquals(ctr, set.mkString(", "));
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString2()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "1:;:2:;:3:;:4";
        assertEquals(ctr, set.mkString(":;:"));
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString3()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4);
        var ctr = "1234";
        assertEquals(ctr, set.mkString(""));
    }

    /**
     * Tests the mkString method, on the
     * empty set.
     * */
    @Test
    public void testMkStringEmpty()
    {
        var set = new ImmutableHashSet<>();
        var ctr = "";
        assertEquals(ctr, set.mkString(", "));
    }

    /**
     * Tests the drop method.
     * */
    @Test
    public void testDrop()
    {
        var set = new ImmutableHashSet<>('a', 'b', 'c');
        assertThrows(UnsupportedOperationException.class, () -> set.drop(1));
    }

    /**
     * Tests the sorted method, with
     * correct method calls.
     * */
    @Test
    public void testSortedCorrect()
    {
        var set = new ImmutableHashSet<>(1, 423, 521, 32, 53);
        var ctr = new ImmutableArray<>(1, 32, 53, 423, 521);

        assertNotNull(set.sorted());
        assertEquals(ctr, set.sorted());
        assertEquals(ctr.size(), set.sorted().size());
    }

    /**
     * Test the sorted method, with wrongful
     * input.
     * */
    @Test
    public void testSortedWrongful()
    {
        assertNull(new ImmutableHashSet<>(
                        new SomeClassWithoutComparable(1),
                        new SomeClassWithoutComparable(231)
                ).sorted()
        );
    }

    /**
     * This method tests the sortBy method.
     * */
    @Test
    public void testSortBy()
    {
        var set = new ImmutableHashSet<>(513, 123, 532, 123, 865);
        var ctr = new ImmutableArray<>(123, 513, 532, 865);

        assertEquals(ctr, set.sortBy(Integer::compare));
        assertEquals(ctr.size(), set.size());
    }

    /**
     * A class used in the tests.
     * This is just a dummy class.
     *
     * @param x the dummy class.
     */
    private record SomeClassWithoutComparable(int x) {}

    /**
     * Tests the drop matching method were everything is by the book.
     * */
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

    /**
     * Tests the drop matching when the element is not in list.
     * */
    @Test
    public void testDropMatchingNonExistent()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching("Not in the list");
        assertEquals(myStringSet, res);
        assertEquals(myStringSet.size(), res.size());
    }

    /**
     * Tests the drop matching when the passed argument is null.
     * */
    @Test
    public void testDropMatchingNull()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching(null);
        assertEquals(myStringSet, res);
        assertEquals(myStringSet.size(), res.size());
    }

    /**
     * Tests drop matching with an empty input.
     * */
    @Test
    public void testDropMatchingEmptyInput()
    {
        var myStringSet = new ImmutableHashSet<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringSet.dropMatching();
        assertEquals(myStringSet, res);
        assertEquals(myStringSet.size(), res.size());
    }

    /**
     * Tests the drop while method.
     * */
    @Test
    public void testDropWhile()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5);
        assertThrows(UnsupportedOperationException.class, () -> set.dropWhile(x -> x < 2));
    }

    /**
     * Tests the replace method.
     * */
    @Test
    public void testReplace()
    {
        var set = new ImmutableHashSet<>(1, 2, 3, 4, 5);
        assertThrows(UnsupportedOperationException.class, () -> set.replace(1, 2132));
    }

    /**
     * Tests the contains method.
     * */
    @Test
    public void testContains()
    {
        var ctr = new ImmutableHashSet<>(1, 2, 3, 4, 5, 6);
        assertFalse(ctr.contains(123));
        assertTrue(ctr.contains(2));
    }

    /**
     * Tests the find first method.
     * */
    @Test
    public void testFindFirst()
    {
        var set = new ImmutableHashSet<>("Anton", "Kalle", "Linnea", "Alexandra");
        assertNotNull(set.getFirstMatch(x -> x.length() != 0));
        assertNull(set.getFirstMatch(x -> x.length() == 0));
        assertNull(new ImmutableHashSet<Integer>().getFirstMatch(x -> x == 123));

        assertEquals("Alexandra", set.getFirstMatch(x -> x.length() == 9));
    }

    /**
     * Tests the indexOf method.
     * */
    @Test
    public void testIndexOf()
    {
        var set = new ImmutableHashSet<>("APA", "KAKA", "LATTE");
        assertEquals(-1, set.indexOf("APA"));
        assertEquals(-1, set.indexOf("KROKODIL"));
    }

    /**
     * Tests the diff method.
     * */
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
}
