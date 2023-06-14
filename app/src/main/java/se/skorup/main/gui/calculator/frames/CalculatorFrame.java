package se.skorup.main.gui.calculator.frames;

import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Frame;
import se.skorup.gui.helper.syntax_highlighting.ExpressionSyntaxHighlighting;
import se.skorup.main.gui.calculator.panels.IOPanel;
import se.skorup.main.gui.calculator.panels.KeypadPanel;
import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.parser.Parser;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The frame responsible for handling the calculator.
 * */
public class CalculatorFrame extends Frame implements Environment
{
    private final Map<String, Number> variables = new HashMap<>();

    private final List<String> variableDiagnostics = new ArrayList<>();

    private final KeypadPanel keypad = new KeypadPanel();
    private final IOPanel io = new IOPanel(variables.keySet());

    private final Container cp = this.getContentPane();

    /**
     * Creates a new Frame. With a localization key
     * that will be used for the title.
     * */
    public CalculatorFrame()
    {
        super("ui.title.calculator");
        super.init();
        keypad.addCallback(e -> io.appendInputString(e.cmd()));
        io.addEnterCallback(e -> doCalculation());
    }

    /**
     * Does the calculation.
     * */
    private void doCalculation()
    {
        var input = io.getInputText();
        var parser = new Parser(input);
        var res = parser.parse();
        var value = res.getValue(this);
        var diagnostics = variableDiagnostics;
        diagnostics.addAll(parser.getDiagnostics().toList());

        if (diagnostics.isEmpty())
        {
            var text = new ExpressionSyntaxHighlighting(variables.keySet()).syntaxHighlight(res.toString());

            io.appendOutputString(text);
            io.appendOutputString("<DARK_GREEN>%s</DARK_GREEN>%n".formatted(String.valueOf(value)));
        }
        else
        {
            var sb = new StringBuilder().append("<RED>");

            for (var d : diagnostics)
                sb.append(d).append("\n");

            sb.append("<RED>");

            io.appendOutputString(sb.toString());
        }

        io.clearInput();
        variableDiagnostics.clear();
    }

    @Override
    protected void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    protected void addComponents()
    {
        cp.add(io, BorderLayout.CENTER);
        cp.add(new ComponentContainer(keypad), BorderLayout.PAGE_END);
        this.pack();
    }

    @Override
    public Number getValue(String key)
    {
        if (!variables.containsKey(key))
            variableDiagnostics.add("Variable not found: %s".formatted(key));

        return variables.getOrDefault(key, 0);
    }

    @Override
    public void registerValue(String key, Number value)
    {
        variables.put(key, value);
    }
}
