package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * A template for a binary operator.
 * */
public abstract class BinaryOperator implements Expression
{
    private final Expression left;
    private final Expression right;

    private final char sign;

    /**
     * Creates a new BinaryOperator.
     *
     * @param left the left hand side of the operator.
     * @param right the right hand side of the operator.
     * @param sign the sign of the expression.
     * */
    protected BinaryOperator(Expression left, Expression right, char sign)
    {
        this.left = left;
        this.right = right;
        this.sign = sign;
    }

    /**
     * The application of the binary expression.
     * */
    protected abstract double value(double lhs, double rhs);

    @Override
    public final double getValue(Environment e)
    {
        return value(left.getValue(e), right.getValue(e));
    }

    @Override
    public String toString()
    {
        return "%s %c %s".formatted(left, sign, right);
    }
}
