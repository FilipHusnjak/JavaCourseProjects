package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.hw01.UniqueNumbers.*;

public class UniqueNumbersTest {
	
	@Test
	public void testTreeSizeAddNode() {
		TreeNode root = null;
		
		assertEquals(0, treeSize(root));
		
		root = addNode(root, 42);
		root = addNode(root, 76);
		root = addNode(root, 21);
		
		assertEquals(3, treeSize(root));
		
		root = addNode(root, 76);
		root = addNode(root, 35);
		
		assertEquals(4, treeSize(root));

	}
	
	@Test
	public void testContainsValue() {
		TreeNode root = null;
		
		assertFalse(containsValue(root, 1));
		
		root = addNode(root, 1);
		assertTrue(containsValue(root, 1));
		
	}
	
}
