package se.skorup.API.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the Utils methods.
 * */
public class TestUtils
{
    @Test
    public void testToNameCase()
    {
        assertEquals("Anton", Utils.toNameCase("Anton"));
        assertEquals("Anton", Utils.toNameCase("anton"));
        assertEquals("Anton Skorup", Utils.toNameCase("anton skorup"));
        assertEquals("Anton Skorup", Utils.toNameCase("AnToN sKoRuP"));
    }

    @Test
    public void testIsValidDouble()
    {
        assertTrue(Utils.isValidDouble("00.00"));
        assertTrue(Utils.isValidDouble("5"));
        assertTrue(Utils.isValidDouble("555"));
        assertTrue(Utils.isValidDouble("10.32"));
        assertFalse(Utils.isValidDouble("kaka"));
        assertFalse(Utils.isValidDouble(".5"));
    }
}
