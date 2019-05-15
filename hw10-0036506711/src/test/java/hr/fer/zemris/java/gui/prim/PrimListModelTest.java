package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PrimListModelTest {

	@Test
	public void testConstructor() {
		PrimListModel listModel = new PrimListModel();
		assertEquals(1, listModel.getSize());
		assertEquals(1, listModel.getElementAt(0));
	}
	
	@Test
	public void testSize() {
		PrimListModel listModel = new PrimListModel();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		assertEquals(6, listModel.getSize());
	}
	
	@Test
	public void testNext() {
		PrimListModel listModel = new PrimListModel();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		assertEquals(1, listModel.getElementAt(0));
		assertEquals(2, listModel.getElementAt(1));
		assertEquals(3, listModel.getElementAt(2));
		assertEquals(5, listModel.getElementAt(3));
		assertEquals(7, listModel.getElementAt(4));
		assertEquals(11, listModel.getElementAt(5));
		assertEquals(6, listModel.getSize());
	}
	
	@Test
	public void testGetElementAt() {
		PrimListModel listModel = new PrimListModel();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		listModel.next();
		assertEquals(1, listModel.getElementAt(0));
		assertEquals(2, listModel.getElementAt(1));
		assertEquals(3, listModel.getElementAt(2));
		assertEquals(5, listModel.getElementAt(3));
		assertEquals(7, listModel.getElementAt(4));
		assertEquals(11, listModel.getElementAt(5));
	}
	
}
