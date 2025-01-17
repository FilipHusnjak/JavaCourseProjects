package hr.fer.zemris.java.hw17.trazilica.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.ShellCommand;
import hr.fer.zemris.java.hw17.trazilica.ShellDatabase;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Implementation of {@link ShellCommand} which writes results from the last query.
 * 
 * @author Filip Husnjak
 */
public class ResultsCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		List<String> args = Util.getArgs(arguments);
		if (args.size() != 0) {
			env.writeln("Results command takes no arguments!");
			return ShellStatus.CONTINUE;
		}
		Map<Path, Double> result = ShellDatabase.getLastQuery();
		if (result == null) {
			env.writeln("Query is not executed yet!");
			return ShellStatus.CONTINUE;
		}
		int i = 0;
		for(Map.Entry<Path, Double> e: result.entrySet()) {
			env.writeln(String.format("[%d] (%.4f) %s", i++, e.getValue(), 
					e.getKey().normalize().toAbsolutePath().toString()));
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "results";
	}

}
