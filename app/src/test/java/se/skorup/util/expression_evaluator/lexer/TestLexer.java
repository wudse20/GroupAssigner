package se.skorup.util.expression_evaluator.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLexer
{
    @Test
    public void testLexingNumber()
    {
        var inp = "555.55";
        var t = new SyntaxToken(SyntaxKind.NumberToken, 0, "555.55", 555.55);

        var l = testManyTokens(inp, t);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingFalseNumber()
    {
        var inp = "555a.55";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "555", 555);
        var t2 = new SyntaxToken(SyntaxKind.IdentifierToken, 3, "a", 0);
        var t3 = new SyntaxToken(SyntaxKind.BadToken, 4, ".", 0);
        var t4 = new SyntaxToken(SyntaxKind.IntegerToken, 5, "55", 55);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.nextToken(), t4);
        testErrorSize(l, 1);
    }

    @Test
    public void testLexingWithWhitespacesInteger()
    {
        var inp = "555   ";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "555", 555);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, "   ", 0);

        var l = testManyTokens(inp, t1, t2);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingWithWhitespacesDouble()
    {
        var inp = "555.0   ";
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "555.0", 555.0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 5, "   ", 0);

        var l = testManyTokens(inp, t1, t2);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingAddition()
    {
        var inp = "5+4";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.PlusToken, 1, "+", 0);
        var t3 = new SyntaxToken(SyntaxKind.IntegerToken, 2, "4", 4);

        var l = testManyTokens(inp, t1, t2, t3);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingSubtraction()
    {
        var inp = "5-4";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.MinusToken, 1, "-", 0);
        var t3 = new SyntaxToken(SyntaxKind.IntegerToken, 2, "4", 4);

        var l = testManyTokens(inp, t1, t2, t3);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingMultiplication()
    {
        var inp = "5*4";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.AstrixToken, 1, "*", 0);
        var t3 = new SyntaxToken(SyntaxKind.IntegerToken, 2, "4", 4);

        var l = testManyTokens(inp, t1, t2, t3);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingDivision()
    {
        var inp = "5/4";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.SlashToken, 1, "/", 0);
        var t3 = new SyntaxToken(SyntaxKind.IntegerToken, 2, "4", 4);

        var l = testManyTokens(inp, t1, t2, t3);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingParenthesizedExpression()
    {
        var inp = "(5*4)";
        var t1 = new SyntaxToken(SyntaxKind.OpenParenthesisToken, 0, "(", 0);
        var t2 = new SyntaxToken(SyntaxKind.IntegerToken, 1, "5", 5);
        var t3 = new SyntaxToken(SyntaxKind.AstrixToken, 2, "*", 0);
        var t4 = new SyntaxToken(SyntaxKind.IntegerToken, 3, "4", 4);
        var t5 = new SyntaxToken(SyntaxKind.CloseParenthesisToken, 4, ")", 0);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingVariable()
    {
        var inp = "kaka";
        var t1 = new SyntaxToken(SyntaxKind.IdentifierToken, 0, "kaka", 0);

        var l = testManyTokens(inp, t1);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingVariableInExpression()
    {
        var inp = "kaka + 7";
        var t1 = new SyntaxToken(SyntaxKind.IdentifierToken, 0, "kaka", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 4, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.PlusToken, 5, "+", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 6, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.IntegerToken, 7, "7", 7);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingConstantDeclaration()
    {
        var inp = "let x = 5.0";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IdentifierToken, 4, "x", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 5, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.EqualsToken, 6, "=", 0);
        var t6 = new SyntaxToken(SyntaxKind.WhitespaceToken, 7, " ", 0);
        var t7 = new SyntaxToken(SyntaxKind.NumberToken, 8, "5.0", 5.0);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexingConstantDeclaration2()
    {
        var inp = "let x = 5.0 * 8";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IdentifierToken, 4, "x", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 5, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.EqualsToken, 6, "=", 0);
        var t6 = new SyntaxToken(SyntaxKind.WhitespaceToken, 7, " ", 0);
        var t7 = new SyntaxToken(SyntaxKind.NumberToken, 8, "5.0", 5.0);
        var t8 = new SyntaxToken(SyntaxKind.WhitespaceToken, 11, " ", 0);
        var t9 = new SyntaxToken(SyntaxKind.AstrixToken, 12, "*", 0);
        var t10 = new SyntaxToken(SyntaxKind.WhitespaceToken, 13, " ", 0);
        var t11 = new SyntaxToken(SyntaxKind.IntegerToken, 14, "8", 8);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexModulo()
    {
        var inp = "5 % 2";
        var t1 = new SyntaxToken(SyntaxKind.IntegerToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 1, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.PercentToken, 2, "%", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.IntegerToken, 4, "2", 2);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexAlphaNumericConstantName()
    {
        var inp = "let kaka2 = 51";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IdentifierToken, 4, "kaka2", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 9, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.EqualsToken, 10, "=", 0);
        var t6 = new SyntaxToken(SyntaxKind.WhitespaceToken, 11, " ", 0);
        var t7 = new SyntaxToken(SyntaxKind.IntegerToken, 12, "51", 51);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexAlphaNumericConstantNameError()
    {
        var inp = "let 2kaka = 51";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IntegerToken, 4, "2", 2);
        var t4 = new SyntaxToken(SyntaxKind.IdentifierToken, 5, "kaka", 0);
        var t5 = new SyntaxToken(SyntaxKind.WhitespaceToken, 9, " ", 0);
        var t6 = new SyntaxToken(SyntaxKind.EqualsToken, 10, "=", 0);
        var t7 = new SyntaxToken(SyntaxKind.WhitespaceToken, 11, " ", 0);
        var t8 = new SyntaxToken(SyntaxKind.IntegerToken, 12, "51", 51);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7, t8);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexDoubleAstrixToken()
    {
        var inp = "5.0 ** 2.0";
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5.0", 5d);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.DoubleAstrixToken, 4, "**", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 6, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.NumberToken, 7, "2.0", 2d);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexCaratToken()
    {
        var inp = "5.0 ^ 2.0";
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5.0", 5d);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.DoubleAstrixToken, 4, "^", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 5, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.NumberToken, 6, "2.0", 2d);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexDoubleAstrixTokenWithLet()
    {
        var inp = "let kaka = 5 ** 2.0";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IdentifierToken, 4, "kaka", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 8, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.EqualsToken, 9, "=", 0);
        var t6 = new SyntaxToken(SyntaxKind.WhitespaceToken, 10, " ", 0);
        var t7 = new SyntaxToken(SyntaxKind.IntegerToken, 11, "5", 5);
        var t8 = new SyntaxToken(SyntaxKind.WhitespaceToken, 12, " ", 0);
        var t9 = new SyntaxToken(SyntaxKind.DoubleAstrixToken, 13, "**", 0);
        var t10 = new SyntaxToken(SyntaxKind.WhitespaceToken, 15, " ", 0);
        var t11 = new SyntaxToken(SyntaxKind.NumberToken, 16, "2.0", 2.0);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
        testErrorSize(l, 0);
    }

    @Test
    public void testLexCaratWithLet()
    {
        var inp = "let kaka = 5 ^ 2.0";
        var t1 = new SyntaxToken(SyntaxKind.LetToken, 0, "let", 0);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, " ", 0);
        var t3 = new SyntaxToken(SyntaxKind.IdentifierToken, 4, "kaka", 0);
        var t4 = new SyntaxToken(SyntaxKind.WhitespaceToken, 8, " ", 0);
        var t5 = new SyntaxToken(SyntaxKind.EqualsToken, 9, "=", 0);
        var t6 = new SyntaxToken(SyntaxKind.WhitespaceToken, 10, " ", 0);
        var t7 = new SyntaxToken(SyntaxKind.IntegerToken, 11, "5", 5);
        var t8 = new SyntaxToken(SyntaxKind.WhitespaceToken, 12, " ", 0);
        var t9 = new SyntaxToken(SyntaxKind.DoubleAstrixToken, 13, "^", 0);
        var t10 = new SyntaxToken(SyntaxKind.WhitespaceToken, 14, " ", 0);
        var t11 = new SyntaxToken(SyntaxKind.NumberToken, 15, "2.0", 2.0);

        var l = testManyTokens(inp, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
        testErrorSize(l, 0);
    }

    /**
     * Tests the number of errors.
     *
     * @param l the lexer used.
     * @param expected the expected amount of errors.
     * */
    private void testErrorSize(Lexer l, int expected)
    {
        assertEquals(l.getDiagnostics().size(), expected);
    }

    /**
     * Tests many syntax tokens.
     *
     * @param inp the input to the lexer.
     * @param tokens the tokens that are imported.
     * @return the lexer used in the test.
     * */
    private Lexer testManyTokens(String inp, SyntaxToken... tokens)
    {
        var l = new Lexer(inp);

        for (var t : tokens)
        {
            assertEquals(t, l.nextToken());
        }

        testEOF(l.nextToken());

        return l;
    }

    /**
     * Tests EOF.
     *
     * @param t the token that should be an EOF token.
     * */
    private void testEOF(SyntaxToken t)
    {
        assertEquals(t.kind(), SyntaxKind.EOF);
    }
}
