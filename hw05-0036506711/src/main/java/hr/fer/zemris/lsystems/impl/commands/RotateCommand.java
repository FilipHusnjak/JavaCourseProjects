package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface. Its execute method 
 * rotates the turtle by {@link #angle}.
 * 
 * @author Filip Husnjak
 */
public class RotateCommand implements Command {

	/**
	 * Angle in radians by which the turtle will be rotated in execute method
	 */
	private final double angle;
	
	/**
	 * Constructs {@code RotateCommand} object with specified angle. Angle should be
	 * in radians and if positive the turtle will be rotated counterclockwise.
	 * 
	 * @param angle
	 *        angle by which the turtle will be rotated in {@link #execute(Context, Painter)} 
	 *        method
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	/**
	 * {@inheritDoc}
	 * Rotates the turtle by {@link #angle}. The current state is specified with
	 * given {@code Context} object and after this method returns it will be
	 * updated accordingly.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Objects.requireNonNull(ctx, "Given context cannot be null!").getCurrentState().direction.rotate(angle);
	}
	
}
