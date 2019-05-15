package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List model that generates prime numbers.
 * 
 * @author Filip Husnjak
 */
public class PrimListModel implements ListModel<Integer> {
	
	/**
	 * Listeners of this model.
	 */
	private List<ListDataListener> listeners = new LinkedList<>();
	
	/**
	 * Currently generated prime numbers.
	 */
	private List<Integer> prims = new ArrayList<>();

	/**
	 * Constructs new {@link PrimListModel}.
	 */
	public PrimListModel() {
		next();
	}
	
	@Override
	public int getSize() {
		return prims.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return prims.get(index);
	}
	
	/**
	 * Generates and adds to the list new prime number.
	 */
	public void next() {
		getNextPrim(prims.size() == 0 ? 0 : prims.get(prims.size() - 1));
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize() - 1, getSize() - 1);
		for (ListDataListener listener: listeners) {
			listener.intervalAdded(event);
		}
	}

	/**
	 * Generates next prime number.
	 * 
	 * @param lastPrim
	 *        last prime number that was generated
	 */
	private void getNextPrim(int lastPrim) {
		for (lastPrim++; !isPrim(lastPrim); ++lastPrim);
		prims.add(lastPrim);
	}

	/**
	 * Checks whether the given number is prime.
	 * 
	 * @param num
	 *        number to be checked
	 * @return {@code true} is the given number is prime
	 */
	private boolean isPrim(int num) {
		for (int i = 2, n = (int) Math.sqrt(num); i <= n; ++i) {
			if (num % i == 0) return false;
		}
		return true;
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

}
