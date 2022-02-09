package se.skorup.API.expression_evalutator.expression;

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
        super(left, right, '%');
    }

    @Override
    protected double value(double lhs, double rhs)
    {
        return lhs % rhs;
    }
}
