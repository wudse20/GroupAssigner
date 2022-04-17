package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * The expression used to define a constant.
 * */
public class DefinitionExpression implements Expression
{
    private final String identifier;
    private final Expression value;

    /**
     * Creates a new DefinitionExpression.
     *
     * @param identifier the identifier of the new constant.
     * @param value the value of expression.
     * */
    public DefinitionExpression(String identifier, Expression value)
    {
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public double getValue(Environment e)
    {
        var val = value.getValue(e);
        e.registerValue(identifier, val);
        return val;
    }

    @Override
    public String toString()
    {
        return "let %s = %s".formatted(identifier, value);
    }
}