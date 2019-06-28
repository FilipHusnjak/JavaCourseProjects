package hr.fer.zemris.java.hw17.model;

import java.awt.Color;

import hr.fer.zemris.java.hw17.listeners.ColorChangeListener;

public interface IColorProvider {
	
	Color getCurrentColor();
	
	void addColorChangeListener(ColorChangeListener l);
	
	void removeColorChangeListener(ColorChangeListener l);

}
