package se.skorup.util.expression_evaluator;

/**
 * The environment used to get the value
 * of a key.
 * */
public interface Environment
{
    /**
     * Gets the value of a given key.
     *
     * @param key the key of the value
     * @return the value of the key.
     */
    Number getValue(String key);

    /**
     * Adds a value to a given key.
     *
     * @param key the key of the constant.
     * @param value the value of the constant.
     * */
    void registerValue(String key, Number value);
}
