package hr.fer.zemris.java.hw17.jvdraw.model;

import java.nio.file.Path;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.listeners.GeometricalObjectListener;

/**
 * Represents model used for drawing elements.
 * 
 * @author Filip Husnjak
 */
public interface DrawingModel extends GeometricalObjectListener {

	/**
	 * Returns size of this model. In other words how many elements this model
	 * contains.
	 * 
	 * @return size of this model
	 */
	int getSize();

	/**
	 * Returns {@link GeometricalObject} at the specified index.
	 * 
	 * @param index
	 *        index of the object to be returned
	 * @return {@link GeometricalObject} at the specified index
	 */
	GeometricalObject getObject(int index);

	/**
	 * Adds the given {@link GeometricalObject} to this draw model.
	 * 
	 * @param object
	 *        object to be added
	 */
	void add(GeometricalObject object);

	/**
	 * Removes the given object from this model.
	 * 
	 * @param object
	 *        object to be removed
	 */
	void remove(GeometricalObject object);

	/**
	 * Changes given object position incrementing it by the given offset. Object
	 * cannot be at position less than 0 or >= size.
	 * 
	 * @param object
	 *        object whose position is to be changed
	 * @param offset
	 *        offset of the new position
	 */
	void changeOrder(GeometricalObject object, int offset);

	/**
	 * Returns index of the given {@link GeometricalObject} object.
	 * 
	 * @param object
	 *        object whose index is to be returned
	 * @return index of the given {@link GeometricalObject} object
	 */
	int indexOf(GeometricalObject object);

	/**
	 * Clears this model by removing all objects.
	 */
	void clear();

	/**
	 * Clears modified flag.
	 */
	void clearModifiedFlag();

	/**
	 * Returns whether this model was modified or not.
	 * 
	 * @return whether this model was modified or not
	 */
	boolean isModified();
	
	/**
	 * Saves this model using the given path.
	 * 
	 * @param path
	 *        path of this model
	 */
	void save(Path path);
	
	/**
	 * Loads the model from the given path.
	 * 
	 * @param path
	 *        path of the model to be loaded
	 */
	void load(Path path);
	
	/**
	 * Exports the model into image using the given path.
	 * 
	 * @param path
	 *        path of the image
	 */
	void export(Path path);
		
	/**
	 * Returns current path of this model.
	 * 
	 * @return current path of this model
	 */
	Path getCurrentPath();

	/**
	 * Adds listener to this model.
	 * 
	 * @param l
	 *        listener to be added
	 */
	void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes the given listener from this model.
	 * 
	 * @param l
	 *        listener to be removed
	 */
	void removeDrawingModelListener(DrawingModelListener l);

}
