package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Writes a directory listing (not recursive).<br>
 * Expects one argument which represents directory to be listed.<br>
 * The output consists of four columns:
 * <ul>
 * <li> indicates if current file is directory (d), readable (r), writable (w) and executable (x)
 * <li> file size in bytes
 * <li> file creation date/time
 * <li> file name
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class LsShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "ls";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		Path path;
		try {
			List<String> list = ShellUtil.getArgs(arguments);
			if (list.size() != 1) {
				env.writeln("Ls command expects 1 argument!");
				return ShellStatus.CONTINUE;
			}
			path = Paths.get(list.get(0));
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (!Files.isDirectory(path)) {
			env.writeln("Given path is not a directory!");
			return ShellStatus.CONTINUE;
		}
		try {
			Files.list(path).forEach((p) -> {
				try {
					env.writeln(String.format("%s %10d %s %s", getPermisions(p), Files.size(p), getFormattedDate(p), p.getFileName()));				
				} catch (IOException e) {
					throw new IllegalArgumentException("Cannot read file attributes for file " + p.getFileName());
				}
			});
		} catch (IOException e) {
			env.writeln("Cannot open given directory!");
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Returns formatted creation date determined from the given path.
	 * 
	 * @param p
	 *        {@code Path} object whose creation date is to be formatted
	 * @return formatted creation date determined from the given path
	 * @throws IOException if attributes of the given file cannot be read
	 */
	private String getFormattedDate(Path p) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(
				p, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
		);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		return sdf.format(new Date(fileTime.toMillis()));
	}
	
	/**
	 * Returns formatted permissions determined from the given {@code Path}. 
	 * 
	 * @param p
	 *        {@code Path} object whose permisions are to be determined
	 * @return formatted permissions determined from the given {@code Path}
	 */
	private String getPermisions(Path p) {
		return (Files.isDirectory(p) ? "d" : "-") + (Files.isReadable(p) ? "r" : "-")
				+ (Files.isWritable(p) ? "w" : "-") + (Files.isExecutable(p) ? "x" : "-");
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
				"| Usage: ls <dir>",
				"| Writes a directory listing (not recursive).",
				"| Expects one argument which represents directory to be listed.",
				"| The output consists of four columns:",
				"| - indicates if current file is directory (d), readable (r), writable (w) and executable (x)",
				"| - file size in bytes",
				"| - file creation date/time",
				"| - file name\n"));
	}
	
}
