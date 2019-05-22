package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

/**
 * Represents JLabel which retrieves its text from the given 
 * {@link ILocalizationProvider}.
 * 
 * @author Filip Husnjak
 */
public class LocalizableLabel extends JLabel {

	private static final long serialVersionUID = -5999968617952275339L;

	/**
	 * Constructs new {@link LocalizableLabel} with the given {@link ILocalizationProvider}
	 * and key used to retrieve its text.
	 * 
	 * @param key
	 *        key for the localization
	 * @param lp
	 *        {@link ILocalizationProvider} used to retrieve text
	 */
	public LocalizableLabel(String key, ILocalizationProvider lp) {
		setText(lp.getString(key));
		lp.addLocalizationListener(() -> setText(lp.getString(key)));
	}
	
}
