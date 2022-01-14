package se.skorup.main.gui.components;

import se.skorup.API.expression_evalutator.lexer.Lexer;
import se.skorup.API.expression_evalutator.lexer.SyntaxKind;
import se.skorup.API.expression_evalutator.lexer.SyntaxToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The syntax highlighter for the expressions, according
 * to the Syntax Highlighting.
 * */
public class ExpressionSyntaxHighlighting implements SyntaxHighlighting
{
    /** The color of the literals. */
    public static final String LITERAL_COLOR = "green";

    /** The color of the binary operators. */
    public static final String OPERATOR_COLOR = "light_blue";

    /** The color of the identifiers. */
    public static final String IDENTIFIER_COLOR = "yellow";

    /** The color of the keywords. */
    public static final String KEYWORD_COLOR = "idea_purple";

    /** The color of the non-accepted input. */
    public static final String ERROR_COLOR = "red";

    private final Set<String> keywords;

    /**
     * Creates a new ExpressionSyntaxHighlighting object.
     *
     * @param keywords the keywords that should be colored.
     * */
    public ExpressionSyntaxHighlighting(Set<String> keywords)
    {
        this.keywords = keywords;
    }

    /**
     * Lexes a string and generates the SyntaxTokens.
     *
     * @param str the input string.
     * @return a List of all the SyntaxTokens in the string.
     * */
    private List<SyntaxToken> getTokens(String str)
    {
        var l = new Lexer(str);
        var t = new ArrayList<SyntaxToken>();

        SyntaxToken token;
        do
        {
            token = l.nextToken();
            t.add(token);
        } while (!token.getKind().equals(SyntaxKind.EOF));

        return t;
    }

    /**
     * SyntaxHighlights some text that aren't colored.
     *
     * @param sb the string builder used to build the result.
     * @param text the text to be syntax highlighted.
     * */
    private void syntaxHighlightNoColor(StringBuilder sb, String text)
    {
        sb.append(text);
    }

    /**
     * SyntaxHighlights some text that are colored.
     *
     * @param sb the string builder ued to build the result.
     * @param text the text to be syntax highlighted.
     * @param color the color of the text that's being syntax highlighted.
     * */
    private void syntaxHighlightColor(StringBuilder sb, String text, String color)
    {
        sb.append('<')
          .append(color)
          .append('>')
          .append(text)
          .append("</")
          .append(color)
          .append('>');
    }

   @Override
    public String syntaxHighlight(String str)
    {
        var sb = new StringBuilder();
        var tokens = getTokens(str);

        for (var i = 0; i < tokens.size(); i++)
        {
            var t = tokens.get(i);
            i = colorize(sb, tokens, i, t);
        }

        return sb.toString();
    }

    /**
     * Colorizes a string.
     *
     * @param sb the string builder used.
     * @param tokens the tokens.
     * @param i the index.
     * @param t the current token.
     * */
    private int colorize(StringBuilder sb, List<SyntaxToken> tokens, int i, SyntaxToken t)
    {
        switch (t.getKind())
        {
            case EOF:
                return i + 1;
            case WhitespaceToken:
                syntaxHighlightNoColor(sb, t.getText());
                break;
            case PlusToken:
            case MinusToken:
            case SlashToken:
            case AstrixToken:
            case EqualsToken:
                syntaxHighlightColor(sb, t.getText(), OPERATOR_COLOR);
                break;
            case NumberToken:
            case OpenParenthesisToken:
            case CloseParenthesisToken:
                syntaxHighlightColor(sb, t.getText(), LITERAL_COLOR);
                break;
            case IdentifierToken:
                if (keywords.contains(t.getText()))
                    syntaxHighlightColor(sb, t.getText(), IDENTIFIER_COLOR);
                else
                    syntaxHighlightColor(sb, t.getText(), ERROR_COLOR);
                break;
            case LetToken:
                syntaxHighlightColor(sb, t.getText(), KEYWORD_COLOR);

                while ((i + 1) < tokens.size() && !tokens.get(++i).getKind().equals(SyntaxKind.EqualsToken))
                {
                    if (tokens.get(i).getKind().equals(SyntaxKind.IdentifierToken))
                    {
                        syntaxHighlightColor(sb, tokens.get(i).getText(), IDENTIFIER_COLOR);
                        continue;
                    }

                    colorize(sb, tokens, i, tokens.get(i));
                }

                i--;
                break;
            default:
                syntaxHighlightColor(sb, t.getText(), ERROR_COLOR);
                break;
        }

        return i;
    }
}
