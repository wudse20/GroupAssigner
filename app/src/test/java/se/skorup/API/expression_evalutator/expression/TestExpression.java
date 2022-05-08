package se.skorup.API.expression_evalutator.expression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * The tests for the Expressions.
 * */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestExpression
{
    private Environment alwaysZeroEnv;

    @BeforeEach
    public void setUp()
    {
        this.alwaysZeroEnv = new Environment() {
            @Override
            public double getValue(String key) { return 0; }
            @Override
            public void registerValue(String key, double value) {}
        };
    }

    @Test
    public void testNumber()
    {
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(-1);
        var n3 = new NumberExpression(-.5d);

        assertEquals(10d, n1.getValue(alwaysZeroEnv));
        assertEquals(-1d, n2.getValue(alwaysZeroEnv));
        assertEquals(-.5d, n3.getValue(alwaysZeroEnv));
    }

    @Test
    public void testPlus()
    {
        // 10 + 14 = 24
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var plus = new Plus(n1, n2);

        assertEquals(24d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testPlusNested()
    {
        // 6 + 4 + 12 = 22
        var n1 = new NumberExpression(6);
        var n2 = new NumberExpression(4);
        var n3 = new NumberExpression(12);
        var p1 = new Plus(n1, n2);
        var plus = new Plus(p1, n3);

        assertEquals(22d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMinus()
    {
        // 10 - 14 = -4
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var minus = new Minus(n1, n2);

        assertEquals(-4d, minus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMinusNested()
    {
        // 6 + 4 - 12 = -2
        var n1 = new NumberExpression(6);
        var n2 = new NumberExpression(4);
        var n3 = new NumberExpression(12);
        var p1 = new Plus(n1, n2);
        var minus = new Minus(p1, n3);

        assertEquals(-2d, minus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMultiply()
    {
        // 10 * 14 = 140
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var mul = new Multiplication(n1, n2);

        assertEquals(140d, mul.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMultiplyNested()
    {
        // 6 + 4 * 12 = 54
        var n1 = new NumberExpression(6);
        var n2 = new NumberExpression(4);
        var n3 = new NumberExpression(12);
        var mul = new Multiplication(n2, n3);
        var p1 = new Plus(n1, mul);

        assertEquals(54d, p1.getValue(alwaysZeroEnv));
    }

    @Test
    public void testDivision()
    {
        // 20 / 2 = 10
        var n1 = new NumberExpression(20);
        var n2 = new NumberExpression(2);
        var div = new Division(n1, n2);

        assertEquals(10d, div.getValue(alwaysZeroEnv));
    }

    @Test
    public void testDivisionNested()
    {
        // 6 / 4 + 3 = 4.5
        var n1 = new NumberExpression(6);
        var n2 = new NumberExpression(4);
        var n3 = new NumberExpression(3);
        var div = new Division(n1, n2);
        var p1 = new Plus(div, n3);

        assertEquals(4.5, p1.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryMinus()
    {
        // -4
        var n1 = new NumberExpression(4);
        var minus = new UnaryMinus(n1);

        assertEquals(-4d, minus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMultipleUnaryMinuses()
    {
        // ---4 = -4
        var n1 = new NumberExpression(4);
        var minus = new UnaryMinus(new UnaryMinus(new UnaryMinus(n1)));

        assertEquals(-4d, minus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryPlus()
    {
        // +4
        var n1 = new NumberExpression(4);
        var plus = new UnaryPlus(n1);

        assertEquals(+4d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testMultipleUnaryPluses()
    {
        // +++4 = +4
        var n1 = new NumberExpression(4);
        var plus = new UnaryPlus(new UnaryPlus(new UnaryPlus(n1)));

        assertEquals(+4d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryMinusesAndUnaryPluses()
    {
        // -+-+4 = 4
        var n1 = new NumberExpression(4);
        var plus =
            new UnaryMinus(
                new UnaryPlus(
                    new UnaryMinus(
                        new UnaryPlus(n1)
                    )
                )
            );

        assertEquals(4d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryMinusesAndUnaryPluses2()
    {
        // -+-+-+4 = -4
        var n1 = new NumberExpression(4);
        var plus =
            new UnaryMinus(
                new UnaryPlus(
                    new UnaryMinus(
                        new UnaryPlus(
                            new UnaryMinus(
                                new UnaryPlus(n1)
                            )
                        )
                    )
                )
            );

        assertEquals(-4d, plus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testParenthesizedExpression()
    {
        // (4 + 5) = 9
        var p =
            new ParenthesizedExpression(
                new Plus(
                    new NumberExpression(4),
                    new NumberExpression(5)
                ));

        assertEquals(9d, p.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryMinusParenthesizedExpression()
    {
        // -(4 + 5) = -9
        var p =
            new ParenthesizedExpression(
                new Plus(
                    new NumberExpression(4),
                    new NumberExpression(5)
                ));

        var unaryMinus = new UnaryMinus(p);

        assertEquals(-9d, unaryMinus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testUnaryPlusParenthesizedExpression()
    {
        // +(4 + 5) = 9
        var p =
            new ParenthesizedExpression(
                new Plus(
                    new NumberExpression(4),
                    new NumberExpression(5)
                ));

        var unaryPlus = new UnaryPlus(p);

        assertEquals(9d, unaryPlus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testDefinitionExpression()
    {
        var env = new Environment() {
            private final Map<String, Double> values = new HashMap<>();

            @Override
            public double getValue(String key)
            {
                return values.getOrDefault(key, 0d);
            }

            @Override
            public void registerValue(String key, double value)
            {
                values.put(key, value);
            }
        };

        var expr = new Parser("7 * 7 + 5 - 3").parse();
        var def = new DefinitionExpression("hej", expr);
        var constant = new VariableExpression("hej");

        assertEquals(expr.getValue(env), def.getValue(env));
        assertEquals(expr.getValue(env), constant.getValue(env));
    }

    @Test
    public void testModuloExpression()
    {
        var expr = new Modulo(new NumberExpression(5), new NumberExpression(2));
        assertEquals(1, expr.getValue(alwaysZeroEnv));
    }

    @Test
    public void testModuloExpressionMoreComplex()
    {
        var expr1 = new Parser("123 * 123 + 32").parse();
        var expr2 = new Parser("523 * 2 - (3 + 2)").parse();
        var mod = new Modulo(expr1, expr2);

        assertEquals((123 * 123 + 32) % (523 * 2 - (3 + 2)), mod.getValue(alwaysZeroEnv));
    }

    @Test
    public void testPowerExpression()
    {
        var expr1 = new Parser("5 ** 2").parse();
        var expr2 = new Parser("5").parse();
        var power = new Power(expr1, expr2);

        assertEquals(Math.pow(25, 5), power.getValue(alwaysZeroEnv));
    }

    public ToStringTest[] getData()
    {
        var list = new ArrayList<ToStringTest>();

        list.add(new ToStringTest("5.0 + 5.0", new Parser("5+5").parse()));
        list.add(new ToStringTest("(5.0 + 5.0) * 3.0", new Parser("(5 + 5) * 3").parse()));
        list.add(new ToStringTest("+var(kaka) + -5.0", new Parser("+kaka + -5").parse()));
        list.add(new ToStringTest("5.0 / 5.0", new Parser("5/5").parse()));
        list.add(new ToStringTest("5.0 - 5.0", new Parser("5-5").parse()));
        list.add(new ToStringTest("let x = 4.0", new Parser("let x = 4").parse()));
        list.add(new ToStringTest("let x = 5.0 + 3.0", new Parser("let x = 5 + 3").parse()));
        list.add(new ToStringTest("5.0 % 2.0", new Parser("5 % 2").parse()));
        list.add(new ToStringTest("var(x) % 2.0", new Parser("x % 2").parse()));
        list.add(new ToStringTest("5.0 ** 2.0", new Parser("5 ** 2").parse()));
        list.add(new ToStringTest("5.0 ** var(x)", new Parser("5 ** x").parse()));

        var arr = new ToStringTest[list.size()];
        list.toArray(arr);
        return arr;
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testToString(ToStringTest tst)
    {
        assertEquals(tst.expected, tst.expr.toString());
    }

    private record ToStringTest(String expected, Expression expr) {}
}
