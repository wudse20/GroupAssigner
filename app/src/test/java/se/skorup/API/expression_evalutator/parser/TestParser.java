package se.skorup.API.expression_evalutator.parser;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.expression_evalutator.Environment;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestParser
{
    public static final int COOKIE = 42;
    private final Environment alwaysZeroEnv = s -> 0;
    private final Environment cookieEnv = s -> s.equals("cookie") ? COOKIE : 0;

    /**
     * Gets the test data.
     *
     * @return an array containing the test data.
     * */
    public TestParserData[] getData()
    {
        var list = new ArrayList<TestParserData>();

        list.add(new TestParserData("4 * 3", 12, alwaysZeroEnv));
        list.add(new TestParserData("cookie", COOKIE, cookieEnv));
        list.add(new TestParserData("(-(4 * 3) + 7 * (2 + 5) + 1) * (1 / 2) - 4", 15, alwaysZeroEnv));
        list.add(new TestParserData("-(4 * 3) + 7 * (2 + 5)", 37, alwaysZeroEnv));
        list.add(new TestParserData("(4 + 3) * (3 - 2)", 7, alwaysZeroEnv));
        list.add(new TestParserData("-(5 + 5)", -10, alwaysZeroEnv));
        list.add(new TestParserData("+(5 + 5)", 10, alwaysZeroEnv));
        list.add(new TestParserData("(5 + 5) * 5", 50, alwaysZeroEnv));
        list.add(new TestParserData("(5 + 5)", 10, alwaysZeroEnv));
        list.add(new TestParserData("5 / 5 * 5 + +--7 - -3", 15, alwaysZeroEnv));
        list.add(new TestParserData("5 / 5 * 5 + 7 - 3", 9, alwaysZeroEnv));
        list.add(new TestParserData("-5 / 5", -1, alwaysZeroEnv));
        list.add(new TestParserData("5 / 5", 1, alwaysZeroEnv));
        list.add(new TestParserData("-5 * -5 + -5 - -5", 25d, alwaysZeroEnv));
        list.add(new TestParserData("-5 * -5", 25d, alwaysZeroEnv));
        list.add(new TestParserData("5 + 5 * 5", 30d, alwaysZeroEnv));
        list.add(new TestParserData("5 * 5 + 5", 30d, alwaysZeroEnv));
        list.add(new TestParserData("5 * 5 * 5", 125d, alwaysZeroEnv));
        list.add(new TestParserData("5 * 5", 25d, alwaysZeroEnv));
        list.add(new TestParserData("5-+5", 0d, alwaysZeroEnv));
        list.add(new TestParserData("5--5", 10d, alwaysZeroEnv));
        list.add(new TestParserData("5 - 4 - 1", 0d, alwaysZeroEnv));
        list.add(new TestParserData("5 - 4", 1d, alwaysZeroEnv));
        list.add(new TestParserData("-7 + +3 + --4", 0d, alwaysZeroEnv));
        list.add(new TestParserData("7 + 3", 10d, alwaysZeroEnv));
        list.add(new TestParserData("5+5+5", 15d, alwaysZeroEnv));
        list.add(new TestParserData("5+5", 10d, alwaysZeroEnv));
        list.add(new TestParserData("+5", 5d, alwaysZeroEnv));
        list.add(new TestParserData("-5", -5d, alwaysZeroEnv));
        list.add(new TestParserData("5", 5d, alwaysZeroEnv));
        list.add(new TestParserData("(cookie - 3) * 4", (COOKIE - 3) * 4, cookieEnv));
        list.add(new TestParserData("(cookie - 3) * 4", -12, alwaysZeroEnv));
        list.add(new TestParserData("-cookie", -COOKIE, cookieEnv));
        list.add(new TestParserData("-cookie * 3", -COOKIE * 3, cookieEnv));
        list.add(new TestParserData("----cookie * 3 - 7", COOKIE * 3 - 7, cookieEnv));

        var arr = new TestParserData[list.size()];
        list.toArray(arr);
        return arr;
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testParser(TestParserData t)
    {
        var p = new Parser(t.expression);
        assertEquals(t.expected, p.parse().getValue(t.env));
        assertEquals(p.getDiagnostics().size(), 0);
    }

    private record TestParserData(String expression, double expected, Environment env) {}
}
