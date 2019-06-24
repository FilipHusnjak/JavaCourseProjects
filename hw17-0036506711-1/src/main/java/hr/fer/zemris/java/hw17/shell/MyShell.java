package hr.fer.zemris.java.hw17.shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class MyShell {
	
	private Path path;
	
	private Environment env;
	
	public MyShell(Path path, Scanner sc) {
		this.path = path;
		env = new ShellEnvironment(sc);
		env.setPromptString("Enter command > ");
	}
	
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
			new MyShell(path, sc).start();
		} catch (ShellIOException e) {
			System.out.println("I/O Error ocurred!");
			return;
		}
		
	}
	
}
