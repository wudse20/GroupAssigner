package se.skorup.API.expression_evalutator.expression;

/**
 * The class used to represent a power.
 * */
public class Power extends BinaryOperator
{
    /**
     * Creates a new PowerOperator.
     *
     * @param left  the left-hand side of the operator.
     * @param right the right-hand side of the operator.
     */
    public Power(Expression left, Expression right)
    {
        super(left, right, "**");
    }

    @Override
    protected double value(double lhs, double rhs)
    {
        return Math.pow(lhs, rhs);
    }
}
