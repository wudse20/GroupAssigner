package se.skorup.API.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
