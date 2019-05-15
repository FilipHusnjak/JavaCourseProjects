package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Represents calculator button. It sets its background and font defined
 * in {@link ButtonProperties} class. All other buttons that are more specialized
 * inherit this class.
 * 
 * @author Filip Husnjak
 */
public class CalcButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new {@link CalcButton} with specified listener and text
	 * to be displayed.
	 * 
	 * @param text
	 *        text to be displayed
	 * @param listener
	 *        listener to be added
	 */
	public CalcButton(String text, ActionListener listener) {
		super(text);
		initGui();
		addActionListener(listener);
	}
	
	/**
	 * Constructs new {@link CalcButton} with specified text to be displayed.
	 * 
	 * @param text
	 *        text to be displayed
	 */
	public CalcButton(String text) {
		super(text);
		initGui();
	}
	
	/**
	 * Initializes properties of this button defined in {@link ButtonProperties}
	 * class.
	 */
	private void initGui() {
		setFont(getFont().deriveFont(ButtonProperties.buttonFont));
		setBackground(ButtonProperties.btnColor);
	}
	
}
