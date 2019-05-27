package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class TreeWriter {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Program expects 1 argument representing filename!");
			return;
		}
		Path path = Paths.get(args[0]);
		try {
			String docBody = Files.readString(path);
			SmartScriptParser parser = new SmartScriptParser(docBody);
			WriterVisitor visitor = new WriterVisitor();
			parser.getDocumentNode().accept(visitor);
		} catch (IOException e) {
			System.out.println("Given file cannot be read!");
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Represents visitor class for Visitor Design Pattern support. It provides methods
	 * for each type of {@code Node}.
	 * 
	 * @author Filip Husnjak
	 */
	private static class WriterVisitor implements INodeVisitor {
		
		private StringBuilder file = new StringBuilder();
		
		/**
		 * Returns the expected {@code String} for {@code DocumentNode}.
		 * 
		 * @param text
		 *        {@code DocumentNode} from which {@code String} should be created
		 * @return the expected {@code String} for {@code DocumentNode}
		 * @throws NullPointerException if the given {@code DocumentNode} is {@code null}
		 */
		@Override
		public void visitDocumentNode(DocumentNode document) {
			Objects.requireNonNull(document, "Given DocumentNode cannot be null!");
			if (document.numberOfChildren() > 0) {
				for (int i = 0, n = document.numberOfChildren(); i < n; ++i) {
					document.getChild(i).accept(this);
				}
			}
			System.out.println(file.toString());
		}

		/**
		 * Returns the expected {@code String} for {@code ForLoopNode}.
		 * 
		 * @param forLoop
		 *        {@code ForLoopNode} from which {@code String} should be created
		 * @return the expected {@code String} for {@code forLoopNode}
		 * @throws NullPointerException if the given {@code ForLoopNode} is {@code null}
		 */
		@Override
		public void visitForLoopNode(ForLoopNode forLoop) {
			Objects.requireNonNull(forLoop, "Given ForLoopNode cannot be null!");
			file.append("{$FOR " + forLoop.getVariable().toString() + " " + 
					forLoop.getStartExpression().toString() + " " + 
					forLoop.getEndExpression().toString() + " " + 
					(forLoop.getStepExpression() != null ? 
							forLoop.getStepExpression().toString() : "") + " $}");
			if (forLoop.numberOfChildren() > 0) {
				for (int i = 0, n = forLoop.numberOfChildren(); i < n; ++i) {
					forLoop.getChild(i).accept(this);
				}
			}
			file.append("{$END$}").toString();
		}
		
		/**
		 * Returns the expected {@code String} for {@code EchoNode}.
		 * 
		 * @param echo
		 *        {@code EchoNode} from which {@code String} should be created
		 * @return the expected {@code String} for {@code EchoNode}
		 * @throws NullPointerException if the given {@code EchoNode} is {@code null}
		 */
		@Override
		public void visitEchoNode(EchoNode echo) {
			Objects.requireNonNull(echo, "Given EchoNode cannot be null!");
			file.append("{$= ");
			for (Element e : echo.getElements()) {
				file.append(e.toString() + " ");
			}
			file.append("$}").toString();
		}
		
		/**
		 * Returns the expected {@code String} for {@code TextNode}.
		 * 
		 * @param text
		 *        {@code TextNode} from which {@code String} should be created
		 * @return the expected {@code String} for {@code TextNode}
		 * @throws NullPointerException if the given {@code TextNode} is {@code null}
		 */
		@Override
		public void visitTextNode(TextNode text) {
			file.append(Objects.requireNonNull(text, "Given TextNode cannot be null!").toString());
		}
		
	}

}
