package hr.fer.zemris.java.hw17.shell.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import hr.fer.zemris.java.hw17.shell.Environment;
import hr.fer.zemris.java.hw17.shell.ShellCommand;
import hr.fer.zemris.java.hw17.shell.ShellDatabase;
import hr.fer.zemris.java.hw17.shell.ShellStatus;

public class QueryCommand implements ShellCommand {
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		if (arguments.strip().isBlank()) {
			env.writeln("Query command takes at least one argument!");
			return ShellStatus.CONTINUE;
		}
		String[] filtered = ShellDatabase.filterQuery(arguments);
		Map<Path, Double> result = ShellDatabase.executeQuery(filtered);
		env.writeln("Query is: " + Arrays.toString(filtered));
		env.writeln("Najboljih 10 rezultata:");
		int i = 0;
		for(Map.Entry<Path, Double> e: result.entrySet()) {
			env.writeln(String.format("[%d] (%.4f) %s", i++, e.getValue(), 
					e.getKey().normalize().toAbsolutePath().toString()));
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "query";
	}

}
