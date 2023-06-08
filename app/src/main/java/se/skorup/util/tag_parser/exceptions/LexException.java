package se.skorup.util.tag_parser.exceptions;

/**
 * The exception thrown when the lexer fails.
 * */
public class LexException extends RuntimeException
{
    /**
     * Creates a RuntimeException, LexException.
     *
     * @param message the message of the exception.
     * */
    public LexException(String message)
    {
        super(message);
    }
}
