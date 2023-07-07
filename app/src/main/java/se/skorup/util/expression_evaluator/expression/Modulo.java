package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Type;

/**
 * The modulo operator.
 * */
public class Modulo extends BinaryOperator
{
    /**
     * Creates a new modulo operator.
     *
     * @param left  the left-hand side of the operator.
     * @param right the right-hand side of the operator.
     * */
    public Modulo(Expression left, Expression right) {
        super(left, right, "%");
    }

    @Override
    protected Number value(Number lhs, Number rhs, Type t)
    {
        if (t.equals(Type.DOUBLE))
            return lhs.doubleValue() % rhs.doubleValue();

        return lhs.intValue() % rhs.intValue();
    }
}