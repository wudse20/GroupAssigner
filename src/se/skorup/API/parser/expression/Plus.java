package se.skorup.API.parser.expression;

public class Plus extends BinaryOperator
{
    /**
     * Creates a new Plus.
     *
     * @param left the left hand side of the operator.
     * @param right the right hand side of the operator.
     */
    public Plus(Expression left, Expression right)
    {
        super(left, right);
    }

    @Override
    protected double value(double lhs, double rhs)
    {
        return lhs + rhs;
    }
}
