package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.expression_evalutator.Type;

/**
 * Represents the a variable.
 *
 * @param identifier The identifier of the variable.
 * */
public record VariableExpression(String identifier) implements Expression
{
    @Override
    public Number getValue(Environment e)
    {
        return e.getValue(identifier);
    }

    @Override
    public Type getType(Environment e)
    {
        var val = getValue(e);

        // Little hacky but better than all alternatives.
        if (val instanceof Double)
            return Type.DOUBLE;
        else
            return Type.INTEGER;
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
