package searching.algorithms;

/**
 * Represents pair of current state and cost of the state.
 * 
 * @author Filip Husnjak
 */
public class Transition<S> {

	/**
	 * Current state
	 */
	private S state;
	
	/**
	 * Cost of the state
	 */
	private double cost;

	/**
	 * Constructs new Transition object with specified parameters.
	 * 
	 * @param state
	 *        current state
	 * @param cost
	 *        cost of the state
	 */
	public Transition(S state, double cost) {
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
	 * Returns the cost of the state.
	 * 
	 * @return the cost of the state
	 */
	public double getCost() {
		return cost;
	}
	
}
