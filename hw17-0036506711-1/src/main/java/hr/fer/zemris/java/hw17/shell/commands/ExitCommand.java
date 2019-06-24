package hr.fer.zemris.java.hw17.shell.commands;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.java.hw17.shell.Environment;
import hr.fer.zemris.java.hw17.shell.ShellCommand;
import hr.fer.zemris.java.hw17.shell.ShellStatus;

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
