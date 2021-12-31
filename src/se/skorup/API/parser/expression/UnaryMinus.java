package se.skorup.API.parser.expression;

public class UnaryMinus extends UnaryExpression
{
    /**
     * Creates a UnaryMinus.
     *
     * @param expr the expression the unary expression is applied to.
     * */
    public UnaryMinus(Expression expr)
    {
        super(expr);
    }

    @Override
    protected double unary(double value)
    {
        return -value;
    }
}
