package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.Type;

/**
 * A template for a binary operator.
 * */
public abstract class BinaryOperator implements Expression
{
    private final Expression left;
    private final Expression right;

    private final String sign;

    /**
     * Creates a new BinaryOperator.
     *
     * @param left the left-hand side of the operator.
     * @param right the right-hand side of the operator.
     * @param sign the sign of the expression.
     * */
    protected BinaryOperator(Expression left, Expression right, String sign)
    {
        this.left = left;
        this.right = right;
        this.sign = sign;
    }

    /**
     * The application of the binary expression.
     *
     * @param lhs the value at the left-hand side.
     * @param rhs the value at the right-hand side.
     * @param t   the type of the expression.
     * @return the value of this expression.
     */
    protected abstract Number value(Number lhs, Number rhs, Type t);

    @Override
    public final Number getValue(Environment e)
    {
        return value(left.getValue(e), right.getValue(e), getType(e));
    }

    @Override
    public Type getType(Environment e)
    {
        if (left.getType(e).equals(Type.DOUBLE) || right.getType(e).equals(Type.DOUBLE))
            return Type.DOUBLE;

        return Type.INTEGER;
    }

    @Override
    public String toString()
    {
        return "%s %s %s".formatted(left, sign, right);
    }
}
