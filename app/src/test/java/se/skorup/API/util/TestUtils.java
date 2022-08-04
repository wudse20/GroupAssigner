package se.skorup.API.util;


import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Utils methods.
 * */
public class TestUtils
{
    public static Stream<Arguments> getNameCaseData()
    {
        return Stream.of(
            Arguments.of("Anton", "Anton"),
            Arguments.of("Anton", "anton"),
            Arguments.of("Anton Skorup", "anton skorup"),
            Arguments.of("Anton Skorup", "AnToN sKoRuP"),
            Arguments.of("Anton Skorup", "Anton Skorup"),
            Arguments.of("Anton Karl Dan Skorup", "Anton Karl Dan Skorup"),
            Arguments.of("Anton Karl Dan Skorup", "anton KARL dan SKORUP")
        );
    }

    @ParameterizedTest
    @MethodSource("getNameCaseData")
    public void testToNameCase(String expected, String input)
    {
        assertEquals(expected, Utils.toNameCase(input), input);
    }

    public static Stream<Arguments> getDoubleData()
    {
        return Stream.of(
            Arguments.of("5", true),
            Arguments.of("555", true),
            Arguments.of("10.32", true),
            Arguments.of("kaka", false),
            Arguments.of(".5", true),
            Arguments.of("5.", true),
            Arguments.of("5..", false),
            Arguments.of("5..0", false),
            Arguments.of("123443d", true),
            Arguments.of("213123D", true),
            Arguments.of("123443f", true),
            Arguments.of("213123F", true)
        );
    }

    @ParameterizedTest
    @MethodSource("getDoubleData")
    public void testIsValidDouble(String input, boolean expected)
    {
        assertEquals(expected, Utils.isValidDouble(input), input);
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

    public static Stream<Arguments> getPowData()
    {
        return Stream.of(
            Arguments.of(5, 2, 5 * 5),
            Arguments.of(5, -2, 0),
            Arguments.of(2, 31, Integer.MIN_VALUE),
            Arguments.of(-5, 2, 25),
            Arguments.of(-5, 3, -125),
            Arguments.of(5, 5, (int) Math.pow(5, 5)),
            Arguments.of(10, 3, 1000),
            Arguments.of(10, 7, (int) Math.pow(10, 7)),
            Arguments.of(7, 4, (int) Math.pow(7, 4)),
            Arguments.of(1, (int) Math.pow(10, 100), 1),
            Arguments.of(1413, 1, 1413),
            Arguments.of(123, 0, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("getPowData")
    public void testPowers(int b, int e, int expected)
    {
        assertEquals(Utils.pow(b, e), expected);
    }

    public static Stream<Arguments> getDigitData()
    {
        return Stream.of(
            Arguments.of('0', true),
            Arguments.of('1', true),
            Arguments.of('2', true),
            Arguments.of('3', true),
            Arguments.of('4', true),
            Arguments.of('5', true),
            Arguments.of('6', true),
            Arguments.of('7', true),
            Arguments.of('8', true),
            Arguments.of('9', true),
            Arguments.of('a', false),
            Arguments.of('ö', false)
        );
    }

    @ParameterizedTest
    @MethodSource("getDigitData")
    public void testIsValidDigit(char c, boolean expected)
    {
        assertEquals(expected, Utils.isDigit(c));
    }

    public static Stream<Arguments> getIntegerData()
    {
        return Stream.of(
            Arguments.of("1", true),
            Arguments.of("2", true),
            Arguments.of("3", true),
            Arguments.of("4", true),
            Arguments.of("5", true),
            Arguments.of("6", true),
            Arguments.of("7", true),
            Arguments.of("8", true),
            Arguments.of("9", true),
            Arguments.of("123", true),
            Arguments.of("2123", true),
            Arguments.of("31435", true),
            Arguments.of("48796", true),
            Arguments.of("5351", true),
            Arguments.of("62314", true),
            Arguments.of("73452", true),
            Arguments.of("81243", true),
            Arguments.of("95345", true),
            Arguments.of("apa", false),
            Arguments.of("2asdf", false),
            Arguments.of("3231f", false),
            Arguments.of("231f34", false),
            Arguments.of("123f5", false),
            Arguments.of("12f36", false),
            Arguments.of("7f214", false),
            Arguments.of("8123asf12", false),
            Arguments.of("91ö23", false),
            Arguments.of("123443d", false),
            Arguments.of("213123D", false),
            Arguments.of("123f", false),
            Arguments.of("123F", false)
        );
    }

    @ParameterizedTest
    @MethodSource("getIntegerData")
    public void testIsValidInteger(String str, boolean expected)
    {
        assertEquals(expected, Utils.isValidInteger(str));
    }

    public static Stream<Arguments> getSizeData()
    {
        return Stream.of(
            Arguments.of("test", Optional.empty(), 10, "<html><p>test 1, 2, 5, 10</p></html>"),
            Arguments.of("test", Optional.empty(), 0, "<html><p>test 0</p></html>"),
            Arguments.of("test", Optional.empty(), 3, "<html><p>test 1, 3</p></html>"),
            Arguments.of("test", Optional.of("red"), 24, "<html><p color=\"red\">test 1, 2, 3, 4, 6, 8, 12, 24</p></html>"),
            Arguments.of("test", Optional.of("red"), 0, "<html><p color=\"red\">test 0</p></html>"),
            Arguments.of("test", Optional.of("red"), 3, "<html><p color=\"red\">test 1, 3</p></html>")
        );
    }

    @ParameterizedTest
    @MethodSource("getSizeData")
    public void testFormatSize(String header, Optional<String> color, int size, String expected)
    {
        assertEquals(expected, Utils.formatSizeString(header, color, size), "TEST WITH FORMAT SIZE");
    }

    public static Stream<Arguments> getURLData()
    {
        return Stream.of(
            Arguments.of("https://skorup.se/test/dont_exist", Optional.empty()),
            Arguments.of("https://skorup.se/test/test_file1", Optional.of("Connection!")),
            Arguments.of("https://skorup.se/test/empty", Optional.of(""))
        );
    }

    @ParameterizedTest
    @MethodSource("getURLData")
    public void testGetContentOfURL(String url, Optional<String> expected)
    {
        assertEquals(expected, Utils.getContentOfURL(url));
    }
}
