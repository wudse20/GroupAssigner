package se.skorup.API.expression_evalutator.expression;

public class Minus extends BinaryOperator
{
    /**
     * Creates a new Minus.
     *
     * @param left  the left hand side of the operator.
     * @param right the right hand side of the operator.
     */
    public Minus(Expression left, Expression right)
    {
        super(left, right, '-');
    }

    @Override
    protected double value(double lhs, double rhs)
    {
        return lhs - rhs;
    }
}
