package se.skorup.main.groups.exceptions;

/**
 * The exception thrown iff there is no
 * possible way to create a group.
 * */
public class NoGroupAvailableException extends Exception
{
    /**
     * Creates a new Exception, with a message.
     *
     * @param msg the message of the exception.
     * */
    public NoGroupAvailableException(String msg)
    {
        super(msg);
    }
}
