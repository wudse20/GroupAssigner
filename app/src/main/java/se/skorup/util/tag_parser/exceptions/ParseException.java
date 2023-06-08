package se.skorup.util.tag_parser.exceptions;

/**
 * The exception thrown when the parser goes wrong.
 * */
public class ParseException extends RuntimeException
{
    /**
     * Creates a new parse exception with a message.
     *
     * @param message the cause of the exception.s
     * */
    public ParseException(String message)
    {
        super(message);
    }
}