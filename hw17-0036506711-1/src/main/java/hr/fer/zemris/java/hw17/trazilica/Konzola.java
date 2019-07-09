package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Simple console application which expects 1 argument representing directory which
 * should be used for searching.
 * 
 * There are 4 possible commands:
 * <ul>
 *  <li> query <list of words separated with spaces> : searches for 10 documents that
 *  are most similar to the vector representation of list of given words
 *  <li> type "n": writes the content of the n-th document to the console
 *  <li> results: writes results from the last query
 *  <li> exit: exits the console
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class Konzola {
	
	/**
	 * Directory path of the documents used in search
	 */
	private Path path;
	
	/**
	 * Environment of this shell
	 */
	private Environment env;
	
	/**
	 * Constructs new shell with specified directory path and {@link Scanner}
	 * object used for reading data from some input stream.
	 * 
	 * @param path
	 *        directory path
	 * @param sc
	 *        {@link Scanner} object used for reading data
	 */
	public Konzola(Path path, Scanner sc) {
		this.path = path;
		env = new ShellEnvironment(sc);
		env.setPromptString("Enter command > ");
	}
	
	/**
	 * Starts the shell
	 */
	public void start() {
		try {
			ShellDatabase.loadDatabase(path);
			env.writeln("Velicina rijecnika je " + ShellDatabase.getDictionarySize() + " riječi.");
			env.writeln("");
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
				status = command.executeCommand(env, parts.length < 2 ? "" : 
					String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
			} while(status != ShellStatus.TERMINATE);
		} catch (IOException e) {
			throw new ShellIOException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Program starts here. Expects one argument representing directory path which
	 * contains documents.
	 * 
	 * @param args
	 *        directory path
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of arguments provided! Directory path expected!");
			return;
		}
		Path path = Paths.get(args[0]);
		if (!Files.exists(path)) {
			System.out.println("Given path does not exist!");
			return;
		}
		try (Scanner sc = new Scanner(System.in)) {
			new Konzola(path, sc).start();
		} catch (ShellIOException e) {
			System.out.println("I/O Error ocurred!");
			return;
		}
		
	}
	
}
