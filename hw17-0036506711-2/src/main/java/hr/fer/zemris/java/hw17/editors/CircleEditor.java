package hr.fer.zemris.java.hw17.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.objects.Circle;

public class CircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 9003306278950191453L;

	private JTextField centerXField;
	
	private JTextField centerYField;
	
	private JTextField radiusField;
	
	private JTextField rField;
	
	private JTextField gField;
	
	private JTextField bField;
	
	private Circle circle;
	
	public CircleEditor(Circle circle) {
		this.circle = circle;
		initGui();
	}
	
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
