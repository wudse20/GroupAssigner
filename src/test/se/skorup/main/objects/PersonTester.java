package se.skorup.main.objects;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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
}
