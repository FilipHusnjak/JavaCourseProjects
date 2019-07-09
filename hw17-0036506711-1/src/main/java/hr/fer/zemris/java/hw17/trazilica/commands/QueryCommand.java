package hr.fer.zemris.java.hw17.trazilica.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.ShellCommand;
import hr.fer.zemris.java.hw17.trazilica.ShellDatabase;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Implementation of {@link ShellCommand} which performs the query.
 * 
 * @author Filip Husnjak
 */
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
