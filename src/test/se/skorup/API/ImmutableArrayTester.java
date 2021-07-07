package se.skorup.API;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

/**
 * The class that test the {@link ImmutableArray}
 * */
public class ImmutableArrayTester
{
    /**
     * This method test: <br>
     *     - {@link ImmutableArray#size()}
     * */
    @Test
    public void testSize1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(4, myStringArray.size());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#size()}
     * */
    @Test
    public void testSize2()
    {
        var arr = new ImmutableArray<String>();
        assertEquals(0, arr.size());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#get(int)}
     * */
    @Test
    public void testGet1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals("Test1", myStringArray.get(0));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#get(int)}
     * */
    @Test
    public void testGet2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IndexOutOfBoundsException.class, () -> myStringArray.get(5));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#get(Object)}
     * */
    @Test
    public void testGet3()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals("Test1", myStringArray.get("Test1"));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#get(Object)}
     * */
    @Test
    public void testGet4()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertNull(myStringArray.get("not in list"));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#forAll}
     * */
    @Test
    public void testForAll1()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        assertTrue(arr.forAll(x -> x > 0));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#forAll}
     * */
    @Test
    public void testForAll2()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        assertFalse(arr.forAll(x -> x < 0));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#forAll}
     * */
    @Test
    public void testForAll3()
    {
        var arr = new ImmutableArray<Integer>(null, null, null, null);
        assertFalse(arr.forAll(x -> x < 0));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#forEach}
     * */
    @Test
    public void testForEach1()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var sum = 1 + 2 + 3 + 4 + 5 + 6;

        AtomicInteger sum2 = new AtomicInteger();
        arr.forEach(sum2::addAndGet);

        assertEquals(sum, sum2.get());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#forEach(Consumer)} )}
     * */
    @Test
    public void testForEach2()
    {
        var arr = new ImmutableArray<String>(null, null, null, null);
        assertThrows(NullPointerException.class, () -> arr.forEach(String::toLowerCase));
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#iterator()}
     * */
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

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#iterator()}
     * */
    @Test
    public void testIterator2()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var sum = 1 + 2 + 3 + 4 + 5 + 6;
        sum *= 2;

        var sum2 = 0;
        var i1 = arr.iterator();
        var i2 = arr.iterator();

        while (i1.hasNext())
            sum2 += i1.next();

        while (i2.hasNext())
            sum2 += i2.next();

        assertEquals(sum, sum2);
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#iterator()}
     * */
    @Test
    public void testIterator3()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4, 5, 6);
        var i1 = arr.iterator();

        for (int i = 0; i < arr.size(); i++)
        {
            assertTrue(i1.hasNext());
            i1.next();
        }

        assertFalse(i1.hasNext());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#iterator()}
     * */
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

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#map}
     * */
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

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#map}
     * */
    @Test
    public void testMap2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>(1, 2, 3, 4);
        myStringArray = new ImmutableArray<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertArrayEquals(controlArr.toArray(), newArr.toArray());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#equals(Object)}
     * */
    @Test
    public void testEquals1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var a2 = new ImmutableArray<>(1, 2, 3, 4);
        assertNotEquals(a2, myStringArray);
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#equals(Object)}
     * */
    @Test
    public void testEquals2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var a2 = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertEquals(a2, myStringArray);
        assertEquals(a2.hashCode(), myStringArray.hashCode());
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#equals(Object)}
     * */
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

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#equals(Object)}
     * */
    @Test
    public void testEquals4()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");

        var controlArr = new ImmutableArray<>(1, 2, 3, 4, 5);
        myStringArray = new ImmutableArray<>("1", "2", "3", "4");
        var newArr = myStringArray.map(Integer::parseInt);

        assertNotEquals(controlArr, newArr);
    }

    /**
     * This method test: <br>
     *     - {@link ImmutableArray#equals(Object)}
     * */
    @Test
    public void testEquals5()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var controlArr = new ImmutableArray<String>(null, null, null, null);

        assertNotEquals(controlArr, myStringArray);
    }

    /**
     * This method test: <br>
     *  - {@link ImmutableArray#ImmutableArray(List)}
     * */
    @Test
    public void testFromList1()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = new ImmutableArray<>(Arrays.asList("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    /**
     * Test the static fromList method.
     * */
    @Test
    public void testFromList2()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromList(Arrays.asList("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    /**
     * Tests the list constructor with list = null.
     * */
    @Test
    public void testFromListNull1()
    {
        List<Integer> list = null;
        assertEquals(0, new ImmutableArray<>(list).size());
    }

    /**
     * Tests the fromList method with list = null.
     * */
    @Test
    public void testFromListNull2()
    {
        assertEquals(0, ImmutableArray.fromList(null).size());
    }

    /**
     * Test the static fromArray method.
     * */
    @Test
    public void testFromCollection()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromCollection(List.of("Test1", "Test2", "Test3", "Test4"));
        assertEquals(myStringArray, arr);
    }

    /**
     * Tests the from array method with arr = null.
     * */
    @Test
    public void testFromCollectionNull()
    {
        assertEquals(0, ImmutableArray.fromCollection(null).size());
    }

    /**
     * Test the static fromArray method.
     * */
    @Test
    public void testFromArray()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var arr = ImmutableArray.fromArray(new String[] {"Test1", "Test2", "Test3", "Test4"});
        assertEquals(myStringArray, arr);
    }

    /**
     * Tests the from array method with arr = null.
     * */
    @Test
    public void testFromArrayNull()
    {
        assertEquals(0, ImmutableArray.fromArray(null).size());
    }

    /**
     * Tests the isEmpty method.
     * */
    @Test
    public void testEmpty()
    {
        assertTrue(new ImmutableArray<String>().isEmpty());
        assertFalse(new ImmutableArray<>("hello").isEmpty());
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1, 2, 3, 4";
        assertEquals(ctr, arr.mkString(", "));
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString2()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1:;:2:;:3:;:4";
        assertEquals(ctr, arr.mkString(":;:"));
    }

    /**
     * Tests the mkString method.
     * */
    @Test
    public void testMkString3()
    {
        var arr = new ImmutableArray<>(1, 2, 3, 4);
        var ctr = "1234";
        assertEquals(ctr, arr.mkString(""));
    }

    /**
     * Tests the mkString method, with an
     * empty Array.
     * */
    @Test
    public void testMkStringEmpty()
    {
        var arr = new ImmutableArray<>();
        var ctr = "";
        assertEquals(ctr, arr.mkString(", "));
    }

    /**
     * Tests the drop method, with an n >= size.
     * */
    @Test
    public void testDropNGreaterThanSize()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(myStringArray.size()));
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(myStringArray.size() + 1));
    }

    /**
     * Tests the drop method, with an n >= size.
     * */
    @Test
    public void testDropNLessThanOne()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(0));
        assertThrows(IllegalArgumentException.class, () -> myStringArray.drop(-123));
    }

    /**
     * Tests the cases where drop isn't supposed to
     * throw an exception.
     * */
    @Test
    public void testDropNotThrows()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        myStringArray.drop(1);
        myStringArray.drop(myStringArray.size() - 1);
    }

    /**
     * Test drop method.
     * */
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

    /**
     * Tests the sorted method, with
     * correct method calls.
     * */
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

    /**
     * Test the sorted method, with wrongful
     * input.
     * */
    @Test
    public void testSortedWrongful()
    {
        assertNull(new ImmutableArray<>(
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
        var arr = new ImmutableArray<>(513, 123, 532, 123, 865);
        var ctr = new ImmutableArray<>(123, 123, 513, 532, 865);

        assertEquals(ctr, arr.sortBy(Integer::compare));
        assertEquals(ctr.size(), arr.size());
    }

    /**
     * A class used in the tests.
     * This is just a dummy class.
     *
     * @param x the dummy class.
     */
    private record SomeClassWithoutComparable(int x) {}

    /**
     * Tests sort by on an empty array.
     * */
    @Test
    public void testSortByEmpty()
    {
        var arr = new ImmutableArray<Integer>();

        assertTrue(arr.sortBy(Integer::compare).isEmpty());
        assertEquals(arr, arr.sortBy(Integer::compareTo));
    }

    /**
     * Tests the fill method.
     * */
    @Test
    public void testFill()
    {
        var ctr = new ImmutableArray<>("hej", "hej", "hej", "hej", "hej");
        var arr = ImmutableArray.fill(5, () -> "hej");

        assertEquals(ctr.size(), arr.size());
        assertEquals(ctr, arr);
    }

    /**
     * Tests the fill method.
     * */
    @Test
    public void testFill2()
    {
        var rand1 = new Random(123);
        var rand2 = new Random(123);

        var list = new Vector<Integer>();
        for (int i = 1; i <= 10000000; i++)
            list.add(rand1.nextInt());

        var arr = ImmutableArray.fill(10000000, rand2::nextInt);
        assertEquals(list.size(), arr.size());
        assertEquals(ImmutableArray.fromList(list), arr);
    }

    /**
     * Tests the fill method with f = null and n = 0 and n &lt; 0.
     * */
    @Test
    public void testFillNullAndZero()
    {
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(100, null));
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(0, () -> 1));
        assertThrows(IllegalArgumentException.class, () -> ImmutableArray.fill(-123, () -> 1));
    }

    /**
     * Tests the drop matching method were everything is by the book.
     * */
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

    /**
     * Tests the drop matching when the element is not in list.
     * */
    @Test
    public void testDropMatchingNonExistent()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching("Not in the list");
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    /**
     * Tests the drop matching when the passed argument is null.
     * */
    @Test
    public void testDropMatchingNull()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching(null);
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    /**
     * Tests drop matching with an empty input.
     * */
    @Test
    public void testDropMatchingEmptyInput()
    {
        var myStringArray = new ImmutableArray<>("Test1", "Test2", "Test3", "Test4");
        var res = myStringArray.dropMatching();
        assertEquals(myStringArray, res);
        assertEquals(myStringArray.size(), res.size());
    }

    /**
     * Tests the drop while function when everything is fine.
     * */
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

    /**
     * Tests drop while, when the passed arg is null.
     * */
    @Test
    public void testDropWhileNull()
    {
        var r = new Random(123);
        var arr = ImmutableArray.fill(100, r::nextInt);
        var arr2 = arr.dropWhile(null);
        assertEquals(arr, arr2);
        assertSame(arr, arr2); // To check the instance.
    }

    /**
     * Tests the replace method.
     * */
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

    /**
     * Tests that the replace method throws IndexOutOfBoundsException.
     * */
    @Test
    public void testReplaceThrows()
    {
        var arr = new ImmutableArray<>("a", "b", "c");
        assertThrows(IndexOutOfBoundsException.class, () -> arr.replace(-1, "a"));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.replace(3, "a"));
    }

    /**
     * Tests the replace method with null.
     * */
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

    /**
     * Tests the find first method.
     * */
    @Test
    public void testFindFirst()
    {
        var arr = new ImmutableArray<>("Anton", "Kalle", "Linnea", "Alexandra", "Sebastian");
        assertNotNull(arr.getFirstMatch(x -> x.length() != 0));
        assertNull(arr.getFirstMatch(x -> x.length() == 0));
        assertNull(new ImmutableArray<Integer>().getFirstMatch(x -> x == 123));

        assertEquals("Alexandra", arr.getFirstMatch(x -> x.length() == 9));
    }

    /**
     * Tests the indexOf method.
     * */
    @Test
    public void testIndexOf()
    {
        var arr = new ImmutableArray<>("Anton", "Kalle", "Linnea", "Alexandra", "Sebastian");
        assertEquals(-1, arr.indexOf(null));
        assertEquals(-1, arr.indexOf("Jim"));
        assertEquals(3, arr.indexOf("Alexandra"));
    }
}
