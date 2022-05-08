package se.skorup.API.expression_evalutator.parser;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
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
        list.add(new TestParserData("let cookie = (123 - 2) * 7", (123 - 2) * 7, alwaysZeroEnv));
        list.add(new TestParserData("(let a = 3)", 3, alwaysZeroEnv));
        list.add(new TestParserData("(let a = 23 * 3) + 7", 23 * 3 + 7, alwaysZeroEnv));
        list.add(new TestParserData("(let a = 5 * (10 + 3)) - -3", 5 * (10 + 3) - -3, alwaysZeroEnv));
        list.add(new TestParserData("let a = ((4 + (cookie + 3)) - 3)", ((4 + (COOKIE + 3)) - 3), cookieEnv));
        list.add(new TestParserData("5 % 2", 5 % 2, alwaysZeroEnv));
        list.add(new TestParserData("(let x = 5) % 2", 1, alwaysZeroEnv));
        list.add(new TestParserData("4 * (((cookie + 3) % (cookie + 3)) + 1)", 4, cookieEnv));
        list.add(new TestParserData("(123 * 123 + 321432 - -321) % 2", (123 * 123 + 321432 - -321) % 2, alwaysZeroEnv));
        list.add(new TestParserData("6 + 2 % 2", 6, alwaysZeroEnv));
        list.add(new TestParserData("(6 + 2) % 2", 0, alwaysZeroEnv));
        list.add(new TestParserData("6 + 1 % 2", 7, alwaysZeroEnv));
        list.add(new TestParserData("(6 + 1) % 2", 1, alwaysZeroEnv));
        list.add(new TestParserData("5 ** 2", 25, alwaysZeroEnv));
        list.add(new TestParserData("5 * 5 ** 2", 5 * Math.pow(5, 2), alwaysZeroEnv));
        list.add(new TestParserData("cookie ** 2", COOKIE * COOKIE, cookieEnv));
        list.add(new TestParserData("(cookie ** 2) % 2", 0, cookieEnv));
        list.add(new TestParserData("5 ** (2 ** 2)", Math.pow(5, 4), alwaysZeroEnv));
        list.add(new TestParserData("2 ** (3 ** 4)", Math.pow(2, Math.pow(3, 4)), alwaysZeroEnv));
        list.add(new TestParserData("5 ** 2 ** 2", Math.pow(5, 4), alwaysZeroEnv));
        list.add(new TestParserData("2 ** 3 ** 4", Math.pow(2, Math.pow(3, 4)), alwaysZeroEnv));
        list.add(new TestParserData("let x = 5 ** 2 ** 3", Math.pow(5, 8), alwaysZeroEnv));
        list.add(new TestParserData("(let x = 3) ** (let y = 4) ** (let z = 5)", Math.pow(3, Math.pow(4, 5)), alwaysZeroEnv));
        list.add(new TestParserData("5 ** 2 + 5 ** 2", 50, alwaysZeroEnv));
        list.add(new TestParserData("(2 ** 2) ** 4", Math.pow(4, 4), alwaysZeroEnv));
        list.add(new TestParserData("-2 ** 2", Math.pow(2, 2), alwaysZeroEnv));
        list.add(new TestParserData("-2 ** 2 ** 2", Math.pow(2, Math.pow(2, 2)), alwaysZeroEnv));
        list.add(new TestParserData("-2 ** 2 ** 2 ** 2", 65536, alwaysZeroEnv));
        list.add(new TestParserData("(-2) ** (-2)", Math.pow(-2, -2), alwaysZeroEnv));
        list.add(new TestParserData("-(-2) ** (-2)", Math.pow(-2, -2), alwaysZeroEnv));


        var arr = new TestParserData[list.size()];
        list.toArray(arr);
        return arr;
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testParser(TestParserData t)
    {
        var p = new Parser(t.expression);
        assertEquals(t.expected, p.parse().getValue(t.env), "ACTUAL VALUE");
        assertEquals(0, p.getDiagnostics().size(), "ERRORS");
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
