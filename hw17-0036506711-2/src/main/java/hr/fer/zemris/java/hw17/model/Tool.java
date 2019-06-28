package hr.fer.zemris.java.hw17.model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public interface Tool {

	void mousePressed(MouseEvent e);
	
	void mouseReleased(MouseEvent e);
	
	void mouseClicked(MouseEvent e);
	
	void mouseMoved(MouseEvent e);
	
	void mouseDragged(MouseEvent e);
	
	void paint(Graphics2D g2d);
	
}
