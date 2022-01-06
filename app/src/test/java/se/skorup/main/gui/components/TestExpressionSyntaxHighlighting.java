package se.skorup.main.gui.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Set;

import static se.skorup.main.gui.components.ExpressionSyntaxHighlighting.KEYWORD_COLOR;
import static se.skorup.main.gui.components.ExpressionSyntaxHighlighting.ERROR_COLOR;
import static se.skorup.main.gui.components.ExpressionSyntaxHighlighting.OPERATOR_COLOR;
import static se.skorup.main.gui.components.ExpressionSyntaxHighlighting.LITERAL_COLOR;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The tests for expression syntax highlighting.
 * */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestExpressionSyntaxHighlighting
{
    private Set<String> keywords;

    @BeforeEach
    public void setUp()
    {
        keywords = Set.of("cookie");
    }

    public SyntaxHighlightTest[] getData()
    {
        var al = new ArrayList<SyntaxHighlightTest>();

        al.add(new SyntaxHighlightTest(
            "-5", "<%s>-</%s><%s>5</%s>".formatted(
                OPERATOR_COLOR, OPERATOR_COLOR, LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
                "+5", "<%s>+</%s><%s>5</%s>".formatted(
                OPERATOR_COLOR, OPERATOR_COLOR, LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "5 +  5", "<%s>5</%s> <%s>+</%s>  <%s>5</%s>".formatted(
                LITERAL_COLOR, LITERAL_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "55 * 5", "<%s>55</%s> <%s>*</%s> <%s>5</%s>".formatted(
                LITERAL_COLOR, LITERAL_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "78 - 5", "<%s>78</%s> <%s>-</%s> <%s>5</%s>".formatted(
                LITERAL_COLOR, LITERAL_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "587 / 5", "<%s>587</%s> <%s>/</%s> <%s>5</%s>".formatted(
                LITERAL_COLOR, LITERAL_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "55 & 5", "<%s>55</%s> <%s>&</%s> <%s>5</%s>".formatted(
                LITERAL_COLOR, LITERAL_COLOR, ERROR_COLOR, ERROR_COLOR,
                LITERAL_COLOR, LITERAL_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "cookie + kaka", "<%s>cookie</%s> <%s>+</%s> <%s>kaka</%s>".formatted(
                KEYWORD_COLOR, KEYWORD_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                ERROR_COLOR, ERROR_COLOR
        )));

        al.add(new SyntaxHighlightTest(
            "(cookie - 1) + 8",
                "<%s>(</%s><%s>cookie</%s> <%s>-</%s> <%s>1</%s><%s>)</%s> <%s>+</%s> <%s>8</%s>".formatted(
                    LITERAL_COLOR, LITERAL_COLOR, KEYWORD_COLOR, KEYWORD_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                    LITERAL_COLOR, LITERAL_COLOR, LITERAL_COLOR, LITERAL_COLOR, OPERATOR_COLOR, OPERATOR_COLOR,
                    LITERAL_COLOR, LITERAL_COLOR
        )));

        var arr = new SyntaxHighlightTest[al.size()];
        al.toArray(arr);
        return arr;
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testSyntaxHighlighting(SyntaxHighlightTest t)
    {
        var h = new ExpressionSyntaxHighlighting(keywords);
        var res = h.syntaxHighlight(t.input);
        assertEquals(t.expected, res);
    }

    private record SyntaxHighlightTest(String input, String expected) {}
}
