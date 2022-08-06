package se.skorup.main.gui.components.objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.main.gui.components.enums.State;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTemplate
{
    public static Stream<Arguments> getAddingData() {
        var al1 = new ArrayList<TemplateItem>();

        for (int i = 0; i < 10; i++)
            al1.add(new TemplateItem(State.WISH, i));

        var al2 = new ArrayList<>(al1);
        al2.add(null);
        al2.add(null);
        al2.add(null);

        var al3 = new ArrayList<TemplateItem>();
        for (int i = 0; i < 10; i++)
            al3.add(null);

        return Stream.of(
            Arguments.of(List.of(), 0),
            Arguments.of(List.of(new TemplateItem(State.WISH, 2), new TemplateItem(State.WISH, 2)), 2),
            Arguments.of(al1, 10),
            Arguments.of(al2, 10),
            Arguments.of(al3, 0)
        );
    }

    public static Stream<Arguments> equalityTest() {
        var t1 = new Template();
        var t2 = new Template();
        var t3 = new Template();
        var t4 = new Template();

        var al1 = new ArrayList<TemplateItem>();
        var al2 = new ArrayList<TemplateItem>();

        for (var i = 0; i < 100; i++)
        {
            al1.add(new TemplateItem(State.WISH, i));

            if (i < 50)
                al2.add(new TemplateItem(State.WISH, i));
        }

        for (var i : al1)
        {
            t1.addTemplateItem(i);
            t2.addTemplateItem(i);
            t3.addTemplateItem(i);
        }

        for (var i : al2)
            t4.addTemplateItem(i);

        return Stream.of(
            Arguments.of(new Template(), new Template(), true),
            Arguments.of(new Template(), new TemplateItem(State.WISH, 0), false),
            Arguments.of(new Template(), null, false),
            Arguments.of(t1, t2, true),
            Arguments.of(t1, t3, true),
            Arguments.of(t2, t3, true),
            Arguments.of(t1, t4, false),
            Arguments.of(t2, t4, false),
            Arguments.of(t3, t4, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getAddingData")
    public void testAdding(List<TemplateItem> items, int expectedSize)
    {
        var template = new Template();
        var count = 0;

        assertEquals(count, template.size(), "Should be empty on creation.");

        for (var i : items)
        {
            if (i != null)
                count++;

            template.addTemplateItem(i);

            assertEquals(count, template.size(), "Intermediate size check.");
        }

        assertEquals(expectedSize, template.size(), "Checking against expected size.");
    }

    @ParameterizedTest
    @MethodSource("equalityTest")
    public void testEquality(Object a, Object b, boolean expected)
    {
        if (a != null && b != null)
            assertEquals(expected, a.hashCode() == b.hashCode(), "a.hashCode == b.hashCode");

        if (a != null)
            assertEquals(expected, a.equals(b), "a.equals(b)");

        if (b != null)
            assertEquals(expected, b.equals(a), "b.equals(a)");
    }
}
