package se.skorup.util.tag_parser.tokens;

/**
 * The token used to store data.
 *
 * @param data the data of the data token.
 * */
public record DataToken(String data) implements Token
{
    @Override
    public String toString()
    {
        return "DataToken: '%s'".formatted(data);
    }
}