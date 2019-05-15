package hr.fer.zemris.java.gui.calc.model.buttons;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Represents button used for binary operations. It uses given operation
 * to define listener of each button.
 * 
 * @author Filip Husnjak
 */
public class BinaryOperationButton extends CalcButton {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs new {@link BinaryOperationButton} with given operation to be
	 * performed upon active operand and currently stored value in the calculator.
	 * 
	 * @param text
	 *        text description of the button
	 * @param operation
	 *        binary operation to be perfomed
	 * @param model
	 *        model used for values
	 * @throws NullPointerException if the given operation or model are {@code null}
	 */
	public BinaryOperationButton(String text, DoubleBinaryOperator operation, CalcModel model) {
		super(text, e -> {
			if (Objects.requireNonNull(model, "Given calculator model cannot be null!").isActiveOperandSet()) {
				model.setValue(model.getPendingBinaryOperation()
						.applyAsDouble(model.getActiveOperand(), model.getValue()));
				model.clearActiveOperand();
			}
			model.setActiveOperand(model.getValue());
			model.setPendingBinaryOperation(Objects.requireNonNull(operation, 
					"Given operation cannot be null!"));
			model.clear();
		});
	}

}
