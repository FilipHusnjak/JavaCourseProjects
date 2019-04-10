package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface. Its execute method
 * moves the turtle by {@link #step} * unitLength from the current position in the direction the turtle is looking at 
 * both specified in the given Context. The current state in the given {@code Context} object is updated accordingly.
 * 
 * @author Filip Husnjak
 */
public class SkipCommand implements Command {

	/**
	 * The number of unit lengths the turtle should be moved in execute method
	 */
	private final double step;

	/**
	 * Constructs {@code DrawCommand} object with specified step.
	 * 
	 * @param step
	 *        the number of unit lengths the turtle should be moved in execute method
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	/**
	 * {@inheritDoc}
	 * Moves the turtle by {@link #step} * currentState.unitLength from the current position 
	 * in the direction the turtle is looking at both specified in the given Context. 
	 * The current state in the given {@code Context} object is updated accordingly.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 * @throws EmptyStackException if the given {@code Context} is empty
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState currState = Objects.requireNonNull(ctx, "Given context cannot be null!").getCurrentState();
		currState.position.translate(currState.direction.scaled(step));
	}
	
}
