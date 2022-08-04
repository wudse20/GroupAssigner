package se.skorup.main.gui.components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVLabel
{
    public static Stream<Arguments> getEqualsData()
    {
        var csv1 = new CSVLabel("Hejsan", 1, 1, null, null);
        var csv2 = new CSVLabel("Hejsan", 1, 1, null, null);
        var csv3 = new CSVLabel("Hejsan", 0, 0, null, null);

        return Stream.of(
            Arguments.of(csv1, csv1, true),
            Arguments.of(csv2, csv2, true),
            Arguments.of(csv3, csv3, true),
            Arguments.of(csv1, csv2, true),
            Arguments.of(csv1, csv3, false),
            Arguments.of(csv2, csv3, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getEqualsData")
    public void testEquals(CSVLabel a, CSVLabel b, boolean expected)
    {
        assertEquals(expected, a.equals(b), "a.equals(b) = %b, expected: %b".formatted(a.equals(b), expected));
        assertEquals(expected, b.equals(a), "b.equals(a) = %b, expected: %b".formatted(b.equals(a), expected));
        assertEquals(
            expected, a.hashCode() == b.hashCode(),
            "a.hashCode() == b.hashCode() = %b, expected: %b".formatted(
                a.hashCode() == b.hashCode(), expected
            )
        );
    }
}
