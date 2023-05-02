package se.skorup.util.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TestImmutableArray
{
    @Test
    public void testSize1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(4, myStringArray.size());
    }

    @Test
    public void testSize2()
    {
        var arr = new ImmutableArray<String>();
        assertEquals(0, arr.size());
    }

    @Test
    public void testGet1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals("Test1", myStringArray.get(0));
    }

    @Test
    public void testGet2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IndexOutOfBoundsException.class, () -> myStringArray.get(5));
    }

    @Test
    public void testGet3()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals("Test1", myStringArray.get("Test1"));
    }

    @Test
    public void testGet4()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertNull(myStringArray.get("not in list"));
    }

    @Test
    public void testForAll1()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        assertTrue(arr.forAll(x -> x > 0));
    }

    @Test
    public void testForAll2()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        assertFalse(arr.forAll(x -> x < 0));
    }

    @Test
    public void testForAll3()
    {
        var arr = new ImmutableArray<Integer>(null, null, null, null);
        assertFalse(arr.forAll(x -> x < 0));
    }

    @Test
    public void testForEach1()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var sum = 1 + 2 + 3 + 4 + 5 + 6;

        var sum2 = new AtomicInteger();
        arr.forEach(sum2::addAndGet);

        assertEquals(sum, sum2.get());
    }

    @Test
    public void testForEach2()
    {
        var arr = new ImmutableArray<String>(null, null, null, null);
        assertThrows(NullPointerException.class, () -> arr.forEach(String::toLowerCase));
    }

    @Test
    public void testIterator1()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var sum = 1 + 2 + 3 + 4 + 5 + 6;
        var sum2 = 0;

        for (var x : arr) {
            sum2 += x;
        }

        assertEquals(sum, sum2);
    }

    @Test
    public void testIterator4()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var i1 = arr.iterator();

        for (int i = 0; i < arr.size(); i++)
        {
            assertTrue(i1.hasNext());
            i1.next();
        }

        assertThrows(NoSuchElementException.class, i1::next);
    }

    @Test
    public void testMap1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>("test1", "test2", "test3", "test4");
        var newArr = myStringArray.map(String::toLowerCase);

        var controlIterator = controlArr.iterator();
        var newArrIterator = newArr.iterator();

        while (controlIterator.hasNext() && newArrIterator.hasNext())
        {
            assertEquals(controlIterator.next(), newArrIterator.next());
        }
    }

    @Test
    public void testMap2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>(1, 2, 3, 4);
        myStringArray = new ImmutableArray<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertArrayEquals(controlArr.toArray(), newArr.toArray());
    }

    @Test
    public void testEquals1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var a2 = new ImmutableArray<>(1, 2, 3, 4);
        assertNotEquals(a2, myStringArray);
    }

    @Test
    public void testEquals2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var a2 = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(a2, myStringArray);
        assertEquals(a2.hashCode(), myStringArray.hashCode());
    }

    @Test
    public void testEquals3()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>(1, 2, 3, 4);
        myStringArray = new ImmutableArray<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertEquals(controlArr, newArr);
        assertEquals(controlArr.hashCode(), newArr.hashCode());
    }

    @Test
    public void testEquals4()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>(1, 2, 3, 4, 5);
        myStringArray = new ImmutableArray<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertNotEquals(controlArr, newArr);
    }

    @Test
    public void testEquals5()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var controlArr = new ImmutableArray<String>(null, null, null, null);

        assertNotEquals(controlArr, myStringArray);
    }

    @Test
    public void testFromList1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = new ImmutableArray<>(Arrays.asList("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    @Test
    public void testFromList2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromList(Arrays.asList("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    @Test
    public void testFromListNull1()
    {
        List<Integer> list = null;
        assertEquals(0, new ImmutableArray<>(list).size());
    }

    @Test
    public void testFromListNull2()
    {
        assertEquals(0, ImmutableArray.fromList(null).size());
    }

    @Test
    public void testFromCollection()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromCollection(List.of("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    @Test
    public void testFromCollectionNull()
    {
        assertEquals(0, ImmutableArray.fromCollection(null).size());
    }

    @Test
    public void testFromArray()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromArray(new String[] {"Test1", "Test2", "Test3", "Test4"});
        assertEquals(myStringArray, arr);
    }

    @Test
    public void testFromArrayNull()
    {
        assertEquals(0, ImmutableArray.fromArray(null).size());
    }

    @Test
    public void testEmpty()
    {
        assertTrue(new ImmutableArray<String>().isEmpty());
        assertFalse(new ImmutableArray<>("hello").isEmpty());
    }

    @Test
    public void testMkString()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1, 2, 3, 4";
        assertEquals(ctr, arr.mkString(", "));
    }

    @Test
    public void testMkString2()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1:;:2:;:3:;:4";
        assertEquals(ctr, arr.mkString(":;:"));
    }

    @Test
    public void testMkString3()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1234";
        assertEquals(ctr, arr.mkString(""));
    }

    @Test
    public void testMkStringEmpty()
    {
        var arr = new ImmutableArray<>();
        var ctr = "";
        assertEquals(ctr, arr.mkString(", "));
    }

    @Test
    public void testDropNGreaterThanSize()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(myStringArray.size()));
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(myStringArray.size() + 1));
    }

    @Test
    public void testDropNLessThanOne()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(0));
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(-123));
    }

    @Test
    public void testDropNotThrows()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        myStringArray.drop(1);
        myStringArray.drop(myStringArray.size() - 1);
    }

    @Test
    public void testDrop()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(myStringArray.size() - 1, myStringArray.drop(1).size());

        var arr = new ImmutableArray<>(1, 2, 3, 4, 5).drop(2);
        var ctr = new ImmutableArray<>(3, 4, 5);

        assertEquals(ctr.size(), arr.size());
        assertEquals(ctr, arr);
    }

    @Test
    public void testSortedCorrect()
    {
        var arr = new ImmutableArray<>(1, 423, 521, 32, 53);
        var ctr = new ImmutableArray<>(1, 32, 53, 423, 521);

        assertNotNull(arr.sorted());
        assertEquals(ctr, arr.sorted());
        assertEquals(ctr.size(), arr.sorted().size());

        assertEquals(new ImmutableArray<String>(), new ImmutableArray<String>().sorted());
    }

    @Test
    public void testSortedWrongful()
    {
        assertNull(new ImmutableArray<>(
                        new SomeClassWithoutComparable(1),
                        new SomeClassWithoutComparable(231)
                ).sorted()
        );
    }

    @Test
    public void testSortBy()
    {
        var arr = new ImmutableArray<>(513, 123, 532, 123, 865);
        var ctr = new ImmutableArray<>(123, 123, 513, 532, 865);

        assertEquals(ctr, arr.sortBy(Integer::compare));
        assertEquals(ctr.size(), arr.size());
    }

    private record SomeClassWithoutComparable(int x) {}

    @Test
    public void testSortByEmpty()
    {
        var arr = new ImmutableArray<Integer>();

        assertTrue(arr.sortBy(Integer::compare).isEmpty());
        assertEquals(arr, arr.sortBy(Integer::compareTo));
    }

    @Test
    public void testFill()
    {
        var ctr = new ImmutableArray<>("hej", "hej", "hej", "hej", "hej");
        var arr = ImmutableArray.fill(5, () -> "hej");

        assertEquals(ctr.size(), arr.size());
        assertEquals(ctr, arr);
    }

    @Test
    public void testFill2()
    {
        var rand1 = new Random(123);
        var rand2 = new Random(123);

        var list = new Vector<Integer>();
        for (int i = 1; i <= 1000000; i++)
            list.add(rand1.nextInt());

        var arr = ImmutableArray.fill(1000000, rand2::nextInt);
        assertEquals(list.size(), arr.size());
        assertEquals(ImmutableArray.fromList(list), arr);
    }

    @Test
    public void testFillNullAndZero()
    {
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(100, null));
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(0, () -> 1));
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(-123, () -> 1));
    }

    @Test
    public void testDropMatching()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching("Test1");
        var ctr = new ImmutableArray<>("Test2", "Test3", "Test4");

        assertEquals(ctr.size(), res.size());
        assertEquals(ctr, res);
        assertNotEquals(myStringArray, res);
    }

    @Test
    public void testDropMatchingNonExistent()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching("Not in the list");
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    @Test
    public void testDropMatchingNull()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        String str = null;
        var res = myStringArray.dropMatching(str);
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    @Test
    public void testDropMatchingEmptyInput()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching();
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    @Test
    public void testDropWhile()
    {
        var i = new AtomicInteger(0);
        var arr = ImmutableArray.fill(10, () -> i.addAndGet(1));

        var arr2 = arr.dropWhile(x -> x <= 5);
        var ctr = new ImmutableArray<>(1, 2, 3, 4, 5);
        assertEquals(ctr.size(), arr2.size());
        assertEquals(ctr, arr2);
    }

    @Test
    public void testDropWhileNull()
    {
        var r = new Random(123);
        var arr = ImmutableArray.fill(100, r::nextInt);
        var arr2 = arr.dropWhile(null);
        assertEquals(arr, arr2);
        assertSame(arr, arr2); // To check the instance.
    }

    @Test
    public void testReplace()
    {
        var arr = ImmutableArray.fill(100, new Random()::nextInt);
        var arr2 = arr.replace(0, 1);

        for (var i = 0; i < arr.size(); i++)
        {
            if (i == 0)
                assertEquals(1, (int) arr2.get(0)); // Needs to cast from some reason...
            else
                assertEquals(arr.get(i), arr2.get(i));
        }
    }

    @Test
    public void testReplaceThrows()
    {
        var arr = new ImmutableArray<>("a", "b", "c");
        assertThrows(IndexOutOfBoundsException.class, () -> arr.replace(-1, "a"));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.replace(3, "a"));
    }

    @Test
    public void testReplaceNull()
    {
        var arr = new ImmutableArray<>("a", "b", "c");
        var arr2 = arr.replace(0, null);

        for (var i = 0; i < arr.size(); i++)
        {
            if (i == 0)
                assertNull(arr2.get(0));
            else
                assertEquals(arr.get(i), arr2.get(i));
        }
    }

    @Test
    public void testFindFirst()
    {
        var arr = new ImmutableArray<>("Anton", "Kalle", "Linnea", "Alexandra", "Sebastian");
        assertNotNull(arr.getFirstMatch(x -> x.length() != 0));
        assertNull(arr.getFirstMatch(x -> x.length() == 0));
        assertNull(new ImmutableArray<Integer>().getFirstMatch(x -> x == 123));

        assertEquals("Alexandra", arr.getFirstMatch(x -> x.length() == 9));
    }

    @Test
    public void testIndexOf()
    {
        var arr = new ImmutableArray<>("Anton", "Kalle", "Linnea", "Alexandra", "Sebastian");
        assertEquals(-1, arr.indexOf(null));
        assertEquals(-1, arr.indexOf("Jim"));
        assertEquals(3, arr.indexOf("Alexandra"));
    }
}