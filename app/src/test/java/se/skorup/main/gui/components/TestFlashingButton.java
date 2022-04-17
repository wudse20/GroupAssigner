package se.skorup.main.gui.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The class that tests the flashing button.
 * */
public class TestFlashingButton
{
    public static Stream<Arguments> getButtons()
    {
        var btn = new FlashingButton("Test1");
        btn.startFlashing(100, Color.RED);

        var btn2 = new FlashingButton("Test2");
        btn2.startFlashing(100, Color.RED, Color.BLUE, Color.GREEN);

        return Stream.of(
            Arguments.of(new FlashingButton("Test3")),
            Arguments.of(btn), Arguments.of(btn2)
        );
    }

    public static Stream<Arguments> getButtons2()
    {
        var btn = new FlashingButton("Test");

        return Stream.of(
            Arguments.of(btn, 0, new Color[0]),
            Arguments.of(btn, 24, new Color[0]),
            Arguments.of(btn, 25, new Color[0]),
            Arguments.of(btn, 0, new Color[] { Color.RED }),
            Arguments.of(btn, 24, new Color[] { Color.RED }),
            Arguments.of(btn, 24, new Color[] { Color.RED, Color.BLUE }),
            Arguments.of(btn, 25, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getButtons")
    public void testStopFlashing(FlashingButton btn)
    {
        assertDoesNotThrow(btn::stopFlashing);
    }

    @ParameterizedTest
    @MethodSource("getButtons")
    public void testNotThrows(FlashingButton btn)
    {
        assertDoesNotThrow(btn::stopFlashing);
        assertDoesNotThrow(() -> btn.startFlashing(100, Color.RED, Color.BLUE, Color.BLACK));
        assertDoesNotThrow(btn::stopFlashing);
    }

    @ParameterizedTest
    @MethodSource("getButtons2")
    public void testThrows(FlashingButton btn, int speed, Color[] colors)
    {
        assertThrows(IllegalArgumentException.class, () -> btn.startFlashing(speed, colors));
    }
}
