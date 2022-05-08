package se.skorup.API.expression_evalutator.lexer;

/**
 * The different kinds of tokens.
 * */
public enum SyntaxKind
{
    EOF(0, 0),
    BadToken(0, 0),
    NumberToken(0, 0),
    WhitespaceToken(0, 0),
    PlusToken(1, Integer.MAX_VALUE),
    MinusToken(1, Integer.MAX_VALUE),
    AstrixToken(2, 0),
    SlashToken(2, 0),
    PercentToken(2, 0),
    DoubleAstrixToken(2, 0),
    OpenParenthesisToken(0, 0),
    CloseParenthesisToken(0, 0),
    IdentifierToken(0, 0),
    EqualsToken(0, 0),
    LetToken(0, 0);

    private final int unaryPrecedence;
    private int binaryPrecedence;

    /**
     * Initializes the value with a precedence.
     *
     * @param binaryPrecedence the binary precedence of the token.
     * @param unaryPrecedence the unary precedence of the token.
     * */
    SyntaxKind(int binaryPrecedence, int unaryPrecedence)
    {
        this.binaryPrecedence = binaryPrecedence;
        this.unaryPrecedence = unaryPrecedence;
    }

    /**
     * Getter for: binaryPrecedence
     *
     * @return the binary precedence of the token.
     * */
    public int getBinaryPrecedence()
    {
        // The hack of a lifetime :)
        // Hack to get it to bind from right.
        if (this.equals(DoubleAstrixToken))
            return ++this.binaryPrecedence;

        return binaryPrecedence;
    }

    /**
     * Getter for: unaryPrecedence
     *
     * @return the unary precedence of the token.
     * */
    public int getUnaryPrecedence()
    {
        return unaryPrecedence;
    }
}
