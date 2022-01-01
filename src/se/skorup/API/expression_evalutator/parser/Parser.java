package se.skorup.API.expression_evalutator.parser;

import se.skorup.API.expression_evalutator.expression.Division;
import se.skorup.API.expression_evalutator.expression.Expression;
import se.skorup.API.expression_evalutator.expression.Minus;
import se.skorup.API.expression_evalutator.expression.Multiplication;
import se.skorup.API.expression_evalutator.expression.NumberExpression;
import se.skorup.API.expression_evalutator.expression.ParenthesizedExpression;
import se.skorup.API.expression_evalutator.expression.Plus;
import se.skorup.API.expression_evalutator.expression.UnaryMinus;
import se.skorup.API.expression_evalutator.expression.UnaryPlus;
import se.skorup.API.expression_evalutator.lexer.Lexer;
import se.skorup.API.expression_evalutator.lexer.SyntaxKind;
import se.skorup.API.expression_evalutator.lexer.SyntaxToken;
import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.immutable_collections.ImmutableCollection;
import se.skorup.API.util.DebugMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for parsing a
 * mathematical expression.
 * */
public class Parser
{
    private final ImmutableCollection<SyntaxToken> tokens;
    private final List<String> diagnostics;
    private int position;

    /**
     * Creates a new Parser.
     *
     * @param text the text to be parsed.
     * */
    public Parser(String text)
    {
        this.diagnostics = new ArrayList<>();
        var lexer = new Lexer(text);
        var tok = new ArrayList<SyntaxToken>();

        SyntaxToken token;
        do
        {
            token = lexer.nextToken();
            tok.add(token);
        } while (!token.getKind().equals(SyntaxKind.EOF));

        var filtered =
            tok.parallelStream()
                  .filter(t -> !t.getKind().equals(SyntaxKind.BadToken))
                  .filter(t -> !t.getKind().equals(SyntaxKind.WhitespaceToken))
                  .toList();

        DebugMethods.log("Tokens: %s".formatted(filtered), DebugMethods.LogType.DEBUG);
        this.tokens = ImmutableArray.fromCollection(filtered);
        diagnostics.addAll(lexer.getDiagnostics().toList());
    }

    /**
     * Peeks in to the future of the SyntaxTokens,
     * with an offset that decides how far into
     * the future we should peek. If offset results
     * in an index that is out of bounds the last token
     * will be returned.
     *
     * @param offset How far into the future we should
     *               look.
     * @return the SyntaxToken offset times a head.
     *         If out of bounds then the last token
     *         will be returned.
     * */
    private SyntaxToken peek(int offset)
    {
        var index = position + offset;

        if (index >= tokens.size())
            return tokens.get(tokens.size() - 1);
        else
            return tokens.get(index);
    }

    /**
     * Gets the current token.
     *
     * @return the current token.
     * */
    private SyntaxToken current()
    {
        return peek(0);
    }

    /**
     * Gets the next token.
     *
     * @return the next token.
     * */
    private SyntaxToken nextToken()
    {
        var current = current();
        position++;
        return current;
    }

    /**
     * Matches a kind to the current token, if they match
     * then it will return the next token, else if will return
     * a dummy token and report an error.
     *
     * @param kind the kind to match against.
     * @return the next token if the kinds match up, else
     *         it will return a dummy token of kind: <br>
     *         {@code new SyntaxToken(kind, current().position, null, 0)}
     * */
    private SyntaxToken matchToken(SyntaxKind kind)
    {
        if (current().getKind().equals(kind))
            return nextToken();

        diagnostics.add("ERROR: Unexpected token: %s, expected %s".formatted(current().getKind(), kind));
        return new SyntaxToken(kind, current().getPos(), null, 0);
    }

    /**
     * Parses an expression.
     *
     * @param parentPrecedence the precedence of the parent.
     * @return the result of the parsing.
     * */
    private Expression parseExpression(int parentPrecedence)
    {
        Expression left;
        var unaryOperatorPrecedence = current().getKind().getUnaryPrecedence();

        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence)
        {
            var opToken = nextToken();
            var operand = parseExpression(unaryOperatorPrecedence);

            if (opToken.getText().equals("+"))
                left = new UnaryPlus(operand);
            else
                left = new UnaryMinus(operand);
        }
        else
        {
            left = parsePrimaryExpression();
        }

        while (true)
        {
            var precedence = current().getKind().getBinaryPrecedence();

            if (precedence == 0 || precedence <= parentPrecedence)
                break;

            var opToken = nextToken();
            var right = parseExpression(precedence);

            switch (opToken.getText())
            {
                case "+" -> { left = new Plus(left, right); }
                case "-" -> { left = new Minus(left, right); }
                case "*" -> { left = new Multiplication(left, right); }
                case "/" -> { left = new Division(left, right); }
            }
        }

        return left;
    }

    /**
     * Parses a primary expression.
     *
     * @return the parsed expression.
     * */
    private Expression parsePrimaryExpression()
    {
        if (current().getKind().equals(SyntaxKind.OpenParenthesisToken))
        {
            nextToken();
            var expr = parseExpression(0);
            matchToken(SyntaxKind.CloseParenthesisToken);
            return new ParenthesizedExpression(expr);
        }

        var number = matchToken(SyntaxKind.NumberToken);
        return new NumberExpression(number.getValue());
    }

    /**
     * Parses the expression and creates the AST.
     *
     * @return the result of the parsing of the expression.
     * */
    public Expression parse()
    {
        var expr = parseExpression(0);
        var eof = matchToken(SyntaxKind.EOF);
        return expr;
    }

    /**
     * Getter for: diagnostics
     *
     * @return the diagnostics of the parser.
     * */
    public ImmutableCollection<String> getDiagnostics()
    {
        return ImmutableArray.fromList(diagnostics);
    }
}
