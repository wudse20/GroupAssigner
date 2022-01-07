package se.skorup.API.collections.mutable_collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The class responsible for testing the history list.
 * */
public class HistoryListTester
{
    public static Stream<TestAdding<String>> getAddData1()
    {
        var al = new ArrayList<TestAdding<String>>();

        al.add(new TestAdding<>(0, new String[] {}));
        al.add(new TestAdding<>(1, "hej"));
        al.add(new TestAdding<>(2, "hej", "då"));

        var arr = new String[10000];
        for (var i = 0; i < 10000; i++)
        {
            arr[i] = Integer.toString(i, 16);
        }

        al.add(new TestAdding<>(arr.length, arr));

        var arr2 = new String[1000000];
        var r = new Random(123123);
        for (var i = 0; i < 1000000; i++)
        {
            arr2[i] = Integer.toString(r.nextInt(0, i + 1), 16);
        }

        al.add(new TestAdding<>(arr2.length, arr2));

        return al.stream();
    }

    public static Stream<TestAdding<Integer>> getAddData2()
    {
        var al = new ArrayList<TestAdding<Integer>>();

        al.add(new TestAdding<>(0, new Integer[] {}));
        al.add(new TestAdding<>(2, 1, 2));
        al.add(new TestAdding<>(4, 1, 2, 3, 4));

        var arr = new Integer[10000];
        for (var i = 0; i < 10000; i++)
        {
            arr[i] = i;
        }

        al.add(new TestAdding<>(arr.length, arr));

        var arr2 = new Integer[1000000];
        var r = new Random(123123);
        for (var i = 0; i < 1000000; i++)
        {
            arr2[i] = r.nextInt(0, i + 1);
        }

        al.add(new TestAdding<>(arr2.length, arr2));

        return al.stream();
    }

    public static Stream<ToStringTest<Integer>> getToStringData()
    {
        var al = new ArrayList<ToStringTest<Integer>>();

        al.add(new ToStringTest<>(new HistoryList<>(), "[]"));

        var hl1 = new HistoryList<Integer>();
        hl1.add(4);
        hl1.add(6);
        hl1.add(-123);
        al.add(new ToStringTest<>(hl1, "[4, 6, -123]"));

        var hl2 = new HistoryList<Integer>();
        var sb = new StringBuilder().append('[');
        var r = new Random(123);

        for (var i = 1; i <= 10000; i++)
        {
            var num = r.nextInt(0, i);
            hl2.add(num);

            if (i == 10000)
            {
                sb.append(num).append(']');
            }
            else
            {
                sb.append(num).append(", ");
            }
        }

        al.add(new ToStringTest<>(hl2, sb.toString()));

        var hl3 = new HistoryList<Integer>();
        var sb2 = new StringBuilder().append('[');

        for (var i = 1; i <= 1000000; i++)
        {
            var num = r.nextInt(0, i);
            hl3.add(num);

            if (i == 1000000)
            {
                sb2.append(num).append(']');
            }
            else
            {
                sb2.append(num).append(", ");
            }
        }

        al.add(new ToStringTest<>(hl3, sb2.toString()));


        return al.stream();
    }

    public static Stream<ToStringTest<Character>> getToStringData2()
    {
        var al = new ArrayList<ToStringTest<Character>>();

        al.add(new ToStringTest<>(new HistoryList<>(), "[]"));

        var hl1 = new HistoryList<Character>();
        hl1.add('a');
        hl1.add('b');
        hl1.add('c');
        al.add(new ToStringTest<>(hl1, "[a, b, c]"));

        var hl2 = new HistoryList<Character>();
        var sb = new StringBuilder().append('[');
        var r = new Random(123);

        for (var i = 1; i <= 10000; i++)
        {
            var num = (char) (r.nextInt(0, 10) + '0');
            hl2.add(num);

            if (i == 10000)
            {
                sb.append(num).append(']');
            }
            else
            {
                sb.append(num).append(", ");
            }
        }

        al.add(new ToStringTest<>(hl2, sb.toString()));

        var hl3 = new HistoryList<Character>();
        var sb2 = new StringBuilder().append('[');

        for (var i = 1; i <= 1000000; i++)
        {
            var num = (char) (r.nextInt(0, 10) + '0');
            hl3.add(num);

            if (i == 1000000)
            {
                sb2.append(num).append(']');
            }
            else
            {
                sb2.append(num).append(", ");
            }
        }

        al.add(new ToStringTest<>(hl3, sb2.toString()));

        return al.stream();
    }

    @Test
    public void testEmptySize()
    {
        assertEquals(0, new HistoryList<String>().size());
    }

    @ParameterizedTest
    @MethodSource({"getAddData1", "getAddData2"})
    public <T> void testAdding1(TestAdding<T> t)
    {
        var hl = new HistoryList<T>();
        assertEquals(0, hl.size());

        for (var i = 0; i < t.elems.length; i++)
        {
            hl.add(t.elems[i]);
        }

        assertEquals(t.expectedSize, hl.size(), t.toString());
    }

    @ParameterizedTest
    @MethodSource({"getAddData1", "getAddData2"})
    public <T> void testAdding2(TestAdding<T> t)
    {
        var hl = new HistoryList<T>();
        assertEquals(0, hl.size());

        for (var i = 0; i < t.elems.length; i++)
        {
            hl.add(t.elems[i]);

            if (i != 0)
                assertNotEquals(t.elems[i], hl.peek(), "NEQ");
            else
                assertEquals(t.elems[i], hl.peek().orElse(null), "EQ");
        }

        assertEquals(t.expectedSize, hl.size());

        hl.reset();

        for (var e : t.elems)
        {
            assertEquals(e, hl.peek().orElse(null), "Backwards");
            hl.backward();
        }

        assertEquals(Optional.empty(), hl.backward(), "Last backward");

        for (var i = t.elems.length - 1; i >= 0; i--)
        {
            assertEquals(t.elems[i], hl.peek().orElse(null), "Forwards");
            hl.forward();
        }

        assertEquals(Optional.empty(), hl.forward(), "Last forward");
    }

    @ParameterizedTest
    @MethodSource({"getToStringData", "getToStringData2"})
    public <T> void testToString(ToStringTest<T> t)
    {
        assertEquals(t.expected, t.hl.toString());
    }

    private record TestAdding<E>(int expectedSize, E... elems)
    {
        @SafeVarargs
        public TestAdding {} // To stop it from complaining.

        @Override
        public String toString()
        {
            return "TestAdding[expectedSize: %d, elems: %s]".formatted(expectedSize, Arrays.toString(elems));
        }
    }

    private record ToStringTest<E>(HistoryList<E> hl, String expected) {}
}
