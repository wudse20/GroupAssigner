package se.skorup.main.gui.calculator.panels;

import se.skorup.gui.callbacks.ActionCallback;
import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.Panel;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The keypad for the calculator
 * */
public class KeypadPanel extends Panel implements ActionListener
{
    private final List<ActionCallback<KeypadEvent>> callbacks;

    private final Button btnZero = new Button("ui.button.zero");
    private final Button btnOne = new Button("ui.button.one");
    private final Button btnTwo = new Button("ui.button.two");
    private final Button btnThree = new Button("ui.button.three");
    private final Button btnFour = new Button("ui.button.four");
    private final Button btnFive = new Button("ui.button.five");
    private final Button btnSix = new Button("ui.button.six");
    private final Button btnSeven = new Button("ui.button.seven");
    private final Button btnEight = new Button("ui.button.eight");
    private final Button btnNine = new Button("ui.button.nine");
    private final Button btnDecimal = new Button("ui.button.decimal");
    private final Button btnAdd = new Button("ui.button.add");
    private final Button btnSub = new Button("ui.button.sub");
    private final Button btnMulti = new Button("ui.button.multi");
    private final Button btnDivide = new Button("ui.button.divide");
    private final Button btnMod = new Button("ui.button.mod");
    private final Button btnLeftParenthesis = new Button("ui.button.left.paren");
    private final Button btnRightParenthesis = new Button("ui.button.right.paren");

    /**
     * Creates a new panel.
     * */
    public KeypadPanel()
    {
        super(new GridLayout(6, 3));
        this.callbacks = new ArrayList<>();

        this.addComponents();
        this.addActionListeners();

        for (var c : this.getComponents())
            c.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
    }

    /**
     * Adds action listeners to all the buttons.
     * */
    private void addActionListeners()
    {
        for (var c : this.getComponents())
            if (c instanceof Button b)
                b.addActionListener(this);
    }

    /**
     * Adds components.
     * */
    private void addComponents()
    {
        this.add(btnSeven);
        this.add(btnEight);
        this.add(btnNine);
        this.add(btnFour);
        this.add(btnFive);
        this.add(btnSix);
        this.add(btnFive);
        this.add(btnOne);
        this.add(btnTwo);
        this.add(btnThree);
        this.add(btnDecimal);
        this.add(btnZero);
        this.add(btnAdd);
        this.add(btnSub);
        this.add(btnMulti);
        this.add(btnDivide);
        this.add(btnMod);
        this.add(btnLeftParenthesis);
        this.add(btnRightParenthesis);
    }

    /**
     * Adds a callback to the panel.
     *
     * @param callback the callback to be added.
     * */
    public void addCallback(ActionCallback<KeypadEvent> callback)
    {
        if (callback == null)
            return;

        callbacks.add(callback);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        var cmd = e.getActionCommand();
        var event = new KeypadEvent(cmd);
        callbacks.forEach(cb -> cb.action(event));
    }

    /**
     * A wrapper type for the event sent back when a key is pressed.
     *
     * @param cmd the action command of the button.
     * */
    public record KeypadEvent(String cmd) {}
}
