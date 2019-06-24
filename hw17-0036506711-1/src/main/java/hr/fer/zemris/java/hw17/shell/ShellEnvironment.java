package hr.fer.zemris.java.hw17.shell;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import hr.fer.zemris.java.hw17.shell.commands.ExitCommand;
import hr.fer.zemris.java.hw17.shell.commands.QueryCommand;
import hr.fer.zemris.java.hw17.shell.commands.ResultsCommand;
import hr.fer.zemris.java.hw17.shell.commands.TypeCommand;

public class ShellEnvironment implements Environment {
	
	private Scanner sc;
	
	private String promptString;
	
	private Map<String, ShellCommand> commands = new HashMap<>();
	
	public ShellEnvironment(Scanner sc) {
		this.sc = sc;
		fillCommands();
	}

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
