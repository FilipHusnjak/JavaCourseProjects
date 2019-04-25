package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Expects one or two arguments.<br>
 * If one argument was given symbol for given symbol name will be printed.<br>
 * If user provided two arguments the symbol for specified symbol name will be changed to the given new symbol.
 * 
 * @author Filip Husnjak
 *
 */
public class SymbolShellCommand implements ShellCommand {
	
	/**
	 * Command name.
	 */
	private static final String NAME = "symbol";
	
	/**
	 * Map used to map Function, that returns symbol, to its symbol name
	 */
	private static final Map<String, Function<Environment, Character>> getMap = new HashMap<>();
	
	/**
	 * Map used to map BiConsumer, that sets symbol name to the given one, to its symbol name
	 */
	private static final Map<String, BiConsumer<Environment, Character>> setMap = new HashMap<>();
	
	static {
		getMap.put("PROMPT", Environment::getPromptSymbol);
		getMap.put("MORELINES", Environment::getMorelinesSymbol);
		getMap.put("MULTILINE", Environment::getMultilineSymbol);
		
		setMap.put("PROMPT", Environment::setPromptSymbol);
		setMap.put("MORELINES", Environment::setMorelinesSymbol);
		setMap.put("MULTILINE", Environment::setMultilineSymbol);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment cannot be null!");
		Objects.requireNonNull(arguments, "Given arguments cannot be null!");
		String[] parts = arguments.strip().split("\\s+");
		if (parts.length < 1 || parts.length > 2) {
			env.writeln("Symbol command expects 1 or 2 arguments!");
			return ShellStatus.CONTINUE;
		}
		if (parts.length == 1) {
			env.writeln((getMap.get(parts[0]) == null ? "Unrecognized symbol: '" + parts[0] 
					: "Symbol for " + parts[0] + " is '" + getMap.get(parts[0]).apply(env).toString()) + "'");
		} else {
			if (parts[1].length() != 1) {
				env.writeln("Symbol has to be given as second argument!");
				return ShellStatus.CONTINUE;
			}
			env.writeln((setMap.get(parts[0]) == null ? "Unrecognized symbol: " + parts[0]
					: "Symbol for " + parts[0] + " changed from '" + getMap.get(parts[0]).apply(env).toString() + "' to '" + parts[1] + "'"));
			setMap.getOrDefault(parts[0], (e, c) -> {}).accept(env, parts[1].charAt(0));
		}	
		return ShellStatus.CONTINUE;
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
				"| Usage: symbol <symbol name> <new symbol>",
				"| Expects one or two arguments.",
				"| If one argument was given symbol for given symbol name will be printed.",
				"| If user provided two arguments the symbol for specified symbol name will be changed to the given new symbol.\n"));
	}

}
