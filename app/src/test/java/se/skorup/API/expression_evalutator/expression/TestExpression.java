package se.skorup.API.expression_evalutator.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * The tests for the Expressions.
 * */
public class TestExpression
{
    @Test
    public void testNumber()
    {
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(-1);
        var n3 = new NumberExpression(-.5d);

        assertEquals(10d, n1.getValue());
        assertEquals(-1d, n2.getValue());
        assertEquals(-.5d, n3.getValue());
    }

    @Test
    public void testPlus()
    {
        // 10 + 14 = 24
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var plus = new Plus(n1, n2);

        assertEquals(24d, plus.getValue());
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

        assertEquals(22d, plus.getValue());
    }

    @Test
    public void testMinus()
    {
        // 10 - 14 = -4
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var minus = new Minus(n1, n2);

        assertEquals(-4d, minus.getValue());
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

        assertEquals(-2d, minus.getValue());
    }

    @Test
    public void testMultiply()
    {
        // 10 * 14 = 140
        var n1 = new NumberExpression(10);
        var n2 = new NumberExpression(14);
        var mul = new Multiplication(n1, n2);

        assertEquals(140d, mul.getValue());
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

        assertEquals(54d, p1.getValue());
    }

    @Test
    public void testDivision()
    {
        // 20 / 2 = 10
        var n1 = new NumberExpression(20);
        var n2 = new NumberExpression(2);
        var div = new Division(n1, n2);

        assertEquals(10d, div.getValue());
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

        assertEquals(4.5, p1.getValue());
    }

    @Test
    public void testUnaryMinus()
    {
        // -4
        var n1 = new NumberExpression(4);
        var minus = new UnaryMinus(n1);

        assertEquals(-4d, minus.getValue());
    }

    @Test
    public void testMultipleUnaryMinuses()
    {
        // ---4 = -4
        var n1 = new NumberExpression(4);
        var minus = new UnaryMinus(new UnaryMinus(new UnaryMinus(n1)));

        assertEquals(-4d, minus.getValue());
    }

    @Test
    public void testUnaryPlus()
    {
        // +4
        var n1 = new NumberExpression(4);
        var plus = new UnaryPlus(n1);

        assertEquals(+4d, plus.getValue());
    }

    @Test
    public void testMultipleUnaryPluses()
    {
        // +++4 = +4
        var n1 = new NumberExpression(4);
        var plus = new UnaryPlus(new UnaryPlus(new UnaryPlus(n1)));

        assertEquals(+4d, plus.getValue());
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

        assertEquals(4d, plus.getValue());
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

        assertEquals(-4d, plus.getValue());
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

        assertEquals(9d, p.getValue());
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

        assertEquals(-9d, unaryMinus.getValue());
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

        assertEquals(9d, unaryPlus.getValue());
    }
}
