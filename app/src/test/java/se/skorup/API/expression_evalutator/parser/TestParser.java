package se.skorup.API.expression_evalutator.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestParser
{
    @Test
    public void testParseNumber()
    {
        testParser("5", 5d);
    }

    @Test
    public void testParseUnaryMinusWithNumber()
    {
        testParser("-5", -5d);
    }

    @Test
    public void testParseUnaryPlusWithNumber()
    {
        testParser("+5", 5d);
    }

    @Test
    public void testParseAddition()
    {
        testParser("5+5", 10d);
    }

    @Test
    public void testParseAddition2()
    {
        testParser("5+5+5", 15d);
    }

    @Test
    public void testParseAddition3()
    {
        testParser("7 + 3", 10d);
    }

    @Test
    public void testParseAdditionWithUnary()
    {
        testParser("-7 + +3 + --4", 0d);
    }

    @Test
    public void testParseSubtraction()
    {
        testParser("5 - 4", 1d);
    }

    @Test
    public void testParseSubtraction2()
    {
        testParser("5 - 4 - 1", 0d);
    }

    @Test
    public void testParseSubtractionWithUnary()
    {
        testParser("5--5", 10d);
    }

    @Test
    public void testParseSubtractionWithUnary2()
    {
        testParser("5-+5", 0d);
    }

    @Test
    public void testParseMultiplication()
    {
        testParser("5 * 5", 25d);
    }

    @Test
    public void testParseMultiplication2()
    {
        testParser("5 * 5 * 5", 125d);
    }

    @Test
    public void testParseMultiplicationWithAddition()
    {
        testParser("5 * 5 + 5", 30d);
    }

    @Test
    public void testParseMultiplicationWithAddition2()
    {
        testParser("5 + 5 * 5", 30d);
    }

    @Test
    public void testParseMultiplicationWithUnaryOperators()
    {
        testParser("-5 * -5", 25d);
    }

    @Test
    public void testParseMultiplicationWithUnaryOperatorsAndAdditionAndSubtraction()
    {
        testParser("-5 * -5 + -5 - -5", 25d);
    }

    @Test
    public void testParseDivision()
    {
        testParser("5 / 5", 1);
    }

    @Test
    public void testParseDivisionWithUnaryOperators()
    {
        testParser("-5 / 5", -1);
    }

    @Test
    public void testParseDivisionWithOtherBinaryOperations()
    {
        testParser("5 / 5 * 5 + 7 - 3", 9);
    }

    @Test
    public void testParseDivisionWithOtherBinaryOperationsAndUnaryOperators()
    {
        testParser("5 / 5 * 5 + +--7 - -3", 15);
    }

    @Test
    public void testParseParenthesizedExpression()
    {
        testParser("(5 + 5)", 10);
    }

    @Test
    public void testParseParenthesizedExpression2()
    {
        testParser("(5 + 5) * 5", 50);
    }

    @Test
    public void testParseParenthesizedExpressionWithUnary()
    {
        testParser("+(5 + 5)", 10);
    }

    @Test
    public void testParseParenthesizedExpressionWithUnary2()
    {
        testParser("-(5 + 5)", -10);
    }

    @Test
    public void testParseParenthesizedExpressionWithBinary()
    {
        testParser("(4 + 3) * (3 - 2)", 7);
    }

    @Test
    public void testParseParenthesizeExpressionWithBinaryAndUnary()
    {
        testParser("-(4 * 3) + 7 * (2 + 5)", 37);
    }

    @Test
    public void testParseParenthesizeExpressionWithBinaryAndUnary2()
    {
        testParser("(-(4 * 3) + 7 * (2 + 5) + 1) * (1 / 2) - 4", 15);
    }

    private void testParser(String expression, double expected)
    {
        var p = new Parser(expression);
        assertEquals(p.parse().getValue(), expected);
        assertEquals(p.getDiagnostics().size(), 0);
    }
}
