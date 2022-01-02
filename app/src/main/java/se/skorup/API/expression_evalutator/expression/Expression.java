package se.skorup.API.expression_evalutator.expression;

import se.skorup.API.expression_evalutator.Environment;

/**
 * The interface representing an Expression.
 * */
public interface Expression
{
    /**
     * Gets the value of the expression.
     *
     * @param e the environment with the variables.
     * @return the value of the expression.
     * */
    double getValue(Environment e);
}
