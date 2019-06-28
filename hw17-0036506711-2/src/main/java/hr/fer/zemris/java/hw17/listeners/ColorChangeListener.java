package hr.fer.zemris.java.hw17.listeners;

import java.awt.Color;

import hr.fer.zemris.java.hw17.model.IColorProvider;

public interface ColorChangeListener {
	
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
	
}
