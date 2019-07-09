package hr.fer.zemris.java.hw17.jvdraw.listeners;

import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;

/**
 * Listens upon changes on drawing model.
 * 
 * @author Filip Husnjak
 */
public interface DrawingModelListener {

	/**
	 * Fired when object was added to the drawing model.
	 * 
	 * @param source
	 *        which drawing model is changed
	 * @param index0
	 *        index of the first added object
	 * @param index1
	 *        index of the last added object
	 */
	void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Fired when object was removed from the drawing model.
	 * 
	 * @param source
	 *        which drawing model is changed
	 * @param index0
	 *        index of the first removed object
	 * @param index1
	 *        index of the last removed object
	 */
	void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Fired when object was changed.
	 * 
	 * @param source
	 *        which drawing model is changed
	 * @param index0
	 *        index of the first changed object
	 * @param index1
	 *        index of the last changed object
	 */
	void objectsChanged(DrawingModel source, int index0, int index1);

}
