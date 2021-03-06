package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.Type;

/**
 * Represents a real number, it's called NumberExpression
 * to remove the collision.
 *
 * @param value the value of the number.
 * */
public record NumberExpression(double value) implements Expression
{
    @Override
    public Number getValue(Environment e)
    {
        return value;
    }

    @Override
    public Type getType(Environment e)
    {
        return Type.DOUBLE;
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }
}
