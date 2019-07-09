package hr.fer.zemris.java.hw17.jvdraw.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;

/**
 * Implementation of {@link GeometricalObjectEditor} used to edit Circles.
 * 
 * @author Filip Husnjak
 */
public class CircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 9003306278950191453L;

	/** Text field for editing X coordinate of the center */
	private JTextField centerXField;
	
	/** Text field for editing Y coordinate of the center */
	private JTextField centerYField;
	
	/** Text field for editing radius */
	private JTextField radiusField;
	
	/** Text field for editing r component of the color */
	private JTextField rField;
	
	/** Text field for editing g component of the color */
	private JTextField gField;
	
	/** Text field for editing b component of the color */
	private JTextField bField;
	
	/** Circle object to be edited */
	private Circle circle;
	
	/**
	 * Constructs new {@link CircleEditor} with the given circle object.
	 * 
	 * @param circle
	 *        circle object to be edited
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;
		initGui();
	}
	
	/**
	 * Initializes GUI of this JComponent.
	 */
	private void initGui() {
		centerXField = new JTextField(String.valueOf(circle.getCenterX()));
		centerYField = new JTextField(String.valueOf(circle.getCenterY()));
		radiusField = new JTextField(String.valueOf(circle.getRadius()));
		rField = new JTextField(String.valueOf(circle.getFgColor().getRed()));
		gField = new JTextField(String.valueOf(circle.getFgColor().getGreen()));
		bField = new JTextField(String.valueOf(circle.getFgColor().getBlue()));
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Center X: "));
		add(centerXField);
		add(new JLabel("Center Y: "));
		add(centerYField);
		add(new JLabel("Radius: "));
		add(radiusField);
		add(new JLabel("R: "));
		add(rField);
		add(new JLabel("G: "));
		add(gField);
		add(new JLabel("B: "));
		add(bField);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(centerXField.getText());
			Integer.parseInt(centerYField.getText());
			Integer.parseInt(radiusField.getText());
			int r = Integer.parseInt(rField.getText());
			int g = Integer.parseInt(gField.getText());
			int b = Integer.parseInt(bField.getText());
			checkRange(r);
			checkRange(g);
			checkRange(b);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Integer values expected!");
		}
	}

	/**
	 * Checks range of the given value.
	 * 
	 * @param c
	 *        value whose range is to be checked
	 */
	private void checkRange(int c) {
		if (c < 0 || c > 255) {
			throw new IllegalArgumentException("Color number out of range [0, 255]!");
		}
	}

	@Override
	public void acceptEditing() {
		int centerX = Integer.parseInt(centerXField.getText());
		int centerY = Integer.parseInt(centerYField.getText());
		int radius = Integer.parseInt(radiusField.getText());
		int r = Integer.parseInt(rField.getText());
		int g = Integer.parseInt(gField.getText());
		int b = Integer.parseInt(bField.getText());
		circle.setCenter(centerX, centerY);
		circle.setRadius(radius);
		circle.setFgColor(new Color(r, g, b));
	}

}
