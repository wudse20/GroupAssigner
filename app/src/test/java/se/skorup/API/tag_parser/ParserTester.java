package se.skorup.API.tag_parser;

import org.junit.jupiter.api.Test;
import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.API.util.Utils;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The class that tests the parser.
 * */
public class ParserTester
{
    /**
     * This tests test to parse one tag.
     * */
    @Test
    public void testOneTag()
    {
        var p = new Parser(new Lexer("HelloWorld").lex());
        var res = p.parse();
        assertDoesNotThrow(p::parse);

        var controlSegment = new TextSegment("HelloWorld", Color.WHITE);
        var controlResult = new ImmutableArray<>(controlSegment);

        assertEquals(1, res.size());
        assertEquals(controlResult, res);
    }

    /**
     * Tests parsing of multiple tags.
     * */
    @Test
    public void testNestedTags()
    {
        var p = new Parser(new Lexer("<blue>Hi, <green>World</green>!</blue>").lex());
        var res = p.parse();
        assertDoesNotThrow(p::parse);

        var controlSegments = new TextSegment[] {
            new TextSegment("Hi, ", Utils.colorMap().get("BLUE")),
            new TextSegment("World", Utils.colorMap().get("GREEN")),
            new TextSegment("!", Utils.colorMap().get("BLUE"))
        };

        var control = new ImmutableArray<>(controlSegments);
        assertEquals(3, res.size());
        assertEquals(control, res);
    }
}
