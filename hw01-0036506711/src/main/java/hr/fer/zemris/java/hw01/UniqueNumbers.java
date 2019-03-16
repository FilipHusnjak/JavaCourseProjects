package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * 
 * Program that stores data in a binary tree.
 * 
 * @author Filip Husnjak
 *
 */

public class UniqueNumbers {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		TreeNode root = null;
		
		while (true) {
			System.out.print("Unesite broj > ");
			String entry = sc.next();
			if (entry.equals("kraj")) {
				break;
			}
			
			try {
				int number = Integer.parseInt(entry);
				
				if (containsValue(root, number)) {
					System.out.println("Broj već postoji. Preskačem.");
					
				} else {
					root = addNode(root, number);
					System.out.println("Dodano");
					
				}
				
			} catch(NumberFormatException ex) {
				System.out.printf("\'%s\' nije cijeli broj.%n", entry);
				
			}
		}
		System.out.print("Ispis od najmanjeg: ");
		printInOrder(root);
		System.out.println();
		
		System.out.print("Ispis od najvećeg: ");
		printOutOrder(root);
		System.out.println();
		
		sc.close();
		
	}
	
	/**
	 * 
	 * Prints tree in-order.
	 * 
	 * @param root
	 *        root node of the tree
	 */
	
	public static void printInOrder(TreeNode root) {
		if (root == null) {
			return;
		}
		printInOrder(root.left);
		System.out.print(root.value + " ");
		printInOrder(root.right);
	}
	
	/**
	 * 
	 * Prints tree out-order.
	 * 
	 * @param root
	 *        root node of the tree
	 */
	
	public static void printOutOrder(TreeNode root) {
		if (root == null) {
			return;
		}
		printOutOrder(root.right);
		System.out.print(root.value + " ");
		printOutOrder(root.left);
	}
	
	/**
	 * 
	 * Adds node to a given tree.
	 * 
	 * @param root
	 *        root of a tree
	 *        
	 * @param value
	 *        value of a tree node
	 * 
	 * @return root of the final tree
	 */
	
	public static TreeNode addNode(TreeNode root, int value) {
		
		if (root == null) {
			root = new TreeNode();
			root.value = value;
			return root;
		}
		
		if (root.value == value) {
			return root;
		}
		
		if (value < root.value) {
			root.left = addNode(root.left, value);
			
		} else {
			root.right = addNode(root.right, value);
			
		}

		return root;
		
	}
	
	
	
	/**
	 * 
	 * Checks if tree contains a given value.
	 * 
	 * @param root
	 *        root node of a tree
	 * 
	 * @param value
	 *        value whose presence in this tree is to be tested
	 * 
	 * @return true if a tree contains specified value
	 */
	
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) {
			return false;
		}
		
		if (root.value == value) {
			return true;
		}
		
		if (value < root.value) {
			return containsValue(root.left, value);
		}
		
		return containsValue(root.right, value);
	}
	
	/**
	 * 
	 * Calculates size of a specified tree.
	 * 
	 * @param root
	 *        root node of a tree
	 * 
	 * @return size of a specified tree
	 */
	
	public static int treeSize(TreeNode root) {
		if (root == null) return 0;
		return 1 + treeSize(root.left) + treeSize(root.right);
	}

	/**
	 * 
	 * Representation of a tree node.
	 * 
	 * @author Filip Husnjak
	 *
	 */
	
	public static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;
	}
	
}
