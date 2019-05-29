package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents a visitor used for implementing Visitor Design Pattern. It
 * provides methods for visiting all types of {@link Node} class.
 * 
 * @author Filip Husnjak
 */
public interface INodeVisitor {

	/**
	 * Visits text node and performs the proper operation upon given text node.
	 * 
	 * @param node
	 *        {@link TextNode} used to perform proper operation
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Visits for loop node and performs the proper operation upon given for loop
	 * node.
	 * 
	 * @param node
	 *        {@link ForLoopNode} used to perform proper operation
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Visits echo node and performs the proper operation upon given echo node.
	 * 
	 * @param node
	 *        {@link EchoNode} used to perform proper operation
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Visits document node and performs the proper operation upon given document 
	 * node.
	 * 
	 * @param node
	 *        {@link DocumentNode} used to perform proper operation
	 */
	public void visitDocumentNode(DocumentNode node);
	
}
