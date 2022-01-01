package se.skorup.API.expression_evalutator.lexer;

import org.testng.annotations.Test;
import se.skorup.API.expression_evalutator.lexer.Lexer;
import se.skorup.API.expression_evalutator.lexer.SyntaxKind;
import se.skorup.API.expression_evalutator.lexer.SyntaxToken;

import static org.testng.Assert.assertEquals;

/**
 * Tests the lexer class
 * */
public class TestLexer
{
    @Test
    public void testLexingNumber()
    {
        var inp = "555.55";
        var t = new SyntaxToken(SyntaxKind.NumberToken, 0, "555.55", 555.55);
        var l = new Lexer(inp);
        assertEquals(l.nextToken(), t);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingFalseNumber()
    {
        var inp = "555a.55";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "555", 555);
        var t2 = new SyntaxToken(SyntaxKind.BadToken, 3, "a", 0);
        var t3 = new SyntaxToken(SyntaxKind.BadToken, 4, ".", 0);
        var t4 = new SyntaxToken(SyntaxKind.NumberToken, 5, "55", 55);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.nextToken(), t4);
        assertEquals(l.getDiagnostics().size(), 2);
    }

    @Test
    public void testLexingWithWhitespaces()
    {
        var inp = "555   ";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "555", 555);
        var t2 = new SyntaxToken(SyntaxKind.WhitespaceToken, 3, "   ", 0);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingAddition()
    {
        var inp = "5+4";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.PlusToken, 1, "+", 0);
        var t3 = new SyntaxToken(SyntaxKind.NumberToken, 2, "4", 4);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingSubtraction()
    {
        var inp = "5-4";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.MinusToken, 1, "-", 0);
        var t3 = new SyntaxToken(SyntaxKind.NumberToken, 2, "4", 4);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingMultiplication()
    {
        var inp = "5*4";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.AstrixToken, 1, "*", 0);
        var t3 = new SyntaxToken(SyntaxKind.NumberToken, 2, "4", 4);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingDivision()
    {
        var inp = "5/4";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.NumberToken, 0, "5", 5);
        var t2 = new SyntaxToken(SyntaxKind.SlashToken, 1, "/", 0);
        var t3 = new SyntaxToken(SyntaxKind.NumberToken, 2, "4", 4);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.getDiagnostics().size(), 0);
    }

    @Test
    public void testLexingParenthesizedExpression()
    {
        var inp = "(5*4)";
        var l = new Lexer(inp);
        var t1 = new SyntaxToken(SyntaxKind.OpenParenthesisToken, 0, "(", 0);
        var t2 = new SyntaxToken(SyntaxKind.NumberToken, 1, "5", 5);
        var t3 = new SyntaxToken(SyntaxKind.AstrixToken, 2, "*", 0);
        var t4 = new SyntaxToken(SyntaxKind.NumberToken, 3, "4", 4);
        var t5 = new SyntaxToken(SyntaxKind.CloseParenthesisToken, 4, ")", 0);

        assertEquals(l.nextToken(), t1);
        assertEquals(l.nextToken(), t2);
        assertEquals(l.nextToken(), t3);
        assertEquals(l.nextToken(), t4);
        assertEquals(l.nextToken(), t5);
        assertEquals(l.getDiagnostics().size(), 0);
    }
}
