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
    PlusToken(1, 4),
    MinusToken(1, 4),
    AstrixToken(2, 0),
    SlashToken(2, 0),
    OpenParenthesisToken(0, 0),
    CloseParenthesisToken(0, 0),
    ConstantToken(0, 0);

    private final int binaryPrecedence;
    private final int unaryPrecedence;

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
