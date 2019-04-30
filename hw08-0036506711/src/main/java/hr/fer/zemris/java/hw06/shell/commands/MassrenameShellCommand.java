package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.FilterResult;
import hr.fer.zemris.java.hw06.shell.util.LexerException;
import hr.fer.zemris.java.hw06.shell.util.NameBuilder;
import hr.fer.zemris.java.hw06.shell.util.NameBuilderParser;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

public class MassrenameShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "copy";

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
				|| cmd.equals("show") && list.size() != 5) {
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
				NameBuilderParser parser1 = new NameBuilderParser(list.get(4));
				NameBuilder builder1 = parser1.getNameBuilder();
				filter(dir1, list.get(3)).forEach((r) -> {
					StringBuilder sb = new StringBuilder();
					builder1.execute(r, sb);
					env.writeln(sb.toString());
				});
				break;
			case "execute":
				List<FilterResult> files = filter(dir1, list.get(3));
				NameBuilderParser parser2 = new NameBuilderParser(list.get(4));
				NameBuilder builder2 = parser2.getNameBuilder();
				for (FilterResult file : files) {
					StringBuilder sb = new StringBuilder();
					builder2.execute(file, sb);
					Path file1 = dir1.resolve(file.toString());
					Path file2 = Paths.get(dir2.toString(), sb.toString());
					Files.move(file1, file2);
				}
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

	public List<FilterResult> filter(Path dir, String patternText) throws IOException {
		Pattern pattern = Pattern.compile(patternText, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		return Files.list(dir).map(Path::getFileName).filter((f) -> pattern.matcher(f.toString()).matches())
				.map((f) -> new FilterResult(f, pattern.matcher(f.toString()))).collect(Collectors.toList());
	}

	@Override
	public String getCommandName() {
		return NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return null;
	}

}
