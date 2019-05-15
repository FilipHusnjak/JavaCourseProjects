package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalcLayoutTest {
	
	private CalcLayout layout;
	
	@BeforeEach
	public void setup() {
		layout = new CalcLayout();
	}

	@Test
	public void testIllegalPositionException() {
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "0, 1"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "6, 1"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 0"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 8"));
	}
	
	@Test
	public void testFirstComponentException() {
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 5"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 4"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 3"));
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 2"));
	}
	
	@Test
	public void testMultipleComponentsSamePlace() {
		layout.addLayoutComponent(new JLabel(), "1, 1");
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 1"));
		
		layout.addLayoutComponent(new JLabel(), "1, 7");
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "1, 7"));
		
		layout.addLayoutComponent(new JLabel(), "2, 5");
		assertThrows(CalcLayoutException.class, () -> layout.addLayoutComponent(new JLabel(), "2, 5"));
	}
	
	@Test
	public void testHomeworkExample1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		assertEquals(dim.width, 152);
		assertEquals(dim.height, 158);
	}
	
	@Test
	public void testHomeworkExample2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		assertEquals(dim.width, 152);
		assertEquals(dim.height, 158);
	}
	
}
