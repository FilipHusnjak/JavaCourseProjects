package hr.fer.zemris.java.hw17.shell.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw17.shell.Environment;
import hr.fer.zemris.java.hw17.shell.ShellCommand;
import hr.fer.zemris.java.hw17.shell.ShellDatabase;
import hr.fer.zemris.java.hw17.shell.ShellStatus;

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
