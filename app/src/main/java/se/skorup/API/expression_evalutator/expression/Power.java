package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.util.Utils;

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
    protected Number value(Number lhs, Number rhs)
    {
        if (lhs instanceof Double || rhs instanceof Double)
            return Math.pow(lhs.doubleValue(), rhs.doubleValue());

        return Utils.pow(lhs.intValue(), rhs.intValue());
    }
}
