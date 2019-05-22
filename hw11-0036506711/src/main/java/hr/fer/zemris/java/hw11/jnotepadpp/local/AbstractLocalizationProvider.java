package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link ILocalizationProvider} that implements methods for
 * manipulating with listeners. getString() method is still not implemented.
 * 
 * @author Filip Husnjak
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	/**
	 * Listeners of this {@link AbstractLocalizationProvider}
	 */
	private List<ILocalizationListener> listeners = new LinkedList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners about the localization change.
	 */
	public void fire() {
		for (ILocalizationListener listener: listeners) {
			listener.localizationChanged();
		}
	}
	
}
