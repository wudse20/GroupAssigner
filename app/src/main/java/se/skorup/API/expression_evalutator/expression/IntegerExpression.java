package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.Type;

/**
 * Represents an integer, it's called IntegerExpression
 * to remove the collision.
 *
 * @param value the value of the integer.
 * */
public record IntegerExpression(int value) implements Expression
{
    @Override
    public Number getValue(Environment e)
    {
        return value;
    }

    @Override
    public Type getType(Environment e)
    {
        return Type.INTEGER;
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }

}
