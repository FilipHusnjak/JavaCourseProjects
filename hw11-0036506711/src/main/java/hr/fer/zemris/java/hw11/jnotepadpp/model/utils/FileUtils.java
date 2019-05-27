package hr.fer.zemris.java.hw11.jnotepadpp.model.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class FileUtils {

	private FileUtils() {}
	
	public static int showDialog(Component context, String message, 
			String title, int optionType, int messageType) {
		return JOptionPane.showOptionDialog(
				context,
				message,
				title,
				optionType,
				messageType,
				null, null, null);
	}
	
}
