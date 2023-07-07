package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Type;

/**
 * The multiplication operator.
 * */
public class Multiplication extends BinaryOperator
{
    /**
     * Creates a new multiplication.
     *
     * @param left  the left-hand side of the operator.
     * @param right the right-hand side of the operator.
     */
    public Multiplication(Expression left, Expression right)
    {
        super(left, right, "*");
    }

    @Override
    protected Number value(Number lhs, Number rhs, Type t)
    {
        if (t.equals(Type.DOUBLE))
            return lhs.doubleValue() * rhs.doubleValue();

        return lhs.intValue() * rhs.intValue();
    }
}
