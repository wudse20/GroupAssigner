package se.skorup.game.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.games.base.Pos;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPos
{
    public static Stream<Arguments> getTestData()
    {
        return Stream.of(
            Arguments.of(new Pos(1, 1), 1, new Pos(1, 1)),
            Arguments.of(new Pos(1, 1), -1, new Pos(-1, -1)),
            Arguments.of(new Pos(1, 1), 0, new Pos(0, 0)),
            Arguments.of(new Pos(213, 123), 5, new Pos(213 * 5, 123 * 5)),
            Arguments.of(new Pos(123, 4132), -5, new Pos(123 * -5, 4132 * -5))
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testMultiplyInteger(Pos start, int i, Pos expected)
    {
        assertEquals(
            expected, start.multiply(i),
            "%d * %s = %s, expected: %s%n".formatted(
                i, start, start.multiply(i), expected
        ));
    }

    public static Stream<Arguments> getTestData2()
    {
        return Stream.of(
            Arguments.of(new Pos(1, 1), new Pos(1, 1), new Pos(1, 1)),
            Arguments.of(new Pos(1, 1), new Pos(-1, -1), new Pos(-1, -1)),
            Arguments.of(new Pos(1, 1), new Pos(0, 0), new Pos(0, 0)),
            Arguments.of(new Pos(213, 123), new Pos(5, 5), new Pos(213 * 5, 123 * 5)),
            Arguments.of(new Pos(123, 4132), new Pos(-5, -5), new Pos(123 * -5, 4132 * -5)),
            Arguments.of(new Pos(1, 1), new Pos(-1, 1), new Pos(-1, 1)),
            Arguments.of(new Pos(1, 1), new Pos(1, -1), new Pos(1, -1)),
            Arguments.of(new Pos(-1, -1), new Pos(-1, 1), new Pos(1, -1)),
            Arguments.of(new Pos(-1, -1), new Pos(1, -1), new Pos(-1, 1)),
            Arguments.of(new Pos(1, 1), new Pos(0, 1), new Pos(0, 1)),
            Arguments.of(new Pos(1, 1), new Pos(1, 0), new Pos(1, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData2")
    public void testMultiplyPos(Pos start, Pos multi, Pos expected)
    {
        assertEquals(
            expected, start.multiply(multi),
            "%s * %s = %s, expected: %s%n".formatted(
                start, multi, start.multiply(multi), expected
        ));

        assertEquals(
            expected, multi.multiply(start),
            "%s * %s = %s, expected: %s%n".formatted(
                multi, start, multi.multiply(start), expected
        ));
    }
}
