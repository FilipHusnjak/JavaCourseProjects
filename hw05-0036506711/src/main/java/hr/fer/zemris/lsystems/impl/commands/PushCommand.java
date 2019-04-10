package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface.
 * Its execute method adds the copy of the current state to the given 
 * {@code Context} object. The copy becomes current state.
 * 
 * @author Filip Husnjak
 */
public class PushCommand implements Command {

	/**
	 * {@inheritDoc}
	 * Adds the copy of the current state specified in the given {@code Context} object
	 * to the given {@code Context} object. The copy becomes current state.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Objects.requireNonNull(ctx, "Given context cannot be null!").pushState(ctx.getCurrentState().copy());
	}

}
