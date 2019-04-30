package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ShellUtil;

/**
 * Prints a tree of all subdirectories and files contained in each directory recursively.<br>
 * Expects one argument which represents name of a root directory.
 * 
 * @author Filip Husnjak
 */
public class TreeShellCommand implements ShellCommand {
	
	private static final String NAME = "tree";

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the given Environment or String object is {@code null}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path;
		try {
			List<String> list = ShellUtil.getArgs(arguments);
			if (list.size() != 1) {
				env.writeln("Tree command expects 1 argument!");
				return ShellStatus.CONTINUE;
			}
			path = env.getCurrentDirectory().resolve(list.get(0));
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		var visitor = new MyFileVisitor(env);
		try {
			Files.walkFileTree(path, visitor);
		} catch (IOException e) {
			env.writeln("Reading files failed!");
		}
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Implementation of {@code FileVisitor} used to print tree of all directories
	 * and subdirectories and their files.
	 * 
	 * @author Filip Husnjak
	 */
	private static class MyFileVisitor extends SimpleFileVisitor<Path> {
		
		/**
		 * Current level in directory hierarchy
		 */
		private int level;
		
		/**
		 * Environment used to print result tree
		 */
		private final Environment env;
		
		/**
		 * Constructs this {@code MyFileVisitor} object and sets its Environment
		 * variable to the given one
		 * 
		 * @param env
		 *        Environment object used to print tree to the console
		 */
		public MyFileVisitor(Environment env) {
			this.env = env; 
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(level * 2) + dir.getFileName());
			level++;
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(level * 2) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
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
				"| Usage: tree <dir>",
				"| Prints a tree of all subdirectories and files contained in each directory recursively.",
				"| Expects one argument which represents name of a root directory.\n"));
	}

}
