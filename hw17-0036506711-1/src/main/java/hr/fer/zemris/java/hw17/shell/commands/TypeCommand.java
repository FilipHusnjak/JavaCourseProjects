package hr.fer.zemris.java.hw17.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw17.shell.Environment;
import hr.fer.zemris.java.hw17.shell.ShellCommand;
import hr.fer.zemris.java.hw17.shell.ShellDatabase;
import hr.fer.zemris.java.hw17.shell.ShellStatus;

public class TypeCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		List<String> args = Util.getArgs(arguments);
		if (args.size() != 1) {
			env.writeln("Type command expects 1 argument!");
			return ShellStatus.CONTINUE;
		}
		try {
			Path doc = ShellDatabase.getEntryAt(Integer.parseInt(args.get(0)));
			if (doc == null) {
				env.writeln("Query is not executed yet!");
				return ShellStatus.CONTINUE;
			}
			String data = Files.readString(doc);
			env.writeln("----------------------------------------------------------------");
			env.writeln("Dokument: " + doc.toString());
			env.writeln("----------------------------------------------------------------");
			env.writeln(data);
			env.writeln("----------------------------------------------------------------");
		} catch (RuntimeException e) {
			env.writeln("Type command expects 1 positive integer as an "
					+ "argument that should be within the range of the result!");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "type";
	}

}
