package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.FilterResult;
import hr.fer.zemris.java.hw06.shell.util.LexerException;
import hr.fer.zemris.java.hw06.shell.util.NameBuilder;
import hr.fer.zemris.java.hw06.shell.util.NameBuilderParser;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Implementation of interface {@code ShellCommand}.<br>
 * Expects at least 4 arguments.<br>
 * Does several actions based on third argument.<br>
 * <ul>
 * <li> "filter" - Filters the given directory and writes filenames that match the given regular expression.
 *                 Expects 4 arguments. First represents the directory to be checked. Second should be directory
 *                 into which the renamed file is to be placed and 4th argument is regular expression used to 
 *                 filter files based on their names.
 * <li> "groups" - Writes files that match the regular expression and each group that is captured.
 *                 Expects 4 arguments. First represents the directory to be checked. Second should be directory
 *                 into which the renamed file is to be placed and 4th argument is regular expression used to 
 *                 filter files based on their names.
 * <li> "show" - Writes each file that matches the given regular expression and the result file name
 *               that will be created if the command "execute" is called. Expects 5 arguments. 
 *               First represents the directory to be checked. Second should be directory
 *               into which the renamed file is to be placed and 4th argument is regular expression used to 
 *               filter files based on their names. 5th argument represents rules used to rename file.
 * <li> "execute" - Renames all files whose name matches the given regular expression, using the given rules.
 *                  Expects 5 arguments. First represents the directory to be checked. Second should be directory
 *                  into which the renamed file is to be placed and 4th argument is regular expression used to 
 *                  filter files based on their names. 5th argument represents rules used to rename file.       
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class MassrenameShellCommand implements ShellCommand {

	/**
	 * Command name.
	 */
	private static final String NAME = "massrename";

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
		if (list.size() < 4) {
			env.writeln("massrename command expects at least 4 arguments!");
			return ShellStatus.CONTINUE;
		}
		Path dir1 = env.getCurrentDirectory().resolve(list.get(0));
		Path dir2 = env.getCurrentDirectory().resolve(list.get(1));
		if (!Files.isDirectory(dir1) || !Files.isDirectory(dir2)) {
			env.writeln("Given directory does not exist!");
			return ShellStatus.CONTINUE;
		}
		String cmd = list.get(2);
		if ((cmd.equals("filter") || cmd.equals("groups")) && list.size() > 4
				|| (cmd.equals("show") || cmd.equals("execute")) && list.size() != 5) {
			env.writeln("Wrong number of arguments for specified command!");
			return ShellStatus.CONTINUE;
		}
		try {
			switch (cmd) {
			case "filter":
				filter(dir1, list.get(3)).stream().map(FilterResult::toString).forEach(env::writeln);
				break;
			case "groups":
				filter(dir1, list.get(3)).forEach((r) -> {
					env.write(r.toString() + " ");
					for (int i = 0; i <= r.numberOfGroups(); ++i) {
						env.write(i + ": " + r.group(i) + " ");
					}
					env.writeln("");
				});
				break;
			case "show":
				renameOrShow(filter(dir1, list.get(3)), 
						new NameBuilderParser(list.get(4)).getNameBuilder(),
						(original, newName) -> env.writeln(original + " => " + newName));
				break;
			case "execute":
				renameOrShow(filter(dir1, list.get(3)), 
						new NameBuilderParser(list.get(4)).getNameBuilder(), 
						(original, newName) -> {
							Path file1 = dir1.resolve(original);
							Path file2 = Paths.get(dir2.toString(), newName);
							try {
								Files.move(file1, file2);
							} catch (IOException e) {
								throw new IllegalArgumentException("Error while renaming the files!");
							}
						});
				break;
			default:
				env.writeln("Given command does not exist!");
			}
		} catch (PatternSyntaxException e) {
			env.writeln("Given regular expression cannot be parsed!");
		} catch (IOException e) {
			env.writeln("There was an error reading the directory!");
		} catch (NumberFormatException e) {
			env.writeln("Number expected but not given!");
		} catch (IndexOutOfBoundsException e) {
			env.writeln("Given group number does not exist!");
		} catch (IllegalArgumentException | LexerException e) {
			env.writeln(e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Generates new name based on name builder and performs an action
	 * specified by BiConsumer object for each file in files list.
	 * 
	 * @param files
	 *        list of files to be renamed
	 * @param builder
	 *        name builder used for generating new name
	 * @param action
	 *        action to be performed upon original name and new generated name
	 */
	private void renameOrShow(List<FilterResult> files, NameBuilder builder, 
			BiConsumer<String, String> action) {
		for (FilterResult file : files) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			action.accept(file.toString(), sb.toString());
		}
	}

	/**
	 * Returns List containing all files whose names match the given pattern
	 * wrapped with {@code FilterResult} object that contains more information 
	 * about grouping.
	 * 
	 * @param dir
	 *        directory whose files are to be filtered
	 * @param patternText
	 *        regular expression used for matching file names
	 * @return List containing all files whose names match the given pattern
	 *         wrapped with {@code FilterResult} object that contains more information about grouping
	 * @throws IOException if error occurred while reading the directory
	 */
	private List<FilterResult> filter(Path dir, String patternText) throws IOException {
		Pattern pattern = Pattern.compile(patternText, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		// Files.list() stream needs to be closed!
		try (Stream<FilterResult> stream = Files.list(dir).filter(Files::isRegularFile).map(Path::getFileName)
				.filter((f) -> pattern.matcher(f.toString()).matches())
				.map((f) -> new FilterResult(f, pattern.matcher(f.toString())))) {
			return stream.collect(Collectors.toList());
		}
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
				"| Usage: massrename <dir1> <dir2> <command> <filterRegex> <renameExpression>",
				"| Does several actions based on third argument.", 
				"| Expects at least 4 arguments.",
				"| If the third argument is:",
				"| - filter - Filters the given directory and writes filenames that match the given regular expression.",
				"|            Expects 4 arguments. First represents the directory to be checked. Second should be directory",
				"|            into which the renamed file is to be placed and 4th argument is regular expression used to", 
				"|            filter files based on their names.",
				"| - groups - Writes files that match the regular expression and each group that is captured.",
				"|            Expects 4 arguments. First represents the directory to be checked. Second should be directory",
				"|            into which the renamed file is to be placed and 4th argument is regular expression used to", 
				"|            filter files based on their names.",
				"| - show - Writes each file that matches the given regular expression and the result file name",
				"|          that will be created if the command \"execute\" i called. Expects 5 arguments.",
				"|          First represents the directory to be checked. Second should be directory",
				"|          into which the renamed file is to be placed and 4th argument is regular expression used to",
				"|          filter files based on their names. 5th arguments represents rules used to rename file.",
				"| - execute - Renames all files whose name match the given regular expression, using the given rules.",
				"|             Expects 5 arguments. First represents the directory to be checked. Second should be directory",
				"|             into which the renamed file is to be placed and 4th argument is regular expression used to",
				"|             filter files based on their names. 5th arguments represents rules used to rename file.\n"));
	}

}
