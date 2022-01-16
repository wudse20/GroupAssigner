package se.skorup.main.gui.command;

import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.main.gui.panels.CalculatorPanel;
import se.skorup.main.manager.GroupManager;

/**
 * The help command.
 * */
public class HelpCommand implements Command
{
    @Override
    public CommandResult execute(CommandEnvironment cmdEnv)
    {
        var res = new StringBuilder().append("<green>Kommandon:\n</green>");
        res.append(ImmutableArray.fromList(cmdEnv.getCommands()).mkString("\n"));
        res.append("\n\n<light_blue>Miniräknare:\n");
        res.append("Räkna:\n");
        res.append("<green>Skriv in ett godkänt matematiskt uttryck.\n</green>");
        res.append("Definiera/uppdatera en konstant:\n");
        res.append("<idea_purple>let <yellow> [Identifierare]</yellow> <light_blue> = </light_blue> <green>[Värde]</green>\n");
        res.append("<idea_purple>let <yellow> [Identifierare]</yellow> <light_blue> = </light_blue> <green>[Uttryck]</green>\n");
        return new CommandResult(res.toString(), true);
    }

    @Override
    public String getDescription()
    {
        return "Listar alla tillgängliga kommandon.";
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof HelpCommand;
    }

    @Override
    public int hashCode()
    {
        return getDescription().hashCode() + execute(new CalculatorPanel(new GroupManager("name"))).hashCode();
    }
}
