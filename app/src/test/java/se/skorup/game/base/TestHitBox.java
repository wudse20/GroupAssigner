package se.skorup.game.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.games.base.HitBox;
import se.skorup.games.base.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHitBox
{

    public static Stream<Arguments> getTestData()
    {
        var hb1 = new HitBox(new Pos(5, 5), 10, 10);
        var list1 = new ArrayList<Test>();

        for (int i = 0; i < 10; i++)
        {
            for (int ii = 0; ii < 10; ii++)
            {
                list1.add(new Test(new Pos(i, ii), i >= 5 && i <= 15 && ii >= 5 && ii <= 15));
            }
        }

        var hb2 = new HitBox(new Pos(0, 0), 0, 0);
        var list2 = new ArrayList<Test>();
        for (int i = 0; i < 20; i++)
        {
            for (int ii = 0; ii < 20; ii++)
            {
                list2.add(new Test(new Pos(i, ii), i == 0 && ii == 0));
            }
        }

        return Stream.of(
            Arguments.of(hb1, list1),
            Arguments.of(hb2, list2)
        );
    }


    @ParameterizedTest
    @MethodSource("getTestData")
    public void testInBounds(HitBox h, List<Test> tests)
    {
        for (var t : tests)
        {
            assertEquals(
                t.res, h.isPosInBound(t.p),
                "Pos: inbounds: %b, expected: %b, (x, y) = (%d, %d)"
                    .formatted(h.isPosInBound(t.p), t.res, t.p.x(), t.p.y())
            );
        }
    }

    record Test(Pos p, boolean res) {}

    public static Stream<Arguments> getTestData2()
    {
        var h1 = new HitBox(new Pos(5, 5), 50, 50);
        var h2 = new HitBox(new Pos(20, 10), 60, 40);

        var h3 = new HitBox(new Pos(10, 10), 50, 50);
        var h4 = new HitBox(new Pos(20, 10), 60, 40);

        var h5 = new HitBox(new Pos(5, 5), 5, 5);
        var h6 = new HitBox(new Pos(20, 10), 5, 5);

        return Stream.of(
            Arguments.of(h1, h2, true),
            Arguments.of(h3, h4, true),
            Arguments.of(h5, h6, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData2")
    public void testHitBoxOverlapping(HitBox h1, HitBox h2, boolean expected)
    {
        assertEquals(expected, h1.isHitBoxOverlapping(h2));
        assertEquals(expected, h2.isHitBoxOverlapping(h1));
    }
}
