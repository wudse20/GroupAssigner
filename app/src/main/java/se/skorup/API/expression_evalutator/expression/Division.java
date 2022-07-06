package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.Type;

/**
 * The division operator.
 * */
public class Division extends BinaryOperator
{
    /**
     * Creates a new Division.
     *
     * @param left  the left hand side of the operator.
     * @param right the right hand side of the operator.
     */
    public Division(Expression left, Expression right)
    {
        super(left, right, "/");
    }

    @Override
    protected Number value(Number lhs, Number rhs, Type t)
    {
        return lhs.doubleValue() / rhs.doubleValue();
    }

    @Override
    public Type getType(Environment e)
    {
        return Type.DOUBLE;
    }
}
