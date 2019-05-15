package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program displays 2 columns each containing list of prime numbers.
 * Button next generates new prime number and the lists are updated accordingly.
 * 
 * @author Filip Husnjak
 */
public class PrimDemo	 {
	
	/**
	 * Width of the window
	 */
	private static int WINDOW_WIDTH = 500;
	
	/**
	 * Height of the window
	 */
	private static int WINDOW_HEIGHT = 500;

	/**
	 * Displays the window. Takes no arguments.
	 * 
	 * @param args
	 *        ignored
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JButton btnNext = new JButton("next");
		btnNext.addActionListener(e -> model.next());
		
		JPanel centerPanel = new JPanel(new GridLayout(1, 0));
		centerPanel.add(new JScrollPane(new JList<>(model)), BorderLayout.WEST);
		centerPanel.add(new JScrollPane(new JList<>(model)), BorderLayout.EAST);
		
		container.add(centerPanel, BorderLayout.CENTER);
		
		container.add(btnNext, BorderLayout.SOUTH);
		
		frame.setLocationRelativeTo(null);
		
		SwingUtilities.invokeLater(() -> frame.setVisible(true));
	}

}

