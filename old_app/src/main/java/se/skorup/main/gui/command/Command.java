package se.skorup.main.gui.command;

/**
 * The interface for a command.
 * */
public interface Command
{
    /**
     * Executes the command.
     *
     * @param cmdEnv the environment used to execute the commands.
     * @return the result of the command executed.
     * */
    CommandResult execute(CommandEnvironment cmdEnv);

    /**
     * Gets the description of the command.
     *
     * @return the description of the command.
     * */
    String getDescription();
}
