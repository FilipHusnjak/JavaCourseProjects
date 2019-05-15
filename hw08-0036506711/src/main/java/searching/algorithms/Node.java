package searching.algorithms;

/**
 * Represents node of the search tree that has reference to the parent, 
 * state and cost of the current state.
 * 
 * @author Filip Husnjak
 */
public class Node<S> {

	/**
	 * Parent of this Node
	 */
	private Node<S> parent;
	
	/**
	 * Current state
	 */
	private S state;
	
	/**
	 * Cost of the current state
	 */
	private double cost;

	/**
	 * Constructs new Node with specified parameters.
	 * 
	 * @param parent
	 *        parent of the Node
	 * @param state
	 *        current state of a puzzle
	 * @param cost
	 *        cost of the state
	 */
	public Node(Node<S> parent, S state, double cost) {
		this.parent = parent;
		this.state = state;
		this.cost = cost;
	}
	
	/**
	 * Returns the current state.
	 * 
	 * @return the current state
	 */
	public S getState() {
		return state;
	}
	
	/**
	 * Returns the cost of the current state.
	 * 
	 * @return the cost of the current state
	 */
	public double getCost() {
		return cost;
	}
	
	/**
	 * Returns the parent of this Node.
	 * 
	 * @return the parent of this Node
	 */
	public Node<S> getParent() {
		return parent;
	}
	
}
