package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Type;

/**
 * The unary minus operator.
 * */
public class UnaryMinus extends UnaryExpression
{
    /**
     * Creates a UnaryMinus.
     *
     * @param expr the expression the unary expression is applied to.
     * */
    public UnaryMinus(Expression expr)
    {
        super(expr, '-');
    }

    @Override
    protected Number unary(Number value, Type t)
    {
        if (t.equals(Type.DOUBLE))
            return -value.doubleValue();

        return -value.intValue();
    }
}
