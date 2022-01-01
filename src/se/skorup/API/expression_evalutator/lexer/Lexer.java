package se.skorup.API.expression_evalutator.lexer;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.immutable_collections.ImmutableCollection;
import se.skorup.API.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Lexes a mathematical expression.
 * */
public class Lexer
{
    private final String text;
    private final List<String> diagnostics;
    private int position;

    /**
     * Creates a new Lexer.
     *
     * @param text the text to be lexed.
     * */
    public Lexer(String text)
    {
        this.text = text;
        this.diagnostics = new ArrayList<>();
        this.position = 0;
    }

    /**
     * Gets the current char. If pos is out
     * of bounds for the String the it will
     * return a null terminator.
     *
     * @return the current character if last then <i>\0</i>.
     * */
    private char current()
    {
        if (position >= text.length())
            return '\0';

        return text.charAt(position);
    }

    /**
     * Increments the position, i.e. lexes the next character.
     * */
    private void lex()
    {
        position++;
    }

    /**
     * Lexes the next token.
     *
     * @return the next token.
     * */
    public SyntaxToken nextToken()
    {
        if (position >= text.length())
            return new SyntaxToken(SyntaxKind.EOF, position, "\0", 0);

        if (Character.isDigit(current()))
        {
            var start = position;

            while(Character.isDigit(current()) || current() == '.')
                lex();

            var length = position - start;
            var t = text.substring(start, start + length);

            if (!Utils.isValidDouble(t))
            {
                diagnostics.add("The number %s isn't a valid double".formatted(t));
                return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position - 1, position), 0);
            }

            return new SyntaxToken(SyntaxKind.NumberToken, start, t, Double.parseDouble(t));
        }

        if (Character.isWhitespace(current()))
        {
            var start = position;

            while(Character.isWhitespace(current()))
                lex();

            var length = position - start;
            var t = text.substring(start, start + length);

            return new SyntaxToken(SyntaxKind.WhitespaceToken, start, t, 0);
        }

        switch (current())
        {
            // TODO: Swap position++ for something cleaner.
            case '+' -> { return new SyntaxToken(SyntaxKind.PlusToken, position++, "+", 0); }
            case '-'-> { return new SyntaxToken(SyntaxKind.MinusToken, position++, "-", 0); }
            case '*' -> { return new SyntaxToken(SyntaxKind.AstrixToken, position++, "*", 0); }
            case '/' -> { return new SyntaxToken(SyntaxKind.SlashToken, position++, "/", 0); }
            case '(' -> { return new SyntaxToken(SyntaxKind.OpenParenthesisToken, position++, "(", 0); }
            case ')' -> { return new SyntaxToken(SyntaxKind.CloseParenthesisToken, position++, ")", 0); }
        }

        diagnostics.add("ERROR: bad character input: %c".formatted(current()));
        return new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position - 1, position), 0);
    }

    public ImmutableCollection<String> getDiagnostics()
    {
        return ImmutableArray.fromList(diagnostics);
    }
}