package hr.fer.zemris.java.hw06.shell;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.*;

/**
 * Represents simple shell with some useful commands:
 * <ul>
 * <li> charsets - lists possible charsets
 * <li> ls - lists directories and files in the specified directory
 * <li> cat - outputs the given file
 * <li> copy - copies the given file into specified folder
 * <li> hexdump - dumps the given file in special format
 * <li> tree - lists all files and directories recursively
 * <li> symbol - changes or outputs symbol for specified action
 * <li> mkdir - makes the specified directory and all needed parent directories
 * <li> exit - exits this shell
 * <li> help - writes list of commands or more information about specified command
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class MyShell implements Environment {
	
	/**
	 * Message to be written when program starts
	 */
	private static final String GREETING_MESSAGE = "Welcome to MyShell v 1.0";
	
	/**
	 * Default symbol for multiple lines
	 */
	private static final Character DEFAULT_MULTILINES_SYMBOL = '|';
	
	/**
	 * Default symbol for prompt
	 */
	private static final Character DEFAULT_PROMPT_SYMBOL = '>';
	
	/**
	 * Default symbol for more lines
	 */
	private static final Character DEFAULT_MORELINES_SYMBOL = '\\';
	
	/**
	 * Scanner object used to read data from InputStream
	 */
	private final Scanner sc;
	
	/**
	 * Maps the String names of each command with its right implementation
	 */
	private SortedMap<String, ShellCommand> commands = new TreeMap<>();
	
	/**
	 * Symbol for multiple lines
	 */
	private Character multiLinesSymbol;
	
	/**
	 * Symbol for prompt
	 */
	private Character promptSymbol;
	
	/**
	 * Symbol for more lines
	 */
	private Character moreLinesSymbol;
	
	/**
	 * Starts the program and creates new environment based on this shell.
	 * Command "help" lists all possible commands which can be used. For more
	 * information about each command write "help commandName". This main method
	 * uses {@code System.in} as {@code InputStream} for getting commands from the user.
	 * 
	 * @param args
	 *        program parameters
	 */
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			Environment env = new MyShell(sc);
			env.writeln(GREETING_MESSAGE);
			ShellStatus status = null;
			do {
				String line = env.readLine();
				String[] parts = line.strip().split("\\s+");
				if (parts.length < 1) {
					env.writeln("No command given!");
					continue;
				}
				ShellCommand command = env.commands().get(parts[0]);
				if (command == null) {
					env.writeln("Invalid command!: " + parts[0]);
					continue;
				}
				status = command.executeCommand(env, parts.length < 2 ? "" : String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
			} while(status != ShellStatus.TERMINATE);
			env.writeln("Goodbye!");
		} catch (ShellIOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Constructs instance of this MyShell object and sets the proper parameters.
	 * 
	 * @param sc
	 *        Scanner object used to read data from InputStream
	 * @throws NullPointerException if the given Scanner object is {@code null}
	 */
	public MyShell(Scanner sc) {
		this.sc = Objects.requireNonNull(sc, "Given Scanner object cannot be null!");
		this.multiLinesSymbol = DEFAULT_MULTILINES_SYMBOL;
		this.promptSymbol = DEFAULT_PROMPT_SYMBOL;
		this.moreLinesSymbol = DEFAULT_MORELINES_SYMBOL;
		commands = buildMap();
	}
	
	/**
	 * Builds the map of commands and returns the instance of unmodifiable
	 * sorted map.
	 * 
	 * @return the instance of unmodifiable sorted map filled with commands of this shell
	 */
	private SortedMap<String, ShellCommand> buildMap() {
		commands.put("ls", new LsShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("hexdump", new HexDumpShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("help", new HelpShellCommand());
		return Collections.checkedSortedMap(commands, String.class, ShellCommand.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readLine() throws ShellIOException {
		System.out.print(promptSymbol + " ");
		String line, result = "";
		try {
			while ((line = sc.nextLine()).strip().endsWith(moreLinesSymbol.toString())) {
				System.out.print(multiLinesSymbol + " ");
				result += line.substring(0, line.lastIndexOf(moreLinesSymbol)) + " ";
			}
		} catch (NoSuchElementException e) {
			throw new ShellIOException("Line was no found!");
		} catch (IllegalStateException e) {
			throw new ShellIOException("Input stream is closed!");
		}
		return result += line;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getMultilineSymbol() {
		return multiLinesSymbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		multiLinesSymbol = Objects.requireNonNull(symbol, "Given symbol cannot be null!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = Objects.requireNonNull(symbol, "Given symbol cannot be null!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		moreLinesSymbol = Objects.requireNonNull(symbol, "Given symbol cannot be null!");
	}
	
}
