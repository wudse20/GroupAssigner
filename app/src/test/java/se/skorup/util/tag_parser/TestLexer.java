package se.skorup.util.tag_parser;

import org.junit.jupiter.api.Test;
import se.skorup.util.collections.ImmutableArray;
import se.skorup.util.tag_parser.exceptions.LexException;
import se.skorup.util.tag_parser.tokens.DataToken;
import se.skorup.util.tag_parser.tokens.TagToken;
import se.skorup.util.tag_parser.tokens.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestLexer
{
    @Test
    public void testNoTags()
    {
        var lexer = new Lexer("Hello World");
        var lexResult = lexer.lex();
        assertDoesNotThrow(lexer::lex);
        assertEquals(1, lexResult.size());
        assertEquals(
            new TagToken("white", true, new ArrayList<>(Collections.singletonList(new DataToken("Hello World")))),
            lexResult.get(0)
        );
    }

    @Test
    public void testNoTags2()
    {
        assertThrows(IllegalArgumentException.class, () -> new Lexer(""));
    }

    @Test
    public void testWrongInput()
    {
        var l = new Lexer("</");
        assertThrows(LexException.class, l::lex);
    }

    @Test
    public void testOneTag()
    {
        var l = new Lexer("<blue>Hello World</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexResult.size());
        assertEquals(
            new TagToken("blue", true, new ArrayList<>(Collections.singletonList(new DataToken("Hello World")))),
            lexResult.get(0)
        );
    }

    @Test
    public void testBlankTagNonBlankTag()
    {
        var l = new Lexer("Hello <blue>World</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var arr = new Token[] {
            new TagToken(
                "white", true,
                new ArrayList<>(Collections.singletonList(new DataToken("Hello ")))
            ),
            new TagToken(
                "blue", true,
                new ArrayList<>(Collections.singletonList(new DataToken("World")))
            )
        };

        assertEquals(new ImmutableArray<>(arr), lexResult);
    }

    @Test
    public void testNonBlankTagBlankTag()
    {
        var l = new Lexer("<blue>Hello </blue>World");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var arr = new Token[] {
            new TagToken(
                "blue", true,
                new ArrayList<>(Collections.singletonList(new DataToken("Hello ")))
            ),
            new TagToken(
                "white", true,
                new ArrayList<>(Collections.singletonList(new DataToken("World")))
            )
        };

        assertEquals(new ImmutableArray<>(arr), lexResult);
    }

    @Test
    public void testSingleNestedTags()
    {
        var l = new Lexer("<blue>He<green>ll</green>o!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexResult.size());

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("ll")))
        );

        var blueList = new ArrayList<>(Arrays.asList(new DataToken("He"), greenTag, new DataToken("o!")));
        var blueTag = new TagToken("blue", true, blueList);

        assertEquals(new ImmutableArray<>(blueTag), lexResult);
    }

    @Test
    public void testBlankTagSingleNestedTags()
    {
        var l = new Lexer("Hello <blue>Wo<green>rl</green>d!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("rl")))
        );

        var blueList = new ArrayList<>(Arrays.asList(new DataToken("Wo"), greenTag, new DataToken("d!")));

        var blueTag = new TagToken("blue", true, blueList);

        var blankTag = new TagToken(
            "white", true,
            new ArrayList<>(Collections.singletonList(new DataToken("Hello ")))
        );

        assertEquals(new ImmutableArray<>(blankTag, blueTag), lexResult);
    }

    @Test
    public void testSingleNestedTagsBlankTag()
    {
        var l = new Lexer("<blue>Wo<green>rl</green>d!</blue> Hello");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("rl")))
        );

        var blueList = new ArrayList<>(Arrays.asList(new DataToken("Wo"), greenTag, new DataToken("d!")));

        var blueTag = new TagToken("blue", true, blueList);

        var blankTag = new TagToken(
            "white", true,
            new ArrayList<>(Collections.singletonList(new DataToken(" Hello")))
        );

        assertEquals(new ImmutableArray<>(blueTag, blankTag), lexResult);
    }

    @Test
    public void testMultiNestedTags()
    {
        var l = new Lexer("<blue>Hi<red>! </red><green>World</green>!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexResult.size());

        var redTag = new TagToken(
            "red", true,
            new ArrayList<>(Collections.singletonList(new DataToken("! ")))
        );

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("World")))
        );

        var dataToken1 = new DataToken("Hi");
        var dataToken2 = new DataToken("!");

        var blueList = new ArrayList<>(Arrays.asList(dataToken1, redTag, greenTag, dataToken2));
        assertEquals(new ImmutableArray<>(new TagToken("blue", true, blueList)), lexResult);
    }

    @Test
    public void testMultiNestedTags2()
    {
        var l = new Lexer("<blue>Hi<red>! </red> Hello <green>World</green>!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexResult.size());

        var redTag = new TagToken(
            "red", true,
            new ArrayList<>(Collections.singletonList(new DataToken("! ")))
        );

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("World")))
        );

        var dataToken1 = new DataToken("Hi");
        var dataToken2 = new DataToken(" Hello ");
        var dataToken3 = new DataToken("!");

        var blueList = new ArrayList<>(Arrays.asList(dataToken1, redTag, dataToken2, greenTag, dataToken3));
        assertEquals(new ImmutableArray<>(new TagToken("blue", true, blueList)), lexResult);
    }

    @Test
    public void testBlankTagMultiNestedTags()
    {
        var l = new Lexer("Hello, <blue>Hi<red>! </red> Hello <green>World</green>!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var redTag = new TagToken(
            "red", true,
            new ArrayList<>(Collections.singletonList(new DataToken("! ")))
        );

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("World")))
        );

        var dataToken1 = new DataToken("Hi");
        var dataToken2 = new DataToken(" Hello ");
        var dataToken3 = new DataToken("!");
        var dataTokenFront = new DataToken("Hello, ");

        var blueList = new ArrayList<>(Arrays.asList(dataToken1, redTag, dataToken2, greenTag, dataToken3));

        var tokens = new Token[] {
            new TagToken("white", true, new ArrayList<>(Collections.singletonList(dataTokenFront))),
            new TagToken("blue", true, blueList)
        };

        assertEquals(new ImmutableArray<>(tokens), lexResult);
    }

    @Test
    public void testMultiNestedTagsBlankTag()
    {
        var l = new Lexer("<blue>Hi<red>! </red> Hello <green>World</green>!</blue>, Hello");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(2, lexResult.size());

        var redTag = new TagToken(
            "red", true,
            new ArrayList<>(Collections.singletonList(new DataToken("! ")))
        );

        var greenTag = new TagToken(
            "green", true,
            new ArrayList<>(Collections.singletonList(new DataToken("World")))
        );

        var dataToken1 = new DataToken("Hi");
        var dataToken2 = new DataToken(" Hello ");
        var dataToken3 = new DataToken("!");
        var dataTokenFront = new DataToken(", Hello");

        var blueList = new ArrayList<>(Arrays.asList(dataToken1, redTag, dataToken2, greenTag, dataToken3));

        var tokens = new Token[] {
            new TagToken("blue", true, blueList),
            new TagToken("white", true, new ArrayList<>(Collections.singletonList(dataTokenFront)))
        };

        assertEquals(new ImmutableArray<>(tokens), lexResult);
    }

    @Test
    public void testMultiLayeredNesting()
    {
        var l = new Lexer("<blue>Hi, <green>World <red>you </red>are </green>cool!</blue>");
        var lexResult = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexResult.size());

        var redTag = new TagToken(
            "red", true, new ArrayList<>(Collections.singletonList(new DataToken("you ")))
        );

        var greenList = new ArrayList<Token>();
        greenList.add(new DataToken("World "));
        greenList.add(redTag);
        greenList.add(new DataToken("are "));

        var greenTag = new TagToken("green", true, greenList);

        var blueList = new ArrayList<Token>();
        blueList.add(new DataToken("Hi, "));
        blueList.add(greenTag);
        blueList.add(new DataToken("cool!"));

        var blueTag = new TagToken("blue", true, blueList);
        assertEquals(blueTag, lexResult.get(0));
    }

    @Test
    public void testMultipleKindsOfTagCombos()
    {
        var l = new Lexer(
            "Johan Falk<purple>Kurt Wallander</purple>" +
            "<blue>Hi, <green>World <red>you </red>are </green>cool!</blue>"
        );
        var lexRes = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(3, lexRes.size());

        // Johan Falk
        var blankTag = new TagToken(
            "white", true, new ArrayList<>(Collections.singletonList(new DataToken("Johan Falk")))
        );

        // Kurt Wallander
        var purpleTag = new TagToken(
            "purple", true, new ArrayList<>(Collections.singletonList(new DataToken("Kurt Wallander")))
        );

        // Big multi nesting
        var redTag = new TagToken(
            "red", true, new ArrayList<>(Collections.singletonList(new DataToken("you ")))
        );

        var greenList = new ArrayList<Token>();
        greenList.add(new DataToken("World "));
        greenList.add(redTag);
        greenList.add(new DataToken("are "));

        var greenTag = new TagToken("green", true, greenList);

        var blueList = new ArrayList<Token>();
        blueList.add(new DataToken("Hi, "));
        blueList.add(greenTag);
        blueList.add(new DataToken("cool!"));

        var blueTag = new TagToken("blue", true, blueList);

        var constructedResult = new ImmutableArray<>(blankTag, purpleTag, blueTag);
        assertEquals(constructedResult, lexRes);
    }

    @Test
    public void testTripleLayeredNesting()
    {
        var l = new Lexer("<a>H<b>e<c>l<d>l</d></c></b>o</a>");
        var lexRes = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexRes.size());

        var dTag = new TagToken("d", true, new ArrayList<>(Collections.singletonList(new DataToken("l"))));

        var cList = new ArrayList<Token>();
        cList.add(new DataToken("l"));
        cList.add(dTag);
        var cTag = new TagToken("c", true, cList);

        var bList = new ArrayList<Token>();
        bList.add(new DataToken("e"));
        bList.add(cTag);
        var bTag = new TagToken("b", true, bList);

        var aList = new ArrayList<Token>();
        aList.add(new DataToken("H"));
        aList.add(bTag);
        aList.add(new DataToken("o"));
        var aTag = new TagToken("a", true, aList);

        assertEquals(new ImmutableArray<Token>(aTag), lexRes);
    }

    @Test
    public void testQuadLayeredNesting()
    {
        var l = new Lexer("<a>H<b>e<c>l<d>l<e>l</e></d></c></b>o</a>");
        var lexRes = l.lex();
        assertDoesNotThrow(l::lex);
        assertEquals(1, lexRes.size());

        var eTag = new TagToken("e", true, new ArrayList<>(Collections.singletonList(new DataToken("l"))));

        var dList = new ArrayList<Token>();
        dList.add(new DataToken("l"));
        dList.add(eTag);
        var dTag = new TagToken("d", true, dList);

        var cList = new ArrayList<Token>();
        cList.add(new DataToken("l"));
        cList.add(dTag);
        var cTag = new TagToken("c", true, cList);

        var bList = new ArrayList<Token>();
        bList.add(new DataToken("e"));
        bList.add(cTag);
        var bTag = new TagToken("b", true, bList);

        var aList = new ArrayList<Token>();
        aList.add(new DataToken("H"));
        aList.add(bTag);
        aList.add(new DataToken("o"));
        var aTag = new TagToken("a", true, aList);

        assertEquals(new ImmutableArray<Token>(aTag), lexRes);
    }
}
