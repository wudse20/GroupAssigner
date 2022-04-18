package se.skorup.API.util;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    public static Stream<Arguments> getPadData()
    {
        return Stream.of(
            Arguments.of("", 'a', 5, "aaaaa"),
            Arguments.of("hej", 'a', 3, "hej"),
            Arguments.of("kaka", '*', 5, "kaka*"),
            Arguments.of("krokodil", ' ', "krokodil".length(), "krokodil"),
            Arguments.of("krokodil", ' ', "krokodil".length() - 1, "krokodil")
        );
    }

    @ParameterizedTest
    @MethodSource("getPadData")
    public void testPadString(String org, char pad, int length, String expected)
    {
        var str = Utils.padString(org, pad, length);
        assertEquals(expected, str);

        if (org.length() < length)
        {
            assertEquals(length, str.length());
            assertEquals(pad, str.charAt(org.length()));
        }
        else
        {
            assertEquals(org, str);
        }
    }
}
