package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Copies given source file into destination file.<br>
 * Expects two arguments.<br>
 * If the second argument is directory given source file will be copied into that<br>
 * directory using the original name.<br>
 * 
 * @author Filip Husnjak
 */
public class CopyShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "copy";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		List<String> list;
		try {
			list = ShellUtil.getArgs(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (list.size() != 2) {
			env.writeln("Copy command expects 2 arguments!");
			return ShellStatus.CONTINUE;
		}
		try (InputStream is = Files.newInputStream(Paths.get(list.get(0))); 
				OutputStream os = Files.newOutputStream(getDestinationPath(Paths.get(list.get(1)), Paths.get(list.get(0)), env))) {
			byte[] buffer = new byte[1024];
			while (true) {
				int n = is.read(buffer);
				if (n <= 0) break;
				os.write(buffer, 0, n);
			}
			env.writeln("File copied!");
		} catch (IOException e) {
			env.writeln("Given files cannot be opened!");
		} catch (CancellationException e) {
			env.writeln("Canceled");
		} catch (InvalidPathException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Returns destination path. If the given {@code pathTo} is directory
	 * it will create new {@code Path} object with same directory path and name
	 * of source file added at the end, otherwise it will not change original
	 * pathTo. This method also prompts user for response if the given file already exists and
	 * determines whether to overwrite it or not based on users response.
	 * 
	 * @param pathTo
	 *        {@code Path} wrapper around users second argument that represents destination file
	 * @param pathFrom
	 *        {@code Path} object used to determine file name if pathTo is directory
	 * @param env
	 *        {@code Environment} object used to prompt user and get response
	 * @return destination path into which the source file is to be copied
	 * @throws CancellationException if user does not want to overwrite existing file
	 */
	private Path getDestinationPath(Path pathTo, Path pathFrom, Environment env) {
		if (Files.isDirectory(pathTo)) {
			pathTo = Paths.get(pathTo.toString(), pathFrom.getFileName().toString());
		}
		if (Files.exists(pathTo)) {
			env.writeln("Would you like to overwrite existing file? y/n");
			String response;
			while (!(response = env.readLine()).toLowerCase().equals("y")) {
				if (response.toLowerCase().equals("n")) {
					throw new CancellationException();
				}
				env.writeln("y/n?");
			}
		}
		return pathTo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCommandName() {
		return NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(Arrays.asList(
				"| Usage: copy <source file> <destination file>",
				"| Copies given source file into destination file.",
				"| Expects two arguments.",
				"| If the second argument is directory given source file will be copied into that directory using the original name.\n"));
	}

}
