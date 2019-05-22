package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Represents AbstractAction which retrieves its name from the given
 * {@link ILocalizationProvider}.
 * 
 * @author Filip Husnjak
 */
public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 5991512482803088003L;
	
	/**
	 * Constructs new {@link LocalizableAction} with the given {@link ILocalizationProvider}
	 * and key used to retrieve its name.
	 * 
	 * @param key
	 *        key for the localization
	 * @param lp
	 *        {@link ILocalizationProvider} used to retrieve name
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		setValues(key, lp);
		lp.addLocalizationListener(() -> setValues(key, lp));
	}
	
	/**
	 * Sets name and description for this action.
	 * 
	 * @param key
	 *        key for string
	 * @param lp
	 *        localization provider used for localization
	 */
	private void setValues(String key, ILocalizationProvider lp) {
		putValue(Action.NAME, lp.getString(key));
		putValue(Action.SHORT_DESCRIPTION, lp.getString(key + "Desc"));
	}
	
}
