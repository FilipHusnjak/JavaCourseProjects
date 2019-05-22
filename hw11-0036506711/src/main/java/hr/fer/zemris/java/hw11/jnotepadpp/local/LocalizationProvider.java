package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Implementation of {@link AbstractLocalizationProvider} that has proper
 * implementation of getString method. This class is singleton which means
 * it cannot be instantiated outside of this scope.
 * 
 * @author Filip Husnjak
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/**
	 * {@link ResourceBundle} used for localization.
	 */
	private ResourceBundle bundle;
	
	/**
	 * Current language.
	 */
	private String language;
	
	/**
	 * Singleton instance of {@link LocalizationProvider}.
	 */
	private static LocalizationProvider instance;
	
	/**
	 * This class cannot be instantiated outside of this scope.
	 * Constructs new {@link LocalizationProvider} with the given language.
	 */
	private LocalizationProvider(String language) {
		setLanguage(language);
	}
	
	/**
	 * Returns {@link #instance} if it exists, otherwise this method will first
	 * create instance and return then it. Language is set to English by default.
	 * 
	 * @return instance of {@link LocalizationProvider}
	 */
	public static LocalizationProvider getInstance() {
		if (instance != null) return instance;
		instance = new LocalizationProvider("en");
		return instance;
	}
	
	/**
	 * Sets language of this {@link LocalizationProvider} to the specified one.
	 * 
	 * @param language
	 *        new language of this {@link LocalizationProvider}
	 */
	public void setLanguage(String language) {
		Locale locale = Locale.forLanguageTag(this.language = language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.translations", 
				locale);
		fire();
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}
	
}
