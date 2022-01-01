package se.skorup.API.expression_evalutator.lexer;

import se.skorup.API.expression_evalutator.expression.Expression;

/**
 * A syntax token.
 * */
public class SyntaxToken implements Expression
{
    private final SyntaxKind kind;
    private final int pos;
    private final String text;
    private final double value;

    public SyntaxToken(SyntaxKind kind, int pos, String text, double value)
    {
        this.kind = kind;
        this.pos = pos;
        this.text = text;
        this.value = value;
    }

    /**
     * Getter for the kind.
     *
     * @return the Kind of the Token.
     * */
    public SyntaxKind getKind()
    {
        return kind;
    }

    /**
     * Getter for the pos.
     *
     * @return the position of the token in the String.
     * */
    public int getPos()
    {
        return pos;
    }

    /**
     * Getter for the text of the token.
     *
     * @return the text of the token.
     * */
    public String getText()
    {
        return text;
    }

    @Override
    public double getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof SyntaxToken s &&
               value == s.value           &&
               text.equals(s.text)        &&
               pos == s.pos               &&
               kind.equals(s.kind);
    }

    @Override
    public int hashCode()
    {
        return (int) (value * pos + text.hashCode() + kind.getPrecedence());
    }

    @Override
    public String toString()
    {
        return "SyntaxToken [Kind: %s, Pos: %d, Text: %s, Value: %f]".formatted(kind, pos, text, value);
    }
}
