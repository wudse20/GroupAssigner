package se.skorup.API.tag_parser.tokens;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Creates a new tag token.
 *  */
public final class TagToken implements Token {
    /** The tag name. */
    private final String tag;

    /** If the tag is closed or not. */
    private boolean closed;

    /** The data of the tag. */
    private final ArrayList<Token> data;

    /**
     * Creates a new token
     *
     * @param tag the tag name
     * @param closed if the tag is closed or not.
     * @param data the tags in the token.
     */
    public TagToken(String tag, boolean closed, ArrayList<Token> data) {
        this.tag = tag;
        this.closed = closed;
        this.data = data;
    }

    /**
     * Getter for: tag
     *
     * @return the value of the tag.
     * */
    public String getTag()
    {
        return tag;
    }

    /**
     * Getter for: closed
     *
     * @return if the tag is closed or not.
     * */
    public boolean isClosed()
    {
        return closed;
    }

    /**
     * Getter for: data
     *
     * @return the data of the tag.
     * */
    public ArrayList<Token> getData()
    {
        return data;
    }

    /**
     * Setter for: closed
     *
     * @param isClosed if the tag should be closed or not.
     * */
    public void setClosed(boolean isClosed)
    {
        this.closed = isClosed;
    }

    @Override
    public String toString()
    {
        return "TagToken: %s, closed: %s\ndata: %s\n".formatted(tag, closed, data.toString());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null || obj.getClass() != this.getClass())
            return false;

        var that = (TagToken) obj;
        return Objects.equals(this.tag, that.tag) &&
                this.closed == that.closed &&
                Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tag, closed, data);
    }
}
