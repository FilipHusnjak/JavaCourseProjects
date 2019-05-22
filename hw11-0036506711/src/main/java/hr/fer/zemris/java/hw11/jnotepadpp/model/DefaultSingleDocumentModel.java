package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Implementation of {@link SingleDocumentModel} used for implementing
 * JNotepad++.
 * 
 * @author Filip Husnjak
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/**
	 * Text area used for writing a document.
	 */
	private JTextArea textArea = new JTextArea();

	/**
	 * Path of this document.
	 */
	private Path file;
	
	/**
	 * Represents whether this document is modified or not.
	 */
	private boolean modified;
	
	/**
	 * Listeners that observe changes upon this document.
	 */
	private List<SingleDocumentListener> listeners = new LinkedList<>();
	
	/**
	 * Represents text of this document before modifications.
	 */
	private String beforeModified = "";
	
	/**
	 * Constructs new {@link SingleDocumentModel} with given path and given
	 * text. Both path and text can be {@code null}.
	 * 
	 * @param file
	 *        path of the new document
	 * @param text
	 *        text of the new document
	 */
	public DefaultSingleDocumentModel(Path file, String text) {
		this.file = file == null ? file : file.toAbsolutePath().normalize();
		textArea.setText(text);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(isReallyModified());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(isReallyModified());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(isReallyModified());
			}
			
			private boolean isReallyModified() {
				return !textArea.getText().equals(beforeModified);
			}
		});
	}
	
	/**
	 * Notifies all listeners about document change specified with given
	 * {@link Consumer}.
	 * 
	 * @param notify
	 *        specifies which change was observed
	 */
	private void notifyListeners(Consumer<SingleDocumentListener> notify) {
		for (SingleDocumentListener listener: listeners) {
			notify.accept(listener);
		}
	}
	
	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return file;
	}

	@Override
	public void setFilePath(Path path) {
		this.file = Objects.requireNonNull(path, "Given file cannot be null!")
				.toAbsolutePath().normalize();
		notifyListeners(l -> l.documentFilePathUpdated(this));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		beforeModified = modified ? beforeModified : textArea.getText();
		notifyListeners(l -> l.documentModifyStatusUpdated(this));
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l, "Given listener cannot be null!"));
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

}
