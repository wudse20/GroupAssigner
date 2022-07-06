package se.skorup.API.expression_evalutator.expression;

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
    protected Number unary(Number value)
    {
        if (value instanceof Double)
            return -value.doubleValue();

        return -value.intValue();
    }
}
