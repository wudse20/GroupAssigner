package se.skorup.gui.components.input;

import se.skorup.gui.components.input.TextField;
import se.skorup.util.expression_evaluator.Environment;
import se.skorup.util.expression_evaluator.expression.Expression;
import se.skorup.util.expression_evaluator.parser.Parser;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MathTextField extends TextField implements FocusListener, Environment
{
    private Expression expr;

    /**
     * Creates a new TextField.
     *
     * @param columns the number of columns in the textfield.
     * */
    public MathTextField(int columns)
    {
        super(columns);
        this.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        if (expr != null)
        {
            this.setText(expr.toString());
        }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        var parser = new Parser(this.getText());
        var expr = parser.parse();

        if (parser.getDiagnostics().isEmpty())
        {
            this.setText(Integer.toString((int) expr.getValue(this)));
            this.expr = expr;
        }
    }

    @Override
    public Number getValue(String key)
    {
        return 1;
    }

    @Override
    public void registerValue(String key, Number value) {}

    @Override
    public String getText()
    {
        if (expr != null)
            return Integer.toString((int) expr.getValue(this));

        return super.getText();
    }
}
