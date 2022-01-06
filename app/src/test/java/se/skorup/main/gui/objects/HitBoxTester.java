package se.skorup.main.gui.objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The class that tests the hit-box.
 * */
public class HitBoxTester
{
    private static final HitBox hb1 = new HitBox(10, 10, 10, 10);

    public static Stream<HitBoxTest> getTestTrue()
    {
        var al = new ArrayList<HitBoxTest>();

        for (int i = 0; i <= hb1.width(); i++)
        {
            for (int ii = 0; ii <= hb1.height(); ii++)
            {
                al.add(new HitBoxTest(hb1, 10 + i, 10 + ii));
            }
        }

        return al.stream();
    }

    public static Stream<HitBoxTest> getTestFalse()
    {
        var al = new ArrayList<HitBoxTest>();

        for (int i = 0; i < hb1.x(); i++)
        {
            for (int ii = 0; ii < hb1.y(); ii++)
            {
                al.add(new HitBoxTest(hb1, i, ii));
            }
        }

        return al.stream();
    }

    @ParameterizedTest
    @MethodSource("getTestTrue")
    public void testHitBoxIsInBounds(HitBoxTest hbt)
    {
        assertTrue(hbt.hb.isCollision(hbt.x, hbt.y));
    }

    @ParameterizedTest
    @MethodSource("getTestFalse")
    public void testHitBoxOutOfBounds(HitBoxTest hbt)
    {
        assertFalse(hbt.hb.isCollision(hbt.x, hbt.y));
    }

    record HitBoxTest(HitBox hb, int x, int y) {}
}
