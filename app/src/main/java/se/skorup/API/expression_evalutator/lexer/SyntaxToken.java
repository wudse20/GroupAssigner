package se.skorup.API.expression_evalutator.lexer;

/**
 * A syntax token.
 *
 * @param kind the kind of the SyntaxToken.
 * @param pos the position of the text of the syntax token.
 * @param text the text of the syntax token, i.e. the text that lexer created the token from.
 * @param value the value of the syntax token when evaluated.
 * */
public record SyntaxToken(SyntaxKind kind, int pos, String text, double value) {}
