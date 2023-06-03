package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.Type;

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
     *
     * @param value the value of the expression.
     * @param t the type of the expression
     * */
    protected abstract Number unary(Number value, Type t);

    @Override
    public Number getValue(Environment e)
    {
        return unary(expr.getValue(e), getType(e));
    }

    @Override
    public Type getType(Environment e)
    {
        return expr.getType(e);
    }

    @Override
    public String toString()
    {
        return "%c%s".formatted(sign, expr);
    }
}