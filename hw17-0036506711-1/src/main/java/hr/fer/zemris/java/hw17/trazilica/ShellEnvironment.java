package hr.fer.zemris.java.hw17.trazilica;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import hr.fer.zemris.java.hw17.trazilica.commands.ExitCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.QueryCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.ResultsCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.TypeCommand;

/**
 * Implementation of {@link Environment} interface which writes output to the
 * console.
 * 
 * @author Filip Husnjak
 */
public class ShellEnvironment implements Environment {
	
	/** {@link Scanner} object used to read data from input stream */
	private Scanner sc;
	
	/** Prompt string written on each command */
	private String promptString;
	
	/** Map of commands of this environment */
	private Map<String, ShellCommand> commands = new HashMap<>();
	
	/**
	 * Constructs new {@link ShellEnvironment} with the given {@link Scanner}
	 * object.
	 * 
	 * @param sc
	 *        {@link Scanner} object used to read data from input stream
	 */
	public ShellEnvironment(Scanner sc) {
		this.sc = sc;
		fillCommands();
	}

	/**
	 * Fills commands map with proper commands.
	 */
	private void fillCommands() {
		ShellCommand query = new QueryCommand();
		commands.put(query.getCommandName(), query);
		
		ShellCommand type = new TypeCommand();
		commands.put(type.getCommandName(), type);
		
		ShellCommand results = new ResultsCommand();
		commands.put(results.getCommandName(), results);
		
		ShellCommand exit = new ExitCommand();
		commands.put(exit.getCommandName(), exit);
	}

	@Override
	public String readLine() throws ShellIOException {
		write(promptString);
		return sc.nextLine();
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	@Override
	public Map<String, ShellCommand> commands() {
		return Collections.unmodifiableMap(commands);
	}

	@Override
	public String getPromptString() {
		return promptString;
	}

	@Override
	public void setPromptString(String promptString) {
		this.promptString = promptString;
	}

}
