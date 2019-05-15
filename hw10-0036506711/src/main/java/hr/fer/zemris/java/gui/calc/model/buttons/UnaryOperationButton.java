package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Represents button used for unary operations. It uses given operations
 * to define listeners of each button. It has normal and inversed state.
 * 
 * @author Filip Husnjak
 */
public class UnaryOperationButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Normal operation to be performed
	 */
	private DoubleUnaryOperator normalOperation;
	
	/**
	 * Inversed operation to be performed
	 */
	private DoubleUnaryOperator inverseOperation;
	
	/**
	 * Model on which operations are performed
	 */
	private CalcModel model;
	
	/**
	 * Text displayed when button state is set to normal
	 */
	private String normalText;
	
	/**
	 * Text displayed when button state is inversed
	 */
	private String inverseText;
	
	/**
	 * Operation performed in normal state
	 */
	private final ActionListener normalListener = e -> model.setValue(
			normalOperation.applyAsDouble(model.getValue()));
	
	/**
	 * Operation performed in inversed state
	 */
	private final ActionListener inverseListener = e -> model.setValue(
			inverseOperation.applyAsDouble(model.getValue()));

	/**
	 * Constructs new {@link UnaryOperationButton} with specified parameters.
	 * @param normalText
	 *        text displayed in normal state
	 * @param inverseText
	 *        text displayed in inversed state
	 * @param model
	 *        model used for operations
	 * @param normalOperation
	 *        operation performed in normal state
	 * @param inverseOperation
	 *        operation performed in inversed state
	 */
	public UnaryOperationButton(String normalText, String inverseText, CalcModel model, 
			DoubleUnaryOperator normalOperation, DoubleUnaryOperator inverseOperation) {
		super(normalText);
		this.normalText = normalText;
		this.inverseText = inverseText;
		this.normalOperation = normalOperation;
		this.inverseOperation = inverseOperation;
		this.model = model;
		setNormal();
	}
	
	/**
	 * Sets this button to normal state.
	 */
	public void setNormal() {
		setText(normalText);
		removeActionListener(inverseListener);
		addActionListener(normalListener);
	}
	
	/**
	 * Sets this button to inversed state.
	 */
	public void setInverse() {
		setText(inverseText);
		removeActionListener(normalListener);
		addActionListener(inverseListener);
	}
		
}
