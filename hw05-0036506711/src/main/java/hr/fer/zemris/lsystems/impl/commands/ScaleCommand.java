package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface. Its execute method 
 * scales the unitLength in the current state, specified by given {@code Context} object,
 * by a {@link #factor}.
 * 
 * @author Filip Husnjak
 */
public class ScaleCommand implements Command {

	/**
	 * Factor the unitLength will be scaled by
	 */
	private final double factor;
	
	/**
	 * Constructs {@code ScaleCommand} object with specified factor by which the
	 * unitLength will be scaled in {@link #execute(Context, Painter)} method.
	 * 
	 * @param factor
	 *        the factor by which unitLength in the currentState will be scaled
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * {@inheritDoc}
	 * Scales the unitLength in the current state, specified by given {@code Context} object,
	 * by a {@link #factor}.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Objects.requireNonNull(ctx, "Given context cannot be null!").getCurrentState().unitLength *= factor;
	}
	
}
