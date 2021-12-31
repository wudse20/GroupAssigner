package se.skorup.API.expression_evalutator.expression;

/**
 * A template for a binary operator.
 * */
public abstract class BinaryOperator implements Expression
{
    private final Expression left;
    private final Expression right;

    /**
     * Creates a new BinaryOperator.
     *
     * @param left the left hand side of the operator.
     * @param right the right hand side of the operator.
     * */
    protected BinaryOperator(Expression left, Expression right)
    {
        this.left = left;
        this.right = right;
    }

    /**
     * The application of the binary expression.
     * */
    protected abstract double value(double lhs, double rhs);

    @Override
    public final double getValue()
    {
        return value(left.getValue(), right.getValue());
    }
}
