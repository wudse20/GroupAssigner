package se.skorup.util.expression_evaluator.expression;

import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.Type;

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
     */
    Number getValue(Environment e);

    /**
     * Gets the type of the expression.
     *
     * @return the type of the expression.
     * */
    Type getType(Environment e);
}
