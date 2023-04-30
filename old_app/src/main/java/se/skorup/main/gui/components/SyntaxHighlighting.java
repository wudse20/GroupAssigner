package se.skorup.main.gui.components;

/**
 * The strategy interface for syntax highlighting.
 * */
@FunctionalInterface
public interface SyntaxHighlighting
{
    /**
     * Syntax highlights a string and returns it,
     * with tags inserted according to the tag parser
     * syntax.
     *
     * @param str the input string.
     * @return the syntax highlighted string.
     * */
    String syntaxHighlight(String str);
}
