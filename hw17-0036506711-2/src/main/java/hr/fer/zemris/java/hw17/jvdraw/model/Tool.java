package hr.fer.zemris.java.hw17.jvdraw.model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Represents abstraction of the tool which performs certain actions upon mouse
 * activity.
 * 
 * @author Filip Husnjak
 */
public interface Tool {

	/**
	 * Action to be performed when mouse was pressed.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	void mousePressed(MouseEvent e);
	
	/**
	 * Action to be performed when mouse was released.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	void mouseReleased(MouseEvent e);
	
	/**
	 * Action to be performed when mouse was clicked.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	void mouseClicked(MouseEvent e);
	
	/**
	 * Action to be performed when mouse was moved.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	void mouseMoved(MouseEvent e);
	
	/**
	 * Action to be performed when mouse was dragged.
	 * 
	 * @param e
	 *        {@link MouseEvent} object
	 */
	void mouseDragged(MouseEvent e);
	
	/**
	 * Paints the object onto the given {@link Graphics2D} object.
	 * 
	 * @param g2d
	 *        {@link Graphics2D} object used for painting
	 */
	void paint(Graphics2D g2d);
	
}
