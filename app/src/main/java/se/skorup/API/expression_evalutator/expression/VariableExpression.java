package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * Represents the a variable.
 *
 * @param identifier The identifier of the variable.
 * */
public record VariableExpression(String identifier) implements Expression
{
    @Override
    public double getValue(Environment e)
    {
        return e.getValue(identifier);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
