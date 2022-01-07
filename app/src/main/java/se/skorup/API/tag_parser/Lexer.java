package se.skorup.API.tag_parser;

import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.API.tag_parser.exceptions.LexException;
import se.skorup.API.tag_parser.tokens.DataToken;
import se.skorup.API.tag_parser.tokens.TagToken;

import java.util.ArrayList;

/**
 * Lexes the input.
 *
 * @param input The input string.
 * */
public record Lexer(String input)
{
    /**
     * Creates a new lexer, with an input.
     *
     * @param input The input string.
     * @throws IllegalArgumentException if the input is empty.
     * */
    public Lexer
    {
        if (input.length() == 0)
            throw new IllegalArgumentException("Input cannot be empty");
    }

    /**
     * Lexes the string.
     *
     * @return an ImmutableArray with all the tags.
     * @throws LexException if the input cannot be lexed.
     * */
    public ImmutableArray<TagToken> lex() throws LexException
    {
        var chars = input.toCharArray();
        var result = new ArrayList<TagToken>();
        TagToken current = null;

        for (int i = 0; i < chars.length; i++)
        {
            if (current == null) // No tag is being worked on
            {
                if (chars[i] != '<') // Not a new tag.
                {
                    // Creates a token for the default color.
                    current = new TagToken("white", true, new ArrayList<>());

                    var sb = new StringBuilder().append(chars[i]);

                    while ((i + 1) < chars.length && chars[i + 1] != '<')
                        sb.append(chars[++i]);

                    // Adds all the data
                    current.getData().add(new DataToken(sb.toString()));

                    // Adds the token to the result list.
                    result.add(current);
                    current = null;
                }
                else if (chars[i] == '<' && i + 1 < chars.length && chars[i + 1] != '/') // New tag
                {
                    if (i + 1 == chars.length) // The input isn't lexable.
                        throw new LexException(String.format(
                                "Unexpected character: %c", chars[i]
                        ));

                    var sb = new StringBuilder();

                    while ((i + 1) < chars.length && chars[i + 1] != '>')
                    {
                        i++;
                        sb.append(chars[i]);
                    }

                    current = new TagToken(sb.toString(), false, new ArrayList<>());
                }
                else // Illegal input
                {
                    throw new LexException(String.format(
                            "Unexpected character: %c", chars[i]
                    ));
                }
            }
            else // A tag is being worked on.
            {
                if (chars[i] != '<') // Data is being worked on.
                {
                    var sb = new StringBuilder();

                    while ((i + 1) < chars.length && chars[i + 1] != '<') // Gets the data.
                        sb.append(chars[++i]);

                    if (sb.length() == 0) // No need in adding blank tags.
                        continue;

                    if (current.getData().stream().anyMatch(x -> x instanceof TagToken token && !token.isClosed())) // Nested tags
                    {
                        tagAdder(current, new DataToken(sb.toString()));
                    }
                    else // No nested tags to take care of
                    {
                        current.getData().add(new DataToken(sb.toString()));
                    }
                }
                else if (chars[i] == '<' && i + 1 < chars.length && chars[i + 1] != '/') // Nested tag.
                {
                    if (i + 1 == chars.length) // The input isn't lexable.
                        throw new LexException(String.format(
                                "Unexpected character: %c", chars[i]
                        ));

                    var sb = new StringBuilder();

                    while ((i + 1) < chars.length && chars[i + 1] != '>')
                        sb.append(chars[++i]);

                    if (current.getData().stream().anyMatch(x -> x instanceof TagToken token && !token.isClosed())) // Nested tags
                    {
                        nestedTagAdder(current, new TagToken(sb.toString(), false, new ArrayList<>()));
                    }
                    else // No nested tags to take care of
                    {
                        current.getData().add(new TagToken(sb.toString(), false, new ArrayList<>()));
                    }
                }
                else if (chars[i] == '<' && i + 1 < chars.length && chars[i + 1] == '/') // Closing tag
                {
                    if (++i + 1 == chars.length) // The input isn't lexable.
                        throw new LexException(String.format(
                                "Unexpected character: %c", chars[i]
                        ));

                    var sb = new StringBuilder();

                    while ((i + 1) < chars.length && chars[i + 1] != '>')
                        sb.append(chars[++i]);

                    if (current.getData().stream().anyMatch(x -> x instanceof TagToken token && !token.isClosed())) // Nested tags
                    {
                        tagCloser(current);
                    }
                    else // No nested tags to take care of
                    {
                        current.setClosed(true);
                        result.add(current);
                        current = null;
                        i++;
                    }
                }
                else // Illegal Input
                {
                    throw new LexException(String.format(
                            "Unexpected character: %c", chars[i]
                    ));
                }
            }
        }

        if (current != null)
            result.add(current);

        return ImmutableArray.fromList(result);
    }

    /**
     * Adds nested tags, correctly.
     *
     * @param first the first tag to check inserting to.
     * @param tag   the tag to be inserted.
     * */
    private void nestedTagAdder(TagToken first, TagToken tag)
    {
        if (first.getData().stream().noneMatch(x -> x instanceof TagToken t && !t.isClosed()))
        {
            first.getData().add(tag);
            return;
        }

        var data = first.getData();
        for (int j = data.size() - 1; j >= 0; j--)
        {
            if (data.get(j) instanceof TagToken t && !t.isClosed())
            {
                nestedTagAdder(t, tag);
                return;
            }
        }
    }

    /**
     * The helper method that adds tags to nested tags.
     *
     * @param tag the current tag being investigated.
     * @param dt  the data token to be added.
     * */
    private void tagAdder(TagToken tag, DataToken dt)
    {
        if (tag.getData().stream().noneMatch(x -> x instanceof TagToken t && !t.isClosed()))
        {
            tag.getData().add(dt);
            return;
        }

        var data = tag.getData();
        for (int j = data.size() - 1; j >= 0; j--) {
            if (data.get(j) instanceof TagToken t && !t.isClosed())
            {
                tagAdder(t, dt);
                return;
            }
        }
    }

    /**
     * The helper method that closes the tags.
     *
     * @param token the tag token to close the first
     *              occurrence of the the tag.
     * */
    private void tagCloser(TagToken token)
    {
        if (token.getData().stream().noneMatch(x -> x instanceof TagToken t && !t.isClosed()))
        {
            token.setClosed(true);
            return;
        }

        var data = token.getData();
        for (int j = data.size() - 1; j >= 0; j--)
        {
            if (data.get(j) instanceof TagToken t && !t.isClosed())
            {
                tagCloser(t);
                return;
            }
        }
    }
}
