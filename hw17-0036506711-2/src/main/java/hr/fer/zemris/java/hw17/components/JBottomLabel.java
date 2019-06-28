package hr.fer.zemris.java.hw17.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import hr.fer.zemris.java.hw17.model.IColorProvider;

public class JBottomLabel extends JLabel {

	private static final long serialVersionUID = -6036556022560726579L;
	
	private Color bgColor;
	
	private Color fgColor;
	
	public JBottomLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		initGui(fgColorProvider, bgColorProvider);
		addListeners(fgColorProvider, bgColorProvider);
	}
	
	private void initGui(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(padding);
		fgColor = fgColorProvider.getCurrentColor();
		bgColor = bgColorProvider.getCurrentColor();
		setText();
	}

	private void addListeners(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		fgColorProvider.addColorChangeListener((p, c1, c2) -> {
			fgColor = c2;
			setText();
		});
		bgColorProvider.addColorChangeListener((p, c1, c2) -> {
			bgColor = c2;
			setText();
		});
	}

	private void setText() {
		setText(String.format("Foreground color: %s, "
				+ "background color: %s.", getRGB(fgColor), getRGB(bgColor)));
		repaint();
	}
	
	private String getRGB(Color color) {
		return String.format("(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
	}
	
}
