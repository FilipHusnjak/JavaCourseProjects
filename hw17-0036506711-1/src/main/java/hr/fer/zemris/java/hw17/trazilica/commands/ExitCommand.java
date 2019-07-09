package hr.fer.zemris.java.hw17.trazilica.commands;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.ShellCommand;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Implementation of {@link ShellCommand} which exits the console.
 * 
 * @author Filip Husnjak
 */
public class ExitCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		List<String> args = Util.getArgs(arguments);
		if (args.size() != 0) {
			env.writeln("Exit command takes no arguments!");
			return ShellStatus.CONTINUE;
		}
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return "exit";
	}

}
