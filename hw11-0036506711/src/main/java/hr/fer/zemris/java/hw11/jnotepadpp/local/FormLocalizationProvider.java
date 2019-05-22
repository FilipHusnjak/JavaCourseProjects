package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Represents {@link LocalizationProvider} used by a frame. It automatically
 * connects / disconnects this {@link LocalizationProviderBridge} from the
 * given {@link LocalizationProvider} when the window is opened / closed.
 * 
 * @author Filip Husnjak
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
	
	/**
	 * Constructs new {@link FormLocalizationProvider} with the given
	 * provider and frame.
	 * 
	 * @param provider
	 *        provider this bridge should connect to
	 * @param frame
	 *        frame whose window operations are observed
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
		});
	}
	
}
