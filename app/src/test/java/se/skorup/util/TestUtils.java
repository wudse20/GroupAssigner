package se.skorup.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            Arguments.of("Anton Karl Dan Skorup", "anton KARL dan SKORUP"),
            Arguments.of("Anton Skorup", "anton\nskorup"),
            Arguments.of("Anton Skorup", "AnToN\nsKoRuP"),
            Arguments.of("Anton Skorup", "Anton\nSkorup"),
            Arguments.of("Anton Karl Dan Skorup", "Anton\nKarl\nDan\nSkorup"),
            Arguments.of("Anton Karl Dan Skorup", "anton\nKARL\ndan\nSKORUP"),
            Arguments.of("Anton Skorup", "anton\tskorup"),
            Arguments.of("Anton Skorup", "AnToN\tsKoRuP"),
            Arguments.of("Anton Skorup", "Anton\tSkorup"),
            Arguments.of("Anton Karl Dan Skorup", "Anton\tKarl\tDan\tSkorup"),
            Arguments.of("Anton Karl Dan Skorup", "anton\tKARL\tdan\tSKORUP"),
            Arguments.of("Anton Karl Dan Skorup", "Anton\tKarl Dan\nSkorup"),
            Arguments.of("Anton Karl Dan Skorup", "anton\tKARL dan\nSKORUP"),
            Arguments.of("", ""),
            Arguments.of("", "   "),
            Arguments.of("", "\t"),
            Arguments.of("", "\n")
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
}
