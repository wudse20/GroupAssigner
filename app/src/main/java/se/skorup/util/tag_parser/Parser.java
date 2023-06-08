package se.skorup.util.tag_parser;

import se.skorup.util.collections.ImmutableArray;
import se.skorup.util.tag_parser.exceptions.ParseException;
import se.skorup.util.tag_parser.tokens.DataToken;
import se.skorup.util.tag_parser.tokens.TagToken;
import se.skorup.util.tag_parser.tokens.Token;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The parser that parses the tokens into text segments.
 *
 * @param tokens the lexed tokens.
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
                            dt.data(), colorMap().getOrDefault(mainColor, Color.WHITE)
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

     /**
      * The map with all colors of tags.
      * The key is the name of the color.
      *
      * @return the map with all the colors.
      * */
    public static Map<String, Color> colorMap()
    {
        var _colorMap = new HashMap<String, Color>();

        _colorMap.put("LIGHT_GREEN", new Color(194, 255, 190));
        _colorMap.put("GREEN", Color.GREEN);
        _colorMap.put("DARK_GREEN", Color.GREEN.darker());
        _colorMap.put("WHITE", Color.WHITE);
        _colorMap.put("BLUE", Color.BLUE);
        _colorMap.put("DARK_BLUE", Color.BLUE.darker());
        _colorMap.put("LIGHT_BLUE", new Color(0, 187, 255));
        _colorMap.put("IDEA_PURPLE", new Color(208, 86, 154));
        _colorMap.put("PURPLE", new Color(117, 50, 168));
        _colorMap.put("LIGHT_PURPLE", new Color(161, 79, 224));
        _colorMap.put("YELLOW", Color.YELLOW);
        _colorMap.put("LIGHT_RED", new Color(245, 37, 85));
        _colorMap.put("RED", Color.RED);
        _colorMap.put("DARK_RED", Color.RED.darker());

        return _colorMap;
    }
}
