package se.skorup.main.groups.exceptions;

/**
 * The exception thrown iff the creation of Groups fail.
 * */
public class GroupCreationFailedException extends RuntimeException
{
    /**
     * Creates a new Exception, with a message.
     *
     * @param msg the message of the exception.
     * */
    public GroupCreationFailedException(String msg)
    {
        super(msg);
    }
}
