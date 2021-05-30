package se.skorup.main.objects;

import org.testng.annotations.Test;

import java.util.HashSet;

import static org.testng.Assert.assertEquals;

/**
 * Test the tuple class.
 * */
public class TupleTester
{
    /**
     * Tests the invert method.
     * */
    @Test
    public void testInvert()
    {
        var t = new Tuple(1, 2);
        var ctr = new Tuple(2, 1);
        assertEquals(ctr, t.invert());
    }

    /**
     * Test the image of method.
     * */
    @Test
    public void testImageOf()
    {
        var set = new HashSet<Tuple>();
        set.add(new Tuple(1, 10));
        set.add(new Tuple(2, 11));
        set.add(new Tuple(1, 12));
        set.add(new Tuple(3, 10));

        var ctr = new HashSet<Integer>();
        ctr.add(10);
        ctr.add(12);

        assertEquals(ctr, Tuple.imageOf(set, 1));
    }

    @Test
    public void testImageOfSet()
    {
        var set = new HashSet<Tuple>();
        set.add(new Tuple(1, 10));
        set.add(new Tuple(2, 11));
        set.add(new Tuple(1, 12));
        set.add(new Tuple(3, 10));

        var ctr = new HashSet<Integer>();
        ctr.add(10);
        ctr.add(12);
        ctr.add(11);

        var iSet = new HashSet<Integer>();
        iSet.add(1);
        iSet.add(2);

        assertEquals(ctr, Tuple.imageOfSet(set, iSet));
    }
}
