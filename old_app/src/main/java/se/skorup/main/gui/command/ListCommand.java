package se.skorup.main.gui.command;

import se.skorup.main.gui.panels.CalculatorPanel;
import se.skorup.main.manager.GroupManager;

public class ListCommand implements Command
{
    @Override
    public CommandResult execute(CommandEnvironment cmdEnv)
    {
        var arrow = "<dark_blue> => </dark_blue>";
        var sb = new StringBuilder();

        sb.append("<dark_green>members").append(arrow)
                .append("Antalet medlämmar i denna grupp\n")
                .append("leaders").append(arrow)
                .append("Antalet ledare i denna grupp\n")
                .append("candidates").append(arrow)
                .append("Antalet deltagare i denna grupp\n")
                .append("mgOne").append(arrow)
                .append("Antalet medlämmar i huvudgrupp 1\n")
                .append("mgTwo").append(arrow)
                .append("Antalet medlämmar i huvudgrupp 2\n</dark_green>");

        return new CommandResult(sb.toString(), true);
    }

    @Override
    public String getDescription()
    {
        return "Listar alla för definierade konstanter.";
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof ListCommand;
    }

    @Override
    public int hashCode()
    {
        return getDescription().hashCode() + execute(new CalculatorPanel(new GroupManager("namn"))).hashCode();
    }
}
