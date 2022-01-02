package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * Represents a number, it's called NumberExpression
 * to remove the collision.
 *
 * @param value the value of the number.
 * */
public record NumberExpression(double value) implements Expression
{
    @Override
    public double getValue(Environment e)
    {
        return value;
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }
}
