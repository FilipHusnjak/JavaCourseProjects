package hr.fer.zemris.lsystems.impl;

import java.util.EmptyStackException;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Represents a state context which consists of a stack of {@code TurtleState}
 * objects. The current state is at the top of stack.
 * 
 * @author Filip Husnjak
 *
 */
public class Context {

	/**
	 * Stack of states that represent context
	 */
	private ObjectStack<TurtleState> states = new ObjectStack<>();
	
	/**
	 * Returns the current {@code TurtleState} without removing it from the
	 * {@code Context}.
	 * 
	 * @return the current {@code TurtleState}
	 * @throws EmptyStackException if this {@code Context} is empty
	 */
	public TurtleState getCurrentState() {
		return states.peek();
	}
	
	/**
	 * Adds the given {@code TurtleState} object to this {@code Context}. The given
	 * {@code TurtleState} becomes current state.
	 * 
	 * @param state
	 *        {@code TurtleState} to be added to this {@code Context}
	 * @throws NullPointerException if the given state is {@code null}
	 */
	public void pushState(TurtleState state) {
		Objects.requireNonNull(state, "Given state cannot be null!");
		states.push(state);
	}
	
	/**
	 * Removes the current state from this {@code Context}.
	 * 
	 * @throws EmptyStackException if this {@code Context} is empty
	 */
	public void popState() {
		states.pop();
	}
	
}
