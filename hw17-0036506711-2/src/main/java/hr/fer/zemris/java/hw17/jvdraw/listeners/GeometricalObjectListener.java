package hr.fer.zemris.java.hw17.jvdraw.listeners;

import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObject;

/**
 * Listens upon changes on {@link GeometricalObject}.
 * 
 * @author Filip Husnjak
 */
public interface GeometricalObjectListener {

	/**
	 * Fired when {@link GeometricalObject} was changed.
	 * 
	 * @param o
	 *        {@link GeometricalObject} that was changed
	 */
	void geometricalObjectChanged(GeometricalObject o);

}
