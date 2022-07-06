package se.skorup.API.expression_evalutator.expression;

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
    protected Number value(Number lhs, Number rhs)
    {
        if (lhs instanceof Double || rhs instanceof Double)
            return lhs.doubleValue() * rhs.doubleValue();

        return lhs.intValue() * rhs.intValue();
    }
}
