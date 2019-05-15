package hr.fer.zemris.java.gui.calc.model.screen;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.model.Calculator;
import hr.fer.zemris.java.gui.calc.model.buttons.ButtonProperties;

/**
 * Represents screen of the {@link Calculator}.
 * 
 * @author Filip Husnjak
 */
public class ScreenLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new {@link ScreenLabel} with text to be displayed.
	 * 
	 * @param text
	 *        text to be displayed in this label
	 */
	public ScreenLabel(String text) {
		super(text);
		initGui();
	}

	/**
	 * Initializes properties of this label. Font is set to digits font
	 * defined in {@link ButtonProperties} class and background color is
	 * set to {@link Color.YELLOW}. Text is right alined.
	 */
	private void initGui() {
		setFont(getFont().deriveFont(ButtonProperties.digitsFont));
		setBackground(Color.YELLOW);
		setHorizontalAlignment(SwingConstants.RIGHT);
		setOpaque(true);
		setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2)));
	}
	
}
