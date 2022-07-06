package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.Type;

/**
 * Represents a ParenthesizedExpression.
 *
 * @param expr the expression to be parenthesized.
 * */
public record ParenthesizedExpression(Expression expr) implements Expression
{
    @Override
    public Number getValue(Environment e)
    {
        return expr.getValue(e);
    }

    @Override
    public Type getType(Environment e)
    {
        return expr.getType(e);
    }

    @Override
    public String toString()
    {
        return "(%s)".formatted(expr);
    }
}
