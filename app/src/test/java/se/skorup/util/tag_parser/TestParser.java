package se.skorup.util.tag_parser;

import org.junit.jupiter.api.Test;
import se.skorup.util.collections.ImmutableArray;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestParser
{
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

    @Test
    public void testNestedTags()
    {
        var p = new Parser(new Lexer("<blue>Hi, <green>World</green>!</blue>").lex());
        var res = p.parse();
        assertDoesNotThrow(p::parse);

        var controlSegments = new TextSegment[] {
                new TextSegment("Hi, ", Parser.colorMap().get("BLUE")),
                new TextSegment("World", Parser.colorMap().get("GREEN")),
                new TextSegment("!", Parser.colorMap().get("BLUE"))
        };

        var control = new ImmutableArray<>(controlSegments);
        assertEquals(3, res.size());
        assertEquals(control, res);
    }
}
