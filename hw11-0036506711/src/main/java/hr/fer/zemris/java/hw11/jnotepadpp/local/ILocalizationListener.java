package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Represents listener that is notified about each lozalization change.
 * 
 * @author Filip Husnjak
 */
public interface ILocalizationListener {

	/**
	 * Notifies observer about localization changes.
	 */
	void localizationChanged();
	
}
