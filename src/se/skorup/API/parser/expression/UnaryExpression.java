package se.skorup.API.parser.expression;

/**
 * A template for a unary expression.
 * */
public abstract class UnaryExpression implements Expression
{
    private final Expression expr;

    /**
     * Creates a UnaryExpression.
     *
     * @param expr the expression the unary expression is applied to.
     * */
    protected UnaryExpression(Expression expr)
    {
        this.expr = expr;
    }

    /**
     * The application of the unary expression.
     * */
    protected abstract double unary(double value);

    @Override
    public double getValue()
    {
        return unary(expr.getValue());
    }
}
