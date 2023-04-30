package se.skorup.game.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.games.base.Score;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestScore
{
    public static Stream<Arguments> getIsScoreBeatenScoreScore()
    {
        return Stream.of(
            Arguments.of(new Score(10), new Score(11), true),
            Arguments.of(new Score(10), new Score(9), false),
            Arguments.of(new Score(10), new Score(10), false),
            Arguments.of(new Score(-10), new Score(-11), false),
            Arguments.of(new Score(-10), new Score(-9), true),
            Arguments.of(new Score(-10), new Score(-10), false)
        );
    }

    public static Stream<Arguments> getIsScoreBeatenScoreInt()
    {
        return Stream.of(
            Arguments.of(new Score(10), 11, true),
            Arguments.of(new Score(10), 9, false),
            Arguments.of(new Score(10), 10, false),
            Arguments.of(new Score(-10), -11, false),
            Arguments.of(new Score(-10), -9, true),
            Arguments.of(new Score(-10), -10, false)
        );
    }

    @ParameterizedTest
    @MethodSource({"getIsScoreBeatenScoreScore", "getIsScoreBeatenScoreInt"})
    public void testIsScoreBeaten(Score s1, Object s2, boolean expected)
    {
        if (s2 instanceof Integer i)
            assertEquals(expected, s1.isScoreBeaten(i));
        else if (s2 instanceof Score s)
            assertEquals(expected, s1.isScoreBeaten(s));
        else
            fail("Illegal argument type!");
    }
}
