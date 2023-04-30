package se.skorup.main.gui.components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
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

    public static Stream<Arguments> getFlashingData()
    {
        var l1 = new CSVLabel("", 0, 0, null, null);
        l1.startFlashing(100, Color.WHITE, Color.BLUE);

        var l2 = new CSVLabel("", 0, 0, null, null);
        l2.startFlashing(100, Color.WHITE, Color.BLUE);
        l2.stopFlashing();

        var l3 = new CSVLabel("", 0, 0, null, null);
        l3.startFlashing(100, Color.WHITE, Color.BLUE);
        l3.stopFlashing();
        l3.startFlashing(100, Color.WHITE, Color.BLUE);

        var l4 = new CSVLabel(",", 0, 0, null, null);
        l4.stopFlashing();
        l4.stopFlashing();

        return Stream.of(
            Arguments.of(new CSVLabel("", 0, 0, null, null), false),
            Arguments.of(l1, true),
            Arguments.of(l2, false),
            Arguments.of(l3, true),
            Arguments.of(l4, false)
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

    @ParameterizedTest
    @MethodSource("getFlashingData")
    public void testIsFlashing(CSVLabel label, boolean excepted)
    {
        assertEquals(excepted, label.isFlashing());
    }
}
