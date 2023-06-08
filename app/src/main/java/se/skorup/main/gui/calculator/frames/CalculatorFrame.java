package se.skorup.main.gui.calculator.frames;

import se.skorup.gui.components.ComponentContainer;
import se.skorup.gui.components.Frame;
import se.skorup.main.gui.calculator.panels.IOPanel;
import se.skorup.main.gui.calculator.panels.KeypadPanel;
import se.skorup.util.expression_evaluator.Environment;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

/**
 * The frame responsible for handling the calculator.
 * */
public class CalculatorFrame extends Frame implements Environment
{
    private final Map<String, Number> variables = new HashMap<>();

    private boolean lastVarCallValid = false;
    private String lastConstantError = "";

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
        lastVarCallValid = variables.containsKey(key);

        if (!lastVarCallValid)
            lastConstantError = key;

        return variables.getOrDefault(key, 0);
    }

    @Override
    public void registerValue(String key, Number value)
    {
        variables.put(key, value);
    }
}
