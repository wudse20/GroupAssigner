package se.skorup.API.expression_evalutator.lexer;

/**
 * The different kinds of tokens.
 * */
public enum SyntaxKind
{
    EOF(-1),
    BadToken(-1),
    NumberToken(0),
    WhitespaceToken(0),
    PlusToken(1),
    MinusToken(1),
    AstrixToken(2),
    SlashToken(2),
    OpenParenthesisToken(3),
    CloseParenthesisToken(3);

    private final int precedence;

    /**
     * Initializes the value with a precedence.
     *
     * @param precedence the precedence of the token.
     * */
    SyntaxKind(int precedence)
    {
        this.precedence = precedence;
    }

    /**
     * Getter for: precedence
     *
     * @return the precedence of the token.
     * */
    public int getPrecedence()
    {
        return precedence;
    }
}
