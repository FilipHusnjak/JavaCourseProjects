package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Represents localization provider that provides useful methods used for
 * app localization.
 * 
 * @author Filip Husnjak
 */
public interface ILocalizationProvider {

	/**
	 * Adds the listener that will observe language changes,
	 * to this localization provider.
	 * 
	 * @param l
	 *        listener to be added
	 * @throws NullPointerException if the given listener is {@code null}
	 */
	void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Removes the given listener from listeners collection of this 
	 * {@link ILocalizationProvider}.
	 * 
	 * @param l
	 *        listener to be removed
	 */
	void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Returns the string for the given key.
	 * 
	 * @param key
	 *        key of the string to be returned
	 * @return the string for the given key
	 */
	String getString(String key);
	
	/**
	 * Returns the current language used by this {@link ILocalizationProvider}.
	 * 
	 * @return the current language used by this {@link ILocalizationProvider}
	 */
	String getCurrentLanguage();
	
}
