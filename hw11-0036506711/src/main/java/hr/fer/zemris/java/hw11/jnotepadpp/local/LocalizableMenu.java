package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Represents JMenu which retrieves its text from the given 
 * {@link ILocalizationProvider}.
 * 
 * @author Filip Husnjak
 */
public class LocalizableMenu extends JMenu {

	private static final long serialVersionUID = -8581953613587604519L;

	/**
	 * Constructs new {@link LocalizableMenu} with the given {@link ILocalizationProvider}
	 * and key used to retrieve its text.
	 * 
	 * @param key
	 *        key for the localization
	 * @param lp
	 *        {@link ILocalizationProvider} used to retrieve text
	 */
	public LocalizableMenu(String key, ILocalizationProvider lp) {
		setText(lp.getString(key));
		lp.addLocalizationListener(() -> setText(lp.getString(key)));
	}
	
}
