package se.skorup.API.expression_evalutator.parser;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.expression_evalutator.Environment;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestParser
{
    public static final int COOKIE = 42;

    private final Environment alwaysZeroEnv = new Environment() {
        @Override
        public double getValue(String key) { return 0; }

        @Override
        public void registerValue(String key, double value) {}
    };

    private final Environment cookieEnv = new Environment() {
        @Override
        public double getValue(String key) { return key.equals("cookie") ? COOKIE : 0;}

        @Override
        public void registerValue(String key, double value) {}
    };

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
        list.add(new TestParserData("let x = 5", 5, alwaysZeroEnv));
        list.add(new TestParserData("let y = 123 * 123", 123 * 123, alwaysZeroEnv));
        list.add(new TestParserData("let z = ( 5 * 5 - 3 )", 22, alwaysZeroEnv));
        list.add(new TestParserData("let cookie = 123 * 3123 * 0.5", 123 * 3123 * .5d, alwaysZeroEnv));

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
        assertEquals(0, p.getDiagnostics().size());
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testNotThrow(TestParserData t)
    {
        var p = new Parser(t.expression());
        assertDoesNotThrow(p::parse);
    }

    private record TestParserData(String expression, double expected, Environment env) {}
}
