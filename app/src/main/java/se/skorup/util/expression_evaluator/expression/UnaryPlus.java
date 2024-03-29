package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Type;

/**
 * The unary plus operator.
 * */
public class UnaryPlus extends UnaryExpression
{
    /**
     * Creates a unary plus.
     *
     * @param expr the expression the unary expression is applied to.
     */
    public UnaryPlus(Expression expr)
    {
        super(expr, '+');
    }

    @Override
    protected Number unary(Number value, Type t)
    {
        if (t.equals(Type.DOUBLE))
            return +value.doubleValue();

        return +value.intValue();
    }
}
