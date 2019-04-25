package hr.fer.zemris.java.hw06.shell;

import java.util.List;

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
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of this command.
	 * 
	 * @return the name of this command
	 */
	String getCommandName();
	
	/**
	 * Returns the description of this command.
	 * 
	 * @return the description of this command
	 */
	List<String> getCommandDescription();
	
}
