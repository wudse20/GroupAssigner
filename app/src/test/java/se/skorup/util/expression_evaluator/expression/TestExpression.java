package se.skorup.util.expression_evaluator.expression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.util.Utils;
import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.Type;
import se.skorup.util.expression_evaluator.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestExpression
{
    private Environment alwaysZeroEnv;
    private Environment nonZeroEnv;

    @BeforeEach
    public void setUp()
    {
        this.alwaysZeroEnv = new Environment() {
            @Override
            public Number getValue(String key) { return 0; }
            @Override
            public void registerValue(String key, Number value) {}
        };

        this.nonZeroEnv = new Environment() {
            @Override
            public Number getValue(String key)
            {
                if (key.equals("COOKIE"))
                    return 42;
                else if (key.equals("COOKIED"))
                    return 42d;

                return 0;
            }

            @Override
            public void registerValue(String key, Number value) {}
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
    public void testInteger()
    {
        var n1 = new IntegerExpression(10);
        var n2 = new IntegerExpression(-1);

        assertEquals(10, n1.getValue(alwaysZeroEnv));
        assertEquals(-1, n2.getValue(alwaysZeroEnv));
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
    public void testPlusInteger()
    {
        // 10 + 14 = 24
        var n1 = new IntegerExpression(10);
        var n2 = new IntegerExpression(14);
        var plus = new Plus(n1, n2);

        assertEquals(24, plus.getValue(alwaysZeroEnv));
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
    public void testPlusNestedInteger()
    {
        // 6 + 4 + 12 = 22
        var n1 = new IntegerExpression(6);
        var n2 = new IntegerExpression(4);
        var n3 = new IntegerExpression(12);
        var p1 = new Plus(n1, n2);
        var plus = new Plus(p1, n3);

        assertEquals(22, plus.getValue(alwaysZeroEnv));
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
    public void testMinusInteger()
    {
        // 10 - 14 = -4
        var n1 = new IntegerExpression(10);
        var n2 = new IntegerExpression(14);
        var minus = new Minus(n1, n2);

        assertEquals(-4, minus.getValue(alwaysZeroEnv));
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
    public void testMinusNestedInteger()
    {
        // 6 + 4 - 12 = -2
        var n1 = new IntegerExpression(6);
        var n2 = new IntegerExpression(4);
        var n3 = new IntegerExpression(12);
        var p1 = new Plus(n1, n2);
        var minus = new Minus(p1, n3);

        assertEquals(-2, minus.getValue(alwaysZeroEnv));
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
    public void testMultiplyInteger()
    {
        // 10 * 14 = 140
        var n1 = new IntegerExpression(10);
        var n2 = new IntegerExpression(14);
        var mul = new Multiplication(n1, n2);

        assertEquals(140, mul.getValue(alwaysZeroEnv));
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
    public void testMultiplyNestedInteger()
    {
        // 6 + 4 * 12 = 54
        var n1 = new IntegerExpression(6);
        var n2 = new IntegerExpression(4);
        var n3 = new IntegerExpression(12);
        var mul = new Multiplication(n2, n3);
        var p1 = new Plus(n1, mul);

        assertEquals(54, p1.getValue(alwaysZeroEnv));
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
    public void testDivisionInteger()
    {
        // 20 / 2 = 10
        var n1 = new IntegerExpression(20);
        var n2 = new IntegerExpression(2);
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
    public void testDivisionNestedInteger()
    {
        // 6 / 4 + 3 = 4.5
        var n1 = new IntegerExpression(6);
        var n2 = new IntegerExpression(4);
        var n3 = new IntegerExpression(3);
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
    public void testUnaryMinusInteger()
    {
        // -4
        var n1 = new IntegerExpression(4);
        var minus = new UnaryMinus(n1);

        assertEquals(-4, minus.getValue(alwaysZeroEnv));
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
    public void testMultipleUnaryMinusesInteger()
    {
        // ---4 = -4
        var n1 = new IntegerExpression(4);
        var minus = new UnaryMinus(new UnaryMinus(new UnaryMinus(n1)));

        assertEquals(-4, minus.getValue(alwaysZeroEnv));
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
    public void testUnaryPlusInteger()
    {
        // +4
        var n1 = new IntegerExpression(4);
        var plus = new UnaryPlus(n1);

        assertEquals(+4, plus.getValue(alwaysZeroEnv));
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
    public void testMultipleUnaryPlusesInteger()
    {
        // +++4 = +4
        var n1 = new IntegerExpression(4);
        var plus = new UnaryPlus(new UnaryPlus(new UnaryPlus(n1)));

        assertEquals(+4, plus.getValue(alwaysZeroEnv));
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
    public void testUnaryMinusesAndUnaryPlusesInteger()
    {
        // -+-+4 = 4
        var n1 = new IntegerExpression(4);
        var plus =
                new UnaryMinus(
                        new UnaryPlus(
                                new UnaryMinus(
                                        new UnaryPlus(n1)
                                )
                        )
                );

        assertEquals(4, plus.getValue(alwaysZeroEnv));
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
    public void testUnaryMinusesAndUnaryPluses2Integer()
    {
        // -+-+-+4 = -4
        var n1 = new IntegerExpression(4);
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

        assertEquals(-4, plus.getValue(alwaysZeroEnv));
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
    public void testParenthesizedExpressionInteger()
    {
        // (4 + 5) = 9
        var p =
                new ParenthesizedExpression(
                        new Plus(
                                new IntegerExpression(4),
                                new IntegerExpression(5)
                        ));

        assertEquals(9, p.getValue(alwaysZeroEnv));
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
    public void testUnaryMinusParenthesizedExpressionInteger()
    {
        // -(4 + 5) = -9
        var p =
                new ParenthesizedExpression(
                        new Plus(
                                new IntegerExpression(4),
                                new IntegerExpression(5)
                        ));

        var unaryMinus = new UnaryMinus(p);

        assertEquals(-9, unaryMinus.getValue(alwaysZeroEnv));
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
    public void testUnaryPlusParenthesizedExpressionInteger()
    {
        // +(4 + 5) = 9
        var p =
                new ParenthesizedExpression(
                        new Plus(
                                new IntegerExpression(4),
                                new IntegerExpression(5)
                        ));

        var unaryPlus = new UnaryPlus(p);

        assertEquals(9, unaryPlus.getValue(alwaysZeroEnv));
    }

    @Test
    public void testDefinitionExpression()
    {
        var env = new Environment() {
            private final Map<String, Number> values = new HashMap<>();

            @Override
            public Number getValue(String key)
            {
                return values.getOrDefault(key, 0d);
            }

            @Override
            public void registerValue(String key, Number value)
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
        assertEquals(1.0, expr.getValue(alwaysZeroEnv));
    }

    @Test
    public void testModuloExpressionInteger()
    {
        var expr = new Modulo(new IntegerExpression(5), new IntegerExpression(2));
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
        var expr2 = new Parser("2").parse();
        var power = new Power(expr1, expr2);

        assertEquals(Utils.pow(5, Utils.pow(2, 2)), power.getValue(alwaysZeroEnv));
    }

    public static ToStringTest[] getData()
    {
        var list = new ArrayList<ToStringTest>();

        // Integer
        list.add(new ToStringTest("5 + 5", new Parser("5+5").parse()));
        list.add(new ToStringTest("(5 + 5) * 3", new Parser("(5 + 5) * 3").parse()));
        list.add(new ToStringTest("+kaka + -5", new Parser("+kaka + -5").parse()));
        list.add(new ToStringTest("5 / 5", new Parser("5/5").parse()));
        list.add(new ToStringTest("5 - 5", new Parser("5-5").parse()));
        list.add(new ToStringTest("let x = 4", new Parser("let x = 4").parse()));
        list.add(new ToStringTest("let x = 5 + 3", new Parser("let x = 5 + 3").parse()));
        list.add(new ToStringTest("5 % 2", new Parser("5 % 2").parse()));
        list.add(new ToStringTest("x % 2", new Parser("x % 2").parse()));
        list.add(new ToStringTest("5 ** 2", new Parser("5 ** 2").parse()));
        list.add(new ToStringTest("5 ** x", new Parser("5 ** x").parse()));
        list.add(new ToStringTest("5 * kaka + kakor", new Parser("5 * kaka + kakor").parse()));

        // Double
        list.add(new ToStringTest("5.0 + 5.0", new Parser("5.0+5.0").parse()));
        list.add(new ToStringTest("(5.0 + 5.0) * 3.0", new Parser("(5.0 + 5.0) * 3.0").parse()));
        list.add(new ToStringTest("+kaka + -5.0", new Parser("+kaka + -5.0").parse()));
        list.add(new ToStringTest("5.0 / 5.0", new Parser("5.0/5.0").parse()));
        list.add(new ToStringTest("5.0 - 5.0", new Parser("5.0-5.0").parse()));
        list.add(new ToStringTest("let x = 4.0", new Parser("let x = 4.0").parse()));
        list.add(new ToStringTest("let x = 5.0 + 3.0", new Parser("let x = 5.0 + 3.0").parse()));
        list.add(new ToStringTest("5.0 % 2.0", new Parser("5.0 % 2.0").parse()));
        list.add(new ToStringTest("x % 2.0", new Parser("x % 2.0").parse()));
        list.add(new ToStringTest("5.0 ** 2.0", new Parser("5.0 ** 2.0").parse()));
        list.add(new ToStringTest("5.0 ** x", new Parser("5.0 ** x").parse()));
        list.add(new ToStringTest("5.0 * kaka + kakor", new Parser("5.0 * kaka + kakor").parse()));

        var arr = new ToStringTest[list.size()];
        list.toArray(arr);
        return arr;
    }

    public static Stream<TypingTest> getTypeData()
    {
        var list = new ArrayList<TypingTest>();

        list.add(new TypingTest("5 / 5", Type.DOUBLE));
        list.add(new TypingTest("5.0 / 5", Type.DOUBLE));
        list.add(new TypingTest("5 / 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 / 5.0", Type.DOUBLE));
        list.add(new TypingTest("(4 * 3) / 3", Type.DOUBLE));
        list.add(new TypingTest("(5 ** 2) / 5.0", Type.DOUBLE));
        list.add(new TypingTest("(100 / 2) % 2", Type.DOUBLE));
        list.add(new TypingTest("5 + 5", Type.INTEGER));
        list.add(new TypingTest("5.0 + 5", Type.DOUBLE));
        list.add(new TypingTest("5 + 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 + 5.0", Type.DOUBLE));
        list.add(new TypingTest("5 - 5", Type.INTEGER));
        list.add(new TypingTest("5.0 - 5", Type.DOUBLE));
        list.add(new TypingTest("5 - 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 - 5.0", Type.DOUBLE));
        list.add(new TypingTest("5 * 5", Type.INTEGER));
        list.add(new TypingTest("5.0 * 5", Type.DOUBLE));
        list.add(new TypingTest("5 * 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 * 5.0", Type.DOUBLE));
        list.add(new TypingTest("5 ** 5", Type.INTEGER));
        list.add(new TypingTest("5.0 ** 5", Type.DOUBLE));
        list.add(new TypingTest("5 ** 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 ** 5.0", Type.DOUBLE));
        list.add(new TypingTest("5 % 5", Type.INTEGER));
        list.add(new TypingTest("5.0 % 5", Type.DOUBLE));
        list.add(new TypingTest("5 % 5.0", Type.DOUBLE));
        list.add(new TypingTest("5.0 % 5.0", Type.DOUBLE));
        list.add(new TypingTest("let x = 10", Type.INTEGER));
        list.add(new TypingTest("let y = 10 / 5", Type.DOUBLE));
        list.add(new TypingTest("let z = 5.0", Type.DOUBLE));
        list.add(new TypingTest("COOKIE", Type.INTEGER));
        list.add(new TypingTest("COOKIED", Type.DOUBLE));
        list.add(new TypingTest("+COOKIE", Type.INTEGER));
        list.add(new TypingTest("-COOKIE", Type.INTEGER));
        list.add(new TypingTest("+COOKIED", Type.DOUBLE));
        list.add(new TypingTest("-COOKIED", Type.DOUBLE));
        list.add(new TypingTest("5", Type.INTEGER));
        list.add(new TypingTest("5.0", Type.DOUBLE));
        list.add(new TypingTest("5 ** 5 ** 5.0", Type.DOUBLE));
        list.add(new TypingTest("COOKIE * 2 + (let z = 3)", Type.INTEGER));

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testToString(ToStringTest tst)
    {
        assertEquals(tst.expected, tst.expr.toString());
    }

    @ParameterizedTest
    @MethodSource("getTypeData")
    public void testTyping(TypingTest tst)
    {
        var expr = new Parser(tst.expression).parse();
        assertEquals(tst.expected, expr.getType(nonZeroEnv));
    }

    private record ToStringTest(String expected, Expression expr) {}

    private record TypingTest(String expression, Type expected) {}
}