package se.skorup.API.expression_evalutator;

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
     * */
    double getValue(String key);
}
