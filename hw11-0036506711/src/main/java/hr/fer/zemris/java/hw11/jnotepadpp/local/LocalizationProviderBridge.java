package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Implementation of {@link AbstractLocalizationProvider} that represents the
 * bridge from the frame to the {@link LocalizationProvider} listeners.
 * 
 * @author Filip Husnjak
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Provider this bridge is connected to.
	 */
	private ILocalizationProvider provider;
	
	/**
	 * {@code true} if the bridge is currently connected
	 */
	private boolean connected;
	
	/**
	 * Cached language before this bridge was disconnected.
	 */
	private String oldLanguage;
	
	/**
	 * Listener used to connect to {@link #provider}.
	 */
	private final ILocalizationListener listener = this::fire;
	
	/**
	 * Constructs new {@link LocalizationProviderBridge} with specified
	 * {@link ILocalizationProvider}.
	 * 
	 * @param provider
	 *        {@link ILocalizationProvider} this bridge should be connected to
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * Connects this bridge to {@link #provider}. If cached language is not
	 * equal to current language listeners are notified about the change.
	 */
	public void connect() {
		if (connected) return;
		provider.addLocalizationListener(listener);
		connected = true;
		if (provider.getCurrentLanguage() != oldLanguage) {
			oldLanguage = provider.getCurrentLanguage();
			fire();
		}
	}
	
	/**
	 * Disconnects this bridge from {@link #provider}.
	 */
	public void disconnect() {
		provider.removeLocalizationListener(listener);
		connected = false;
	}
	
	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return provider.getCurrentLanguage();
	}
	
}
