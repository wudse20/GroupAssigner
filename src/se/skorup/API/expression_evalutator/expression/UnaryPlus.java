package se.skorup.API.expression_evalutator.expression;

public class UnaryPlus extends UnaryExpression
{
    /**
     * Creates a unary plus.
     *
     * @param expr the expression the unary expression is applied to.
     */
    public UnaryPlus(Expression expr)
    {
        super(expr);
    }

    @Override
    protected double unary(double value)
    {
        return +value;
    }
}
