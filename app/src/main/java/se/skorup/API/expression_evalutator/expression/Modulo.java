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
        super(left, right, "%");
    }

    @Override
    protected Number value(Number lhs, Number rhs)
    {
        if (lhs instanceof Double || rhs instanceof Double)
            return lhs.doubleValue() % rhs.doubleValue();

        return lhs.intValue() % rhs.intValue();
    }
}
