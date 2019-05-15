package hr.fer.zemris.java.gui.calc.model.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Button for inserting a digit. It implements {@link CalcButton} but sets its 
 * font to digits font defined in {@link ButtonProperties} class.
 * 
 * @author Filip Husnjak
 */
public class InsertDigitButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new {@link InsertDigitButton} with specified digit to be
	 * inserted and model which will accept the digit.
	 * @param digit
	 *        digit to be inserted
	 * @param model
	 *        model which will accept the digit
	 */
	public InsertDigitButton(int digit, CalcModel model) {
		super(Integer.toString(digit), e -> model.insertDigit(digit));
		setFont(getFont().deriveFont(ButtonProperties.digitsFont));
	}

}
