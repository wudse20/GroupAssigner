package se.skorup.API.expression_evalutator.expression;

/**
 * Represents a number, it's called NumberExpression
 * to remove the collision.
 * */
public class NumberExpression implements Expression
{
    private final double value;

    /**
     * Creates a new Number with a value.
     *
     * @param value the value of the number.
     * */
    public NumberExpression(double value)
    {
        this.value = value;
    }

    @Override
    public double getValue()
    {
        return value;
    }
}
