package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * A template for a unary expression.
 * */
public abstract class UnaryExpression implements Expression
{
    private final Expression expr;
    private final char sign;

    /**
     * Creates a UnaryExpression.
     *
     * @param expr the expression the unary expression is applied to.
     * @param sign the sign of the expression.
     * */
    protected UnaryExpression(Expression expr, char sign)
    {
        this.expr = expr;
        this.sign = sign;
    }

    /**
     * The application of the unary expression.
     * */
    protected abstract double unary(double value);

    @Override
    public double getValue(Environment e)
    {
        return unary(expr.getValue(e));
    }

    @Override
    public String toString()
    {
        return "%c%s".formatted(sign, expr);
    }
}
