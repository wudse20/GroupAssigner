package se.skorup.API.parser.expression;

/**
 * Represents a ParenthesizedExpression.
 * */
public class ParenthesizedExpression implements Expression
{
    private final Expression expr;

    /**
     * Creates a new ParenthesizedExpression
     *
     * @param expr the expression to be parenthesized.
     * */
    public ParenthesizedExpression(Expression expr)
    {
        this.expr = expr;
    }

    @Override
    public double getValue()
    {
        return expr.getValue();
    }
}
