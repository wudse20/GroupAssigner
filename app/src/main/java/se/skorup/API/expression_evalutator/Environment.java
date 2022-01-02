package se.skorup.API.expression_evalutator;

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
