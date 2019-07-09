package hr.fer.zemris.java.hw17.jvdraw.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.listeners.ColorChangeListener;

/**
 * Abstraction of the simple color provider which defines some useful methods.
 * 
 * @author Filip Husnjak
 */
public interface IColorProvider {
	
	/**
	 * Returns current color of this color provider.
	 * 
	 * @return current color of this color provider
	 */
	Color getCurrentColor();
	
	/**
	 * Adds the given listener.
	 * 
	 * @param l
	 *        listener to be added
	 */
	void addColorChangeListener(ColorChangeListener l);
	
	/**
	 * Removes the given listener.
	 * 
	 * @param l
	 *        listener to be removed
	 */
	void removeColorChangeListener(ColorChangeListener l);

}
