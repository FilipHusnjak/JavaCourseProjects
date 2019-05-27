package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.utils.FileUtils;

/**
 * Represents application for editing and creating text files.
 * 
 * @author Filip Husnjak
 */
public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 5729111831966518041L;
	
	/**
	 * {@link MultipleDocumentModel} used by this application.
	 */
	private DefaultMultipleDocumentModel model;
	
	/**
	 * Localization provider used for retrieving proper translations.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), 
			this);
	
	/**
	 * Name of this application.
	 */
	public static final String APP_NAME = "JNotepadPP";
	
	/**
	 * Label that shows length of a current document.
	 */
	private JLabel lengthLabel = new JLabel("-");
	
	/**
	 * Label that shows line number of a caret position in a current document.
	 */
	private JLabel lineLabel = new JLabel("-");
	
	/**
	 * Label that shows column number of a caret position in a current document.
	 */
	private JLabel columnLabel = new JLabel("-");
	
	/**
	 * Label that shows length of a current selection.
	 */
	private JLabel selectionLabel = new JLabel("-");
	
	/**
	 * Collator used for locale-sensitive string comparison.
	 */
	private Collator collator;
	
	private volatile boolean running = true;
	
	/**
	 * Constructs new {@link JNotepadPP}, initializes GUI and sets appropriate
	 * listeners.
	 */
	public JNotepadPP() {
		setCollator();
		addLocalizationListener();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(APP_NAME);
        setSize(1000, 1000);
        addWindowListener(windowClosing);
        initGui();
        setLocationRelativeTo(null);
	}

	/**
	 * Adds listener which observes each change in current language.
	 */
	private void addLocalizationListener() {
		flp.addLocalizationListener(() -> {
			setOptionPaneLanguage();
			setFileChooserLanguage();
			setCollator();
		});
	}
	
	/**
	 * Sets language for {@link JOptionPane} dialogs.
	 */
	private void setOptionPaneLanguage() {
		UIManager.put("OptionPane.yesButtonText", flp.getString("yes"));
		UIManager.put("OptionPane.noButtonText", flp.getString("no"));
		UIManager.put("OptionPane.cancelButtonText", flp.getString("cancel"));
		UIManager.put("OptionPane.okButtonText", flp.getString("ok"));
	}

	/**
	 * Sets language for {@link JFileChooser} dialogs.
	 */
	private void setFileChooserLanguage() {
		UIManager.put("FileChooser.openButtonText", flp.getString("open"));
        UIManager.put("FileChooser.cancelButtonText", flp.getString("cancel"));
        UIManager.put("FileChooser.saveButtonText", flp.getString("save"));
        UIManager.put("FileChooser.lookInLabelText", flp.getString("lookIn"));
        UIManager.put("FileChooser.fileNameLabelText", flp.getString("fileName"));
        UIManager.put("FileChooser.filesOfTypeLabelText", flp.getString("filesOfType"));
	}

	/**
	 * Sets collator to use current language for string comparison.
	 */
	private void setCollator() {
		Locale locale = new Locale(
				LocalizationProvider.getInstance().getCurrentLanguage());
		collator = Collator.getInstance(locale);
	}

	/**
	 * Initializes GUI of this application.
	 */
	private void initGui() {
		model = new DefaultMultipleDocumentModel();
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(model, BorderLayout.CENTER);
		addDocChangeListener();
		createActions();
		createMenubar();
		cp.add(createToolbar(), BorderLayout.NORTH);
		cp.add(createStatusbar(), BorderLayout.SOUTH);
	}

	/**
	 * Adds listener to observe changes upon {@link JNotepadPP#model}.
	 */
	private void addDocChangeListener() {
		model.addMultipleDocumentListener(docChangedListener);
	}

	/**
	 * Creates and initializes actions.
	 */
	private void createActions() {
		setAction(newFileAction, "control N", KeyEvent.VK_N, true);
		setAction(openFileAction, "control O", KeyEvent.VK_O, true);
		setAction(saveFileAction, "control S", KeyEvent.VK_S, false);
		setAction(saveAsFileAction, "control alt S", KeyEvent.VK_V, false);
		setAction(copyAction, "control C", KeyEvent.VK_C, false);
		setAction(cutAction, "control X", KeyEvent.VK_T, false);
		setAction(pasteAction, "control V", KeyEvent.VK_P, false);
		setAction(statisticsAction, "control alt T", KeyEvent.VK_C, false);
		setAction(closeTabAction, "control W", KeyEvent.VK_E, false);
		setAction(exitAction, "control Q", KeyEvent.VK_X, true);
		setAction(toUpperCaseAction, "control U", KeyEvent.VK_U, false);
		setAction(toLowerCaseAction, "control L", KeyEvent.VK_L, false);
		setAction(invertCaseAction, "control I", KeyEvent.VK_I, false);
		setAction(ascSortAction, "control alt A", KeyEvent.VK_A, false);
		setAction(descSortAction, "control alt D", KeyEvent.VK_D, false);
		setAction(uniqueAction, "control alt U", KeyEvent.VK_Q, false);
	}
	
	/**
	 * Sets up given action with given accelerator key and flag that represents
	 * whether action should be enabled/disabled.
	 * 
	 * @param action
	 *        action to be set
	 * @param accKey
	 *        accelerator key
	 * @param enabled
	 *        {@code true} if the action should be enabled
	 */
	private void setAction(Action action, String accKey, int mnemonicKey, boolean enabled) {
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accKey));
		action.putValue(Action.MNEMONIC_KEY, mnemonicKey);
		action.setEnabled(enabled);
	}
	
	/**
	 * Creates menu bar with all appropriate components.
	 */
	private void createMenubar() {
		JMenuBar mb = new JMenuBar();
		
		JMenu file = new LocalizableMenu("file", flp);
		JMenuItem newFile = new JMenuItem(newFileAction);
		JMenuItem openFile = new JMenuItem(openFileAction);
		JMenuItem saveFile = new JMenuItem(saveFileAction);
		JMenuItem saveAsFile = new JMenuItem(saveAsFileAction);
		JMenuItem statistics = new JMenuItem(statisticsAction);
		JMenuItem exit = new JMenuItem(exitAction);
		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveAsFile);
		file.add(statistics);
		file.addSeparator();
		file.add(exit);
		
		JMenu edit = new LocalizableMenu("edit", flp);
		JMenuItem cut = new JMenuItem(cutAction);
		JMenuItem copy = new JMenuItem(copyAction);
		JMenuItem paste = new JMenuItem(pasteAction);
		JMenuItem unique = new JMenuItem(uniqueAction);
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		
		JMenu languages = new LocalizableMenu("languages", flp);
		JMenuItem en = new JMenuItem(enAction);
		JMenuItem hr = new JMenuItem(hrAction);
		JMenuItem de = new JMenuItem(deAction);
		languages.add(en);
		languages.add(hr);
		languages.add(de);
		
		JMenu tools = new LocalizableMenu("tools", flp);
		
		JMenu changeCase = new LocalizableMenu("changeCase", flp);
		JMenuItem toUpperCase = new JMenuItem(toUpperCaseAction);
		JMenuItem toLowerCase = new JMenuItem(toLowerCaseAction);
		JMenuItem invertCase = new JMenuItem(invertCaseAction);
		changeCase.add(toUpperCase);
		changeCase.add(toLowerCase);
		changeCase.add(invertCase);
		
		JMenu sort = new LocalizableMenu("sort", flp);
		JMenuItem ascSort = new JMenuItem(ascSortAction);
		JMenuItem descSort = new JMenuItem(descSortAction);
		sort.add(ascSort);
		sort.add(descSort);
		
		tools.add(changeCase);
		tools.add(sort);
		tools.add(unique);
		
		mb.add(file);
		mb.add(edit);
		mb.add(languages);
		mb.add(tools);
		
		setJMenuBar(mb);
	}
	
	/**
	 * Creates and returns tool bar with all appropriate components.
	 * 
	 * @return created tool bar
	 */
	private JToolBar createToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.add(newFileAction);
		toolbar.add(openFileAction);
		toolbar.add(saveFileAction);
		toolbar.add(saveAsFileAction);
		toolbar.addSeparator();
		toolbar.add(copyAction);
		toolbar.add(cutAction);
		toolbar.add(pasteAction);
		toolbar.add(statisticsAction);
		toolbar.addSeparator();
		toolbar.add(closeTabAction);
		toolbar.add(exitAction);
		return toolbar;
	}
	
	/**
	 * Creates and returns status bar with all appropriate components.
	 * 
	 * @return status bar with all appropriate components
	 */
	private JToolBar createStatusbar() {
		JToolBar statusbar = new JToolBar();
		statusbar.setMargin(new Insets(0, 10, 0, 10));
		statusbar.setLayout(new BorderLayout());
		statusbar.setBorder(null);
		
		Border padding = BorderFactory.createEmptyBorder(0, 0, 0, 10);
		lengthLabel.setBorder(padding);
		lineLabel.setBorder(padding);
		columnLabel.setBorder(padding);
		
		JPanel leftPanel = new JPanel();
		leftPanel.add(new LocalizableLabel("length", flp));
		leftPanel.add(lengthLabel);
		statusbar.add(leftPanel, BorderLayout.WEST);
		
		JPanel centerPanel = new JPanel();
		centerPanel.add(new JLabel("Ln:"));
		centerPanel.add(lineLabel);
		centerPanel.add(new JLabel("Col:"));
		centerPanel.add(columnLabel);
		centerPanel.add(new JLabel("Sel:"));
		centerPanel.add(selectionLabel);
		statusbar.add(centerPanel, BorderLayout.CENTER);
		
		
		JPanel rightPanel = new JPanel();
		JLabel time = new JLabel();
		setTimer(time);
		rightPanel.add(time);
		statusbar.add(rightPanel, BorderLayout.EAST);
		
		return statusbar;
	}
	
	/**
	 * Sets timer with the label that shows the current time.
	 * 
	 * @param time
	 *        label that shows current time
	 */
	private void setTimer(JLabel time) {
		TimerTask task = new TimerTask() {
			DateTimeFormatter formatter = 
					DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");
			@Override
			public void run() {
				// If window is closed do not update swing GUI
				// because it will prevent application from exiting.
				if (!running) return;
				time.setText(formatter.format(LocalDateTime.now()));
			}
		};
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 0, 500);
	}
	
	/**
	 * Sets all appropriate values (status bar, title, enables/disables actions)
	 * for the current document.
	 * 
	 * @param currentModel
	 *        current document used for setting values, if {@code null} it means
	 *        all documents are closed and values should be set to default
	 */
	private void setCurrentDoc(SingleDocumentModel currentModel) {
		// If current doc exists
		if (currentModel != null) {
			saveFileAction.setEnabled(currentModel.isModified());
			setTitle((currentModel.getFilePath() == null ? 
					DefaultMultipleDocumentModel.DEFAULT_NAME : 
					currentModel.getFilePath().toString())
					+ " - " + APP_NAME);
			updateStats(currentModel);
		} else {
			saveFileAction.setEnabled(false);
			setTitle(APP_NAME);
			enableSelectionActions(false);
			resetStats();
		}
		enableDocumentActions(currentModel != null);
	}
	
	private void enableDocumentActions(boolean enable) {
		closeTabAction.setEnabled(enable);
		saveAsFileAction.setEnabled(enable);
		pasteAction.setEnabled(enable);
		statisticsAction.setEnabled(enable);
	}

	/**
	 * Updates status bar using information retrieved from given current
	 * document.
	 * 
	 * @param model
	 *        document used for retrieving information, it cannot be {@code null}
	 */
	private void updateStats(SingleDocumentModel model) {
		JTextComponent textComponent = model.getTextComponent();
		lengthLabel.setText(String.valueOf(
				textComponent.getText().length()));
		
		int p0 = textComponent.getSelectionStart();
        int p1 = textComponent.getSelectionEnd();
		enableSelectionActions(p0 != p1);
		Pair lineCol = getLineColumnNum(textComponent);
		lineLabel.setText(String.valueOf(lineCol.x1 + 1));
		columnLabel.setText(String.valueOf(lineCol.x2 + 1));
		selectionLabel.setText(String.valueOf(p1 - p0));
	}
	
	/**
	 * Returns {@link Pair} whose first value represents line number of a
	 * caret position and second value represents column number of a caret
	 * position.
	 * 
	 * @param textComponent
	 *        text component whose caret position is used
	 * @return {@link Pair} whose first value represents line number of a
	 *         caret position and second value represents column number of a caret
	 *         position
	 */
	private Pair getLineColumnNum(JTextComponent textComponent) {
		int caretPos = textComponent.getCaretPosition();
		Document doc = textComponent.getDocument();
		Element root = doc.getDefaultRootElement();
		int line = root.getElementIndex(caretPos);
		int col = caretPos - root.getElement(line).getStartOffset();
		return new Pair(line, col);
	}
	
	/**
	 * Sets all actions that use selected text as input enabled/disabled based
	 * on given boolean flag. If its {@code true} all actions will be enabled.
	 * 
	 * @param enable
	 *        tells whether actions should be enabled
	 */
	private void enableSelectionActions(boolean enable) {
		copyAction.setEnabled(enable);
		cutAction.setEnabled(enable);
		toUpperCaseAction.setEnabled(enable);
		toLowerCaseAction.setEnabled(enable);
		invertCaseAction.setEnabled(enable);
		ascSortAction.setEnabled(enable);
		descSortAction.setEnabled(enable);
		uniqueAction.setEnabled(enable);
	}
	
	/**
	 * Observes changes upon current document model.
	 */
	private final SingleDocumentListener docModifiedListener = new SingleDocumentListener() {
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			setCurrentDoc(model);
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			setTitle(model.getFilePath().toString() + " - " + APP_NAME);
		}
	};
	
	/**
	 * Observes changes upon {@link MultipleDocumentListener} used by this application.
	 */
	private final MultipleDocumentListener docChangedListener = new MultipleDocumentListener() {
		@Override
		public void documentRemoved(SingleDocumentModel removedModel) {}
		
		@Override
		public void documentAdded(SingleDocumentModel model) {
			model.getTextComponent().addCaretListener(e -> updateStats(model));
		}
		
		@Override
		public void currentDocumentChanged(SingleDocumentModel previousModel, 
				SingleDocumentModel currentModel) {
			if (previousModel == null && currentModel == null) {
				throw new IllegalArgumentException("Both previous and "
						+ "current model cannot be null!");
			}
			if (previousModel != null) {
				previousModel.removeSingleDocumentListener(docModifiedListener);
			}
			if (currentModel != null) {
				currentModel.addSingleDocumentListener(docModifiedListener);
			}
			setCurrentDoc(currentModel);
		}
		
		@Override
		public void wantCLoseDocument(SingleDocumentModel model) {
			closeFile(model);
		}
	};
	
	/**
	 * Resets all statistical information back to default values.
	 */
	private void resetStats() {
		lengthLabel.setText("-");
		lineLabel.setText("-");
		columnLabel.setText("-");
		selectionLabel.setText("-");
	}
	
	/**
	 * Observes the window closing event and before disposing checks whether
	 * there are modified documents and prompts user with appropriate message.
	 */
	private final WindowListener windowClosing = new WindowAdapter() {
    	@Override
    	public void windowClosing(WindowEvent e) {
    		for (SingleDocumentModel document: model) {
    			if (document.isModified()) {
    				model.activateDocument(document);
    				setCurrentDoc(document);
    				int res = FileUtils.showDialog(
    						JNotepadPP.this, 
    						flp.getString("saveMessage"),
    						flp.getString("exitTitle"),
    						JOptionPane.YES_NO_CANCEL_OPTION, 
    						JOptionPane.QUESTION_MESSAGE);
    				if (res == JOptionPane.CANCEL_OPTION) {
    					return;
    				} else if (res == JOptionPane.NO_OPTION) {
    					continue;
    				}
    				saveFile(document.getFilePath() == null, document);
    			}
    		}
    		running = false;
    		dispose();
    	}
	};
	
	/**
	 * Action used for creating new document.
	 */
	private final Action newFileAction = new LocalizableAction("new", flp) {
		private static final long serialVersionUID = -6821252397037245630L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};
	
	/**
	 * Action used for opening existing.
	 */
	private final Action openFileAction = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = -1395661784327032415L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Path path = selectPath(false);
			if (path == null) return;
			model.loadDocument(path);
		}
	};
	
	/**
	 * Action used for saving document.
	 */
	private final Action saveFileAction = new LocalizableAction("save", flp) {
		private static final long serialVersionUID = -3330931000709851504L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			saveFile(doc.getFilePath() == null, doc);
		}
	};
	
	/**
	 * Action used for saving as document.
	 */
	private final Action saveAsFileAction = new LocalizableAction("saveAs", flp) {
		private static final long serialVersionUID = 6366520240770468350L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveFile(true, model.getCurrentDocument());
		}
	};
	
	/**
	 * Action used for copying text in current document.
	 */
	private final Action copyAction = new LocalizableAction("copy", flp) {
		private static final long serialVersionUID = -8975951147484779117L;
		
		private Action copyAction = new DefaultEditorKit.CopyAction();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			copyAction.actionPerformed(e);
		}
	};
	
	/**
	 * Action used for cutting text in current document.
	 */
	private final Action cutAction = new LocalizableAction("cut", flp) {
		private static final long serialVersionUID = -4740193850619797021L;
		
		private Action cutAction = new DefaultEditorKit.CutAction();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			cutAction.actionPerformed(e);
		}
	};
	
	/**
	 * Action used for pasting text in current document.
	 */
	private final Action pasteAction = new LocalizableAction("paste", flp) {
		private static final long serialVersionUID = 2049096668919768676L;
		
		private Action pasteAction = new DefaultEditorKit.PasteAction();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			pasteAction.actionPerformed(e);
		}
	};
	
	/**
	 * Action used for closing tab.
	 */
	private final Action closeTabAction = new LocalizableAction("closeTab", flp) {
		private static final long serialVersionUID = 8561091307169087482L;

		@Override
		public void actionPerformed(ActionEvent e) {
			closeFile(model.getCurrentDocument());
		}
	};
	
	/**
	 * Action used for changing current language to English.
	 */
	private final Action enAction = new LocalizableAction("changeEn", flp) {
		private static final long serialVersionUID = -8632949666956604123L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	/**
	 * Action used for changing current language to Croatian.
	 */
	private final Action hrAction = new LocalizableAction("changeHr", flp) {
		private static final long serialVersionUID = -2192575453755819752L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	/**
	 * Action used for changing current language to German.
	 */
	private final Action deAction = new LocalizableAction("changeDe", flp) {
		private static final long serialVersionUID = 1039196291708262397L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	/**
	 * Action used for showing statistical information.
	 */
	private final Action statisticsAction = new LocalizableAction("statistics", flp) {
		private static final long serialVersionUID = -2320654903477302660L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			String text = textComponent.getText();
			int numberOfChar = text.length();
			int numberOfNonBlankChar = text.replaceAll("\\s+", "").length();
			int numberOfLines = text.split("\n").length;
			String message = flp.getString("numberOfChar") + numberOfChar + "\n" +
					flp.getString("numberOfNonBlankChar") + numberOfNonBlankChar + "\n" +
					flp.getString("numberOfLines") + numberOfLines;
			FileUtils.showDialog(
					JNotepadPP.this, 
					message, 
					flp.getString("statistics"),
					JOptionPane.DEFAULT_OPTION, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	/**
	 * Action used for exiting application.
	 */
	private final Action exitAction = new LocalizableAction("exit", flp) {
		private static final long serialVersionUID = 6366520240770468350L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispatchEvent(new WindowEvent(JNotepadPP.this, WindowEvent.WINDOW_CLOSING));
		}
	};
	
	/**
	 * Action used for setting selected text to uppercase.
	 */
	private final Action toUpperCaseAction = new LocalizableAction("toUpperCase", flp) {
		private static final long serialVersionUID = 1444795640905414262L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			caseSwitch(Character::toUpperCase, textComponent);
		}
	};
	
	/**
	 * Action used for setting selected text to lowercase.
	 */
	private final Action toLowerCaseAction = new LocalizableAction("toLowerCase", flp) {
		private static final long serialVersionUID = 2944907531926574005L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			caseSwitch(Character::toLowerCase, textComponent);
		}
	};
	
	/**
	 * Action used for inverting the case of selected text.
	 */
	private final Action invertCaseAction = new LocalizableAction("invertCase", flp) {
		private static final long serialVersionUID = 1444795640905414262L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			caseSwitch(c -> {
				if (Character.isUpperCase(c)) {
					return Character.toLowerCase(c);
				} else {
					return Character.toUpperCase(c);
				}
			}, textComponent);
		}
	};
	
	/**
	 * Action used for sorting selected lines in ascending order.
	 */
	private final Action ascSortAction = new LocalizableAction("ascSort", flp) {
		private static final long serialVersionUID = 1444795640905414262L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			linesAction(
					textComponent, 
					lines -> {
						Arrays.sort(lines, collator::compare);
						return lines;
					});
		}
	};
	
	/**
	 * Action used for sorting selected lines in descending order.
	 */
	private final Action descSortAction = new LocalizableAction("descSort", flp) {
		private static final long serialVersionUID = 1444795640905414262L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			linesAction(
					textComponent, 
					lines -> {
						Arrays.sort(lines, (s1, s2) -> collator.compare(s2, s1));
						return lines;
					});
		}
	};
	
	/**
	 * Action used for removing duplicate selected lines.
	 */
	private final Action uniqueAction = new LocalizableAction("unique", flp) {
		private static final long serialVersionUID = 1444795640905414262L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textComponent = model.getCurrentDocument().getTextComponent();
			linesAction(
					textComponent, 
					lines -> Arrays.stream(lines).distinct().toArray(String[]::new));
		}
	};
	
	/**
	 * Performs the given action upon selected lines. Selected lines are passed
	 * to the given function and replaced with the returned array of lines.
	 * 
	 * @param textComponent
	 *        text component whose text is to be replaced
	 * @param action
	 *        specified action to be performed
	 */
	private void linesAction(JTextComponent textComponent, Function<String[], String[]> action) {
		try {
			Document doc = textComponent.getDocument();
			Pair sep = getStartEndPosition(textComponent);
			String[] lines = doc.getText(sep.x1, sep.x2 - sep.x1 - 1).split("\n");
			lines = action.apply(lines);
			doc.remove(sep.x1, sep.x2 - sep.x1 - 1);
			doc.insertString(sep.x1, String.join("\n", lines), null);
		} catch (BadLocationException ignorable) {}
	}
	
	/**
	 * Returns {@link Pair} whose first value represents the starting index of replacement
	 * and second value represents ending index of replacement.
	 * 
	 * @param textComponent
	 *        text component whose text is to be replaced
	 * @return {@link Pair} whose first value represents the starting index of replacement
	 *         and second value represents ending index of replacement
	 */
	private Pair getStartEndPosition(JTextComponent textComponent) {
		// Start position of a selection
		int start = textComponent.getSelectionStart();
		// End position of a selection
		int end = textComponent.getSelectionEnd();
		
		Document doc = textComponent.getDocument();
		Element root = doc.getDefaultRootElement();
		
		// Start line to be sorted
		int startLine = root.getElementIndex(start);
		// End line to be sorted
		int endLine = root.getElementIndex(end);
		
		// Start index in document to be replaced
		int startReplace = root.getElement(startLine).getStartOffset();
		// End index in document to be replaced
		int endReplace = root.getElement(endLine).getEndOffset();
		
		return new Pair(startReplace, endReplace);
	}
	
	/**
	 * Switches case of every selected character based on a given transformation
	 * function.
	 * 
	 * @param transformation
	 *        function used to switch character case
	 * @param textComponent
	 *        component whose characters are replaced
	 */
	private void caseSwitch(Function<Character, Character> transformation, 
			JTextComponent textComponent) {
		StringBuilder result = new StringBuilder();
		for (char c: textComponent.getSelectedText().toCharArray()) {
			result.append(transformation.apply(c));
		}
		textComponent.replaceSelection(result.toString());
	}
	
	/**
	 * Saves the given document and asks for path if the given boolean flag is
	 * set to {@code true}.
	 * 
	 * @param askForPath
	 *        tells whether this method should ask for path before saving the document
	 * @param document
	 *        document to be saved
	 */
	private void saveFile(boolean askForPath, SingleDocumentModel document) {
		if (askForPath) {
			Path path = selectPath(true);
			if (path == null) return;
			model.saveDocument(document, path);
		} else {
			model.saveDocument(document, null);
		}
	}
	
	/**
	 * Prompts user for path and returns the selected path. If no path was selected
	 * {@code null} is returned.
	 * 
	 * @param save
	 *        tells whether path is used for saving or opening the document
	 * @return selected path or {@code null} if selection was canceled
	 */
	private Path selectPath(boolean save) {
		JFileChooser chooser = new JFileChooser();
		if ((save ? chooser.showSaveDialog(this) : 
			chooser.showOpenDialog(this)) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return chooser.getSelectedFile().toPath();
	}
	
	/**
	 * Closes the given document. If document was modified this method will
	 * first prompt the user.
	 * 
	 * @param document
	 *        document to be closed
	 */
	private void closeFile(SingleDocumentModel document) {
		if (document.isModified()) {
			int res = FileUtils.showDialog(
					this,
					flp.getString("saveMessage"),
					flp.getString("exitTitle"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (res == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (res == JOptionPane.YES_OPTION) {
				saveFile(document.getFilePath() == null, document);
			}
		}
		model.closeDocument(document);
	}
	
	/**
	 * Class that represents pair of two integers.
	 * 
	 * @author Filip Husnjak
	 */
	private static class Pair {
		
		/**
		 * First integer of this pair
		 */
		final int x1;
		
		/**
		 * Second integer of this pair
		 */
		final int x2;
		
		/**
		 * Constructs new {@link Pair} with specified integers.
		 * 
		 * @param x1
		 *        first integer of a pair
		 * @param x2
		 *        second integer of a pair
		 */
		public Pair(int x1, int x2) {
			this.x1 = x1;
			this.x2 = x2;
		}
		
	}
	
	/**
	 * Starts program and ignores all arguments. Creates new {@link JNotepadPP}
	 * and sets its visibility to {@code true}
	 * 
	 * @param ignored
	 */
	public static void main(String[] ignored) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

}
