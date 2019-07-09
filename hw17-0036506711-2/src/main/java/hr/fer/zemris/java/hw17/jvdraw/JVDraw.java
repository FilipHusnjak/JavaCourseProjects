package hr.fer.zemris.java.hw17.jvdraw;

import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw17.jvdraw.components.MainFrame;

/**
 * Starts the program and opens the proper window.
 * 
 * @author Filip Husnjak
 */
public class JVDraw {

	/**
	 * Program starts here and does not expect any arguments.
	 * 
	 * @param ignored
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}
	
}
