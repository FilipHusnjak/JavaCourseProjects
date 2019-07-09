package hr.fer.zemris.java.hw17.jvdraw.listeners;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.model.IColorProvider;

/**
 * Listener which listens to each color change.
 * 
 * @author Filip Husnjak
 */
public interface ColorChangeListener {
	
	/**
	 * This method is fired whenever proper IColorProvider changes its current
	 * color.
	 * 
	 * @param source
	 *        which {@link IColorProvider} changed its color
	 * @param oldColor
	 *        old color
	 * @param newColor
	 *        new color
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
	
}
