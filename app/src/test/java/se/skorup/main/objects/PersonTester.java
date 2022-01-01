package se.skorup.main.objects;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * The class that tests the abstract class person
 * and its subtypes.
 * */
public class PersonTester
{
    /**
     * Tests equals function on the same
     * subclass.
     * */
    @Test
    public void testEquals()
    {
        var l = new Leader("Anton", 1);
        var l2 = new Leader("Sebbe", 2);
        var l3 = new Leader("Sebbe", 1);

        assertNotEquals(l, l2);
        assertEquals(l, l3);
    }

    /**
     * Tests equals function on the different
     * subclass.
     * */
    @Test
    public void testEqualsNotSameSubClass()
    {
        // Explicit type declaration to prevent different types
        // via type derivation.
        Person l = new Leader("Anton", 1);
        Person c = new Candidate("Sebbe", 1);
        assertNotEquals(l, c);
    }

    /**
     * Tests the wishlist.
     * */
    @Test
    public void testWishlist()
    {
        var p = new Leader("Anton", 0);
        var items = new int[] {1, 2, 3, 4, 5};

        for (var i : items)
            assertTrue(p.addWishlistId(i));

        for (var i : items)
            assertFalse(p.addWishlistId(i));

        assertArrayEquals(items, p.getWishlist());
    }

    /**
     * Tests the denylist.
     * */
    @Test
    public void testDenylist()
    {
        var p = new Leader("Anton", 0);
        var items = new int[] {1, 2, 3, 4, 5};

        for (var i : items)
            assertTrue(p.addDenylistId(i));

        for (var i : items)
            assertFalse(p.addDenylistId(i));

        assertArrayEquals(items, p.getDenylist());
    }

    /**
     * Tests the clone method.
     * */
    @Test
    public void testClone()
    {
        var p = new Leader("Anton", 0);
        var p2 = p.clone();
        assertEquals(p, p2);
        assertNotSame(p, p2);
    }

    /**
     * Tests the clone method, with map on list.
     * */
    @Test
    public void testCloneList()
    {
        var persons = new ArrayList<Person>();
        persons.add(new Leader("Anton", 0));
        persons.add(new Candidate("Sebbe", 0));

        assertEquals(persons, persons.stream().map(Person::clone).collect(Collectors.toCollection(ArrayList::new)));
    }
}
