package hr.fer.zemris.java.hw17.shell;

import java.io.IOException;

/**
 * Represents an interface to each command so that each command can be executed
 * properly.
 * 
 * @author Filip Husnjak
 */
public interface ShellCommand {

	/**
	 * Executes this command with specified environment and arguments. Returns
	 * {@code ShellStatus} which determines whether shell should continue or
	 * terminate.
	 * 
	 * @param env
	 *        shell environment in which this method is called
	 * @param arguments
	 *        additional arguments for execution of this command
	 * @return {@code ShellStatus} which determines whether shell should continue or
	 *         terminate
	 */
	ShellStatus executeCommand(Environment env, String arguments) throws IOException;
	
	/**
	 * Returns the name of this command.
	 * 
	 * @return the name of this command
	 */
	String getCommandName();
	
}
