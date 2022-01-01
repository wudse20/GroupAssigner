package se.skorup.API.tag_parser;

import se.skorup.API.immutable_collections.ImmutableArray;
import se.skorup.API.tag_parser.exceptions.ParseException;
import se.skorup.API.tag_parser.tokens.DataToken;
import se.skorup.API.tag_parser.tokens.TagToken;
import se.skorup.API.tag_parser.tokens.Token;
import se.skorup.API.util.Utils;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The parser that parses the tokens into text segments.
 *
 * @param tokens the lexed tokens.
 *
 * */
public record Parser(ImmutableArray<TagToken> tokens)
{
    /**
     * Parsers this instances tokens into tex segments.
     *
     * @return an ImmutableArray with text segments.
     * */
    public ImmutableArray<TextSegment> parse()
    {
        var res = new ArrayList<TextSegment>();

        for (var t : tokens)
            parseToken(t, res);

        return ImmutableArray.fromList(res);
    }

    /**
     * Parses a token into text segments.
     *
     * @param t      the token to be parsed.
     * @param result the list that the result form the parsing of the token is stored.
     * @throws ParseException if the parser gets something unexpected as input.
     * */
    private void parseToken(Token t, ArrayList<TextSegment> result) throws ParseException
    {
        if (t instanceof TagToken token)
        {
            var mainColor = token.getTag().toUpperCase();
            var data = token.getData();

            for (var current : data)
            {
                if (current instanceof TagToken tag)
                {
                    parseToken(tag, result);
                }
                else if (current instanceof DataToken dt)
                {
                    result.add(new TextSegment(
                        dt.data(), Utils.colorMap().getOrDefault(mainColor, Color.WHITE)
                    ));
                }
                else
                {
                    throw new ParseException("Unexpected token: <%s>".formatted(t));
                }
            }
        }
        else
        {
            throw new ParseException("Unexpected token: <%s>".formatted(t));
        }
    }
}
