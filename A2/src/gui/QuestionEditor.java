package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuestionEditor extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private JList<String> list;

	private static final String addString = "Add";
	private static final String deleteString = "Delete";
	private JButton deleteButton;
	private JTextField question;
	final static JFrame frame = new JFrame("Question Editor");
	static DefaultListModel<String> listModel = new DefaultListModel<String>();
	static List<List<String>> questionArray = QuestionManager.getQuestionArray();

	public QuestionEditor(List<String> aR) {
		super(new BorderLayout());
		clearList();
		setupList(QuestionManager.getQuestions());

		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(15);
		addActionListeners();
		JScrollPane listScrollPane = new JScrollPane(list);

		JButton addButton = new JButton(addString);
		AddListener addListener = new AddListener(addButton);
		addButton.setActionCommand(addString);
		addButton.addActionListener(addListener);
		addButton.setEnabled(false);

		deleteButton = new JButton(deleteString);
		deleteButton.setActionCommand(deleteString);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				list.remove(index);

				List<String> convertedList = new ArrayList<String>();
				for (Object arrayData : listModel.toArray()) {
					convertedList.add((String) arrayData);
				}

				QuestionManager.setQuestions(convertedList);
				QuestionManager.saveLists();
			}
		});

		question = new JTextField(30);
		question.addActionListener(addListener);
		question.getDocument().addDocumentListener(addListener);
		@SuppressWarnings("unused")
		String name = listModel.getElementAt(list.getSelectedIndex()).toString();

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(deleteButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(question);
		buttonPane.add(addButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	}

	private void setupList(List<String> stringList) {
		for (String listData : stringList) {
			listModel.addElement(listData);
		}
	}

	private void addActionListeners() {
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					String selectedQuestion = listModel.get(list.getSelectedIndex());
					for (int i = 0; i < questionArray.size(); i++) {
						List<String> currentList = questionArray.get(i);
						if (currentList.get(0).equals(selectedQuestion)) {
							new AnswerEditor(i);
							frame.dispose();
						}
					}
				}
			}
		});
	}

	private void clearList() {
		listModel.clear();
	}

	class AddListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public AddListener(JButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			String name = question.getText();

			if (name.equals("") || alreadyInList(name)) {
				Toolkit.getDefaultToolkit().beep();
				question.requestFocusInWindow();
				question.selectAll();
				return;
			}

			listModel.addElement(question.getText());

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() >= 2) {
						String selectedQuestion = listModel.get(list.getSelectedIndex());
						for (int i = 0; i < questionArray.size(); i++) {
							List<String> currentList = questionArray.get(i);
							if (currentList.get(0).equals(selectedQuestion)) {
								new AnswerEditor(i);
							}
						}
					}
				}
			});

			question.requestFocusInWindow();
			question.setText("");

			List<String> convertedList = new ArrayList<String>();
			for (Object arrayData : listModel.toArray()) {
				convertedList.add((String) arrayData);
			}

			QuestionManager.setQuestions(convertedList);
			QuestionManager.setOptions("Please Enter", "Please Enter", "Please Enter", listModel.getSize() - 1);
			QuestionManager.setAnswer("Please Enter", listModel.getSize() - 1);
			QuestionManager.saveLists();

			list.setSelectedIndex(0);
			list.ensureIndexIsVisible(0);
		}

		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
		}

		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				deleteButton.setEnabled(false);

			} else {
				deleteButton.setEnabled(true);
			}
		}
	}

	private static void createAndShowGUI(List<String> aR) {
		frame.setLocationRelativeTo(null);
		JComponent newContentPane = new QuestionEditor(aR);
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				frame.dispose();
			}
		});
	}

	public static void runStringEditor(final List<String> aR) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(aR);
			}
		});
	}
}