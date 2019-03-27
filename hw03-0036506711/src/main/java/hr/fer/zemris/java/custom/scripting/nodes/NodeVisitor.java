package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.hw03.SmartScriptTester;

/**
 * Represents visitor class for Visitor Design Pattern support. It provides methods
 * for each type of {@code Node}.
 * 
 * @author Filip Husnjak
 */
public class NodeVisitor {
	
	/**
	 * Returns the expected {@code String} for {@code DocumentNode}.
	 * 
	 * @param text
	 *        {@code DocumentNode} from which {@code String} should be created
	 * @return the expected {@code String} for {@code DocumentNode}
	 * @throws NullPointerException if the given {@code DocumentNode} is {@code null}
	 */
	public String visit(DocumentNode document) {
		Objects.requireNonNull(document, "Given DocumentNode cannot be null!");
		StringBuilder toReturn = new StringBuilder();
		if (document.numberOfChildren() > 0) {
			for (int i = 0, n = document.numberOfChildren(); i < n; ++i) {
				toReturn.append(SmartScriptTester.createOriginalDocumentBody(document.getChild(i)));
			}
		}
		return toReturn.toString();
	}

	/**
	 * Returns the expected {@code String} for {@code ForLoopNode}.
	 * 
	 * @param forLoop
	 *        {@code ForLoopNode} from which {@code String} should be created
	 * @return the expected {@code String} for {@code forLoopNode}
	 * @throws NullPointerException if the given {@code ForLoopNode} is {@code null}
	 */
	public String visit(ForLoopNode forLoop) {
		Objects.requireNonNull(forLoop, "Given ForLoopNode cannot be null!");
		StringBuilder toReturn = new StringBuilder().append("{$FOR " + forLoop.getVariable().toString() + " " + forLoop.getStartExpression().toString()
				+ " " + forLoop.getEndExpression().toString() + " " + (forLoop.getStepExpression() != null ? 
						forLoop.getStepExpression().toString() : "") + " $}");
		if (forLoop.numberOfChildren() > 0) {
			for (int i = 0, n = forLoop.numberOfChildren(); i < n; ++i) {
				toReturn.append(SmartScriptTester.createOriginalDocumentBody(forLoop.getChild(i)));
			}
		}
		return toReturn.append("{$END$}").toString();
	}
	
	/**
	 * Returns the expected {@code String} for {@code EchoNode}.
	 * 
	 * @param echo
	 *        {@code EchoNode} from which {@code String} should be created
	 * @return the expected {@code String} for {@code EchoNode}
	 * @throws NullPointerException if the given {@code EchoNode} is {@code null}
	 */
	public String visit(EchoNode echo) {
		Objects.requireNonNull(echo, "Given EchoNode cannot be null!");
		StringBuilder echoString = new StringBuilder().append("{$= ");
		for (Element e : echo.getElements()) {
			echoString.append(e.toString() + " ");
		}
		return echoString.append("$}").toString();
	}
	
	/**
	 * Returns the expected {@code String} for {@code TextNode}.
	 * 
	 * @param text
	 *        {@code TextNode} from which {@code String} should be created
	 * @return the expected {@code String} for {@code TextNode}
	 * @throws NullPointerException if the given {@code TextNode} is {@code null}
	 */
	public String visit(TextNode text) {
		return Objects.requireNonNull(text, "Given TextNode cannot be null!").toString();
	}
	
}
