package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.utils.FileUtils;

/**
 * Implementation of {@link MultipleDocumentModel} used for implementing
 * JNotepad++.
 * 
 * @author Filip Husnjak
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final long serialVersionUID = -2515137507092721994L;
	
	/**
	 * Default tab name.
	 */
	public static final String DEFAULT_NAME = "(unnamed)";
	
	/**
	 * List of documents in this model.
	 */
	private List<SingleDocumentModel> documents = new ArrayList<>();
	
	/**
	 * Listeners to be activated upon each change.
	 */
	private List<MultipleDocumentListener> listeners = new LinkedList<>();
	
	/**
	 * Represents current active document.
	 */
	private SingleDocumentModel currentModel;
	
	/**
	 * Constructs new {@link MultipleDocumentModel} and adds appropriate listener.
	 */
	public DefaultMultipleDocumentModel() {
		addTabChangeListener();
	}
	
	/**
	 * Adds listener that observes each change of a tab.
	 */
	private void addTabChangeListener() {
		addChangeListener(e -> {
			if (currentModel != null) {
				currentModel.removeSingleDocumentListener(listener);
			}
			SingleDocumentModel nextModel = getSelectedIndex() < 0 ?
					null : documents.get(getSelectedIndex());
			if (nextModel != null) {
				nextModel.addSingleDocumentListener(listener);
			}
			notifyListeners(l -> l.currentDocumentChanged(
					currentModel,
					currentModel = nextModel));
		});
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return addDocument(null, new DefaultSingleDocumentModel(null, null));
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentModel;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Given file cannot be null!");
		try {
			int index = getExistingIndex(path);
			if (index >= 0) {
				setSelectedIndex(index);
				return documents.get(index);
			}
			return addDocument(path, getModelFromPath(path));
		} catch (IOException e) {
			throw new IllegalArgumentException("File cannot be opened!");
		}
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		try {
			if (getExistingIndex(newPath) >= 0) {
				FileUtils.showDialog(
						this, 
						LocalizationProvider.getInstance().getString("openedMessage"),
						LocalizationProvider.getInstance().getString("openedTitle"),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (newPath != null && Files.exists(newPath)) {
				String message = newPath.getFileName().toString() + 
						" " + 
						LocalizationProvider.getInstance().getString("existsMessage");
				if (FileUtils.showDialog(
						this,
						message,
						LocalizationProvider.getInstance().getString("existsTitle"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}
			}
			String text = model.getTextComponent().getText();
			Path path = newPath == null ? model.getFilePath() : newPath;
			Files.writeString(path, text);
			model.setFilePath(path);
			model.setModified(false);
		} catch (IOException e) {
			throw new IllegalArgumentException("File cannot be opened!");
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		if (index == -1) return;
		notifyListeners(l -> l.documentRemoved(model));
		documents.remove(model);
		removeTabAt(index);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l, "Given listener cannot be null!"));
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		Objects.checkIndex(index, documents.size());
		return documents.get(index);
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		JPanel panel = (JPanel) getTabComponentAt(index);
		JLabel leftLabel = (JLabel) panel.getComponents()[0];
		leftLabel.setIcon(icon);
	}

	@Override
	public void activateDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		if (index < 0) {
			throw new IllegalArgumentException("Given document model does not exist!");
		}
		setSelectedIndex(index);
	}
	
	/**
	 * Creates tab for the given {@link SingleDocumentModel} with appropriate
	 * components.
	 * 
	 * @param model
	 *        model whose tab is to be created
	 * @return created tab
	 */
	private JPanel createTab(SingleDocumentModel model) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		JLabel leftLabel = new JLabel();
		
		JLabel centerLabel = new JLabel();
		
		JButton rightButton = new JButton();
		rightButton.addActionListener(
				e -> notifyListeners(l -> l.wantCLoseDocument(model)));
		rightButton.setOpaque(false);
		rightButton.setMargin(new Insets(0, 0, 0, 0));
		rightButton.setIcon(loadIcon("close"));
		rightButton.setBorder(null);
		
		panel.add(leftLabel);
		panel.add(centerLabel);
		panel.add(rightButton);		
		return panel;
	}
	
	/**
	 * Adds the given document model to the collection, creates appropriate tab
	 * and notifies all listeners that document was added.
	 * 
	 * @param path
	 *        path of a added document, if {@code null} it means document was
	 *        just created
	 * @param model
	 *        document to be added
	 * @return added document
	 */
	private SingleDocumentModel addDocument(Path path, SingleDocumentModel model) {
		documents.add(model);
		addTab("", new JScrollPane(model.getTextComponent()));
		setTabComponentAt(getTabCount() - 1, createTab(model));
		setTitleAt(getTabCount() - 1, path);
		setIconAt(getTabCount() - 1, loadIcon("greenDisk"));
		notifyListeners(l -> l.documentAdded(model));
		setSelectedIndex(getTabCount() - 1);
		return model;
	}
	
	/**
	 * Returns an index of a document that has specified path, if document with
	 * specified path does not exist -1 is returned.
	 * 
	 * @param path
	 *        path to be checked
	 * @return an index of a document that has specified path
	 * @throws IOException if there was a problem checking if paths point to the
	 *         same file
	 */
	private int getExistingIndex(Path path) throws IOException {
		if (path == null) return -1;
		for (int i = 0; i < documents.size(); ++i) {
			Path docPath = documents.get(i).getFilePath();
			if (docPath == null) continue;
			if (!Files.exists(path) || !Files.exists(docPath)) {
				continue;
			}
			if (Files.isSameFile(docPath, path)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns new instance of {@link SingleDocumentModel} created from the
	 * given path. The text of the file with the given path is copied to the
	 * new {@link SingleDocumentModel} text area.
	 * 
	 * @param path
	 *        path of a file to opened
	 * @return new instance of {@link SingleDocumentModel} created from the
	 *         given path
	 * @throws IOException if there was an error copying text from the given file
	 */
	private SingleDocumentModel getModelFromPath(Path path) throws IOException {
		String text = Files.readString(path);
		return new DefaultSingleDocumentModel(path, text);
	}
	
	/**
	 * Loads an icon with specified name of an image in resources.
	 * 
	 * @param name
	 *        name of an icon to be loaded
	 * @return created icon
	 */
	private ImageIcon loadIcon(String name) {
		try (InputStream is = this.getClass().getResourceAsStream("icons/" + name + ".png")) {
			if(is == null) {
				throw new RuntimeException("Error getting input stream!");
			}
			byte[] bytes = is.readAllBytes();
			return new ImageIcon(bytes);
		} catch (IOException e) {
			throw new RuntimeException("Error reading input stream!");
		}
	}
	
	/**
	 * Listener of this {@link MultipleDocumentModel} that observes changes
	 * upon current document model represented with {@link SingleDocumentModel}.
	 */
	private SingleDocumentListener listener = new SingleDocumentListener() {
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			ImageIcon icon = loadIcon(model.isModified() ? "redDisk" : "greenDisk");
			setIconAt(getSelectedIndex(), icon);
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			getTabComponentAt(getSelectedIndex());
			setTitleAt(getSelectedIndex(), model.getFilePath());
		}
	};
	
	/**
	 * Sets title at the tab with specified index using path.
	 * 
	 * @param index
	 *        index of a tab whose title is to be set
	 * @param path
	 *        path used for title
	 */
	private void setTitleAt(int index, Path path) {
		setToolTipTextAt(
				index, 
				path == null ? DEFAULT_NAME : path.toString());
		JPanel panel = (JPanel) getTabComponentAt(index);
		JLabel centerLabel = (JLabel) panel.getComponents()[1];
		centerLabel.setText(path == null ? 
				DEFAULT_NAME : path.getFileName().toString());
	}
	
	/**
	 * Notifies all listeners with proper change defined with given {@link Consumer}.
	 * 
	 * @param notify
	 *        consumer that notifies all listeners with proper method
	 */
	private void notifyListeners(Consumer<MultipleDocumentListener> notify) {
		for (MultipleDocumentListener listener: listeners) {
			notify.accept(listener);
		}
	}

}
