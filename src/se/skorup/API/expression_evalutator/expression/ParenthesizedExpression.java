package se.skorup.API.expression_evalutator.expression;

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

    @Override
    public String toString()
    {
        return "(%s)".formatted(expr);
    }
}
