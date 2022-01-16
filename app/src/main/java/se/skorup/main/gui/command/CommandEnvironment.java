package se.skorup.main.gui.command;

import java.util.List;

/**
 * The interface for a command environment,
 * that executes the commands.
 * */
public interface CommandEnvironment
{
    /**
     * Registers a new command.
     *
     * @param name the name of the command.
     * @param cmd the command.
     * */
    void registerCommand(String name, Command cmd);

    /**
     * Executes a command.
     *
     * @param name the name of the command to be executed.
     * @return the result of the command.
     * */
    CommandResult executeCommand(String name);

    /**
     * Gets a list of all commands with a description attached.
     *
     * @return a list containing all commands.
     * */
    List<String> getCommands();
}
