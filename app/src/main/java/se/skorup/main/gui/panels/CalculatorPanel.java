package se.skorup.main.gui.panels;

import se.skorup.API.expression_evalutator.Environment;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.command.Command;
import se.skorup.main.gui.command.CommandEnvironment;
import se.skorup.main.gui.command.CommandResult;
import se.skorup.main.gui.command.ErrorCommand;
import se.skorup.main.gui.command.HelpCommand;
import se.skorup.main.gui.command.ListCommand;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The panel that holds the calculator.
 * */
public class CalculatorPanel extends JPanel implements KeyListener, Environment, CommandEnvironment
{
    /** The keycode for the enter key. */
    private static final int ENTER = 10;

    private boolean lastVarCallValid = true;
    private String lastConstantError = "";

    private final Map<String, Double> vars;
    private final Map<String, Command> cmds;

    private final CalculatorButtonPanel cbp = new CalculatorButtonPanel();
    private final CalculatorIOPanel ciop;
    private final CalculatorConstantPanel ccp;

    /**
     * Creates a new Calculator panel.
     *
     * @param manager the group manager in use.
     * */
    public CalculatorPanel(GroupManager manager)
    {
        this.vars = new HashMap<>();
        this.cmds = new HashMap<>();
        this.ciop = new CalculatorIOPanel(vars.keySet());
        this.ccp = new CalculatorConstantPanel(vars.keySet());

        this.setProperties();
        this.addComponents();
        this.setUpConstants(manager);
        this.setUpCommands();
    }

    /**
     * Sets the properties of the frame.
     * */
    private void setProperties()
    {
        this.setLayout(new BorderLayout());
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setForeground(Utils.FOREGROUND_COLOR);

        cbp.addActionCallback(() -> ciop.setText(cbp.getData()));
    }

    /**
     * Adds the components to the frame.
     * */
    private void addComponents()
    {
        var cont = new JPanel();
        cont.setBackground(Utils.BACKGROUND_COLOR);
        cont.setLayout(new BorderLayout());

        cont.add(ccp, BorderLayout.LINE_START);
        cont.add(ciop, BorderLayout.CENTER);
        cont.add(cbp, BorderLayout.PAGE_END);

        this.add(new JLabel("    "), BorderLayout.PAGE_START);
        this.add(new JLabel("    "), BorderLayout.LINE_START);
        this.add(cont, BorderLayout.CENTER);
        this.add(new JLabel("    "), BorderLayout.LINE_END);
        this.add(new JLabel("    "), BorderLayout.PAGE_END);
    }

    /**
     * Setup for all the constants.
     *
     * @param manager the manager that's the source of
     *                the data.
     * */
    private void setUpConstants(GroupManager manager)
    {
        vars.put("members", (double) manager.getMemberCount());
        vars.put("leaders", (double) manager.getMemberCountOfRole(Person.Role.LEADER));
        vars.put("candidates", (double) manager.getMemberCountOfRole(Person.Role.CANDIDATE));
        vars.put("mgOne", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_1));
        vars.put("mgTwo", (double) manager.getMembersOfMainGroup(Person.MainGroup.MAIN_GROUP_2));

        ccp.setUpButtons(vars.keySet());
    }

    /**
     * Setup for all the commands.
     * */
    private void setUpCommands()
    {
        cmds.put("help", new HelpCommand());
        cmds.put("list", new ListCommand());
//        cmds.put("clear", new ClearCommand(output));
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
//        if (e.getKeyCode() == ENTER)
//            calculate();
//        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
//            swapInput(false);
//        else if (e.getKeyCode() == KeyEvent.VK_UP)
//            swapInput(true);
//        else
//            e.consume();
    }

    @Override
    public double getValue(String key)
    {
        lastVarCallValid = vars.containsKey(key);

        if (!lastVarCallValid)
            lastConstantError = key;

        return vars.getOrDefault(key, 0D);
    }

    @Override
    public void registerValue(String key, double value)
    {
        vars.put(key, value);
    }

    @Override
    public void registerCommand(String name, Command cmd)
    {
        cmds.put(name, cmd);
    }

    @Override
    public CommandResult executeCommand(String name)
    {
        return cmds.getOrDefault(name, new ErrorCommand()).execute(this);
    }

    @Override
    public List<String> getCommands()
    {
        return cmds.entrySet()
                   .stream()
                   .map(e ->
                       "<blue>!%s<white> => </white><green>%s</green></blue>".formatted(
                           e.getKey(), e.getValue().getDescription()
                       )
                   ).toList();
    }
}
