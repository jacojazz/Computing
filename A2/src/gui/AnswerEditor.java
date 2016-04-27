package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class AnswerEditor {
	private static final String setString = "Save";
	static JFrame frame;

	public AnswerEditor(final int questionIndex) {
		JPanel pane = new JPanel(new SpringLayout());
		
		final JLabel questionLabel = new JLabel("Question:");
		pane.add(questionLabel);
		final JTextField question = new JTextField(15);
		question.setText(QuestionManager.getQuestion(questionIndex));
		question.setEditable(true);
		questionLabel.setLabelFor(question);
		pane.add(question);

		final JLabel answerLabel = new JLabel("Correct Answer:");
		pane.add(answerLabel);
		final JTextField answer = new JTextField(15);
		answer.setText(QuestionManager.getAnswer(questionIndex));
		answer.setEditable(true);
		answerLabel.setLabelFor(answer);
		pane.add(answer);

		final JLabel option1Label = new JLabel("Option 1:");
		pane.add(option1Label);
		final JTextField option1 = new JTextField(15);
		option1.setText(QuestionManager.getOptions(questionIndex).get(0));
		option1.setEditable(true);
		option1Label.setLabelFor(option1);
		pane.add(option1);

		final JLabel option2Label = new JLabel("Option 2:");
		pane.add(option2Label);
		final JTextField option2 = new JTextField(15);
		option2.setText(QuestionManager.getOptions(questionIndex).get(1));
		option2.setEditable(true);
		option2Label.setLabelFor(option2);
		pane.add(option2);

		final JLabel option3Label = new JLabel("Option 3:");
		pane.add(option3Label);
		final JTextField option3 = new JTextField(15);
		option3.setText(QuestionManager.getOptions(questionIndex).get(2));
		option3.setEditable(true);
		option3Label.setLabelFor(option3);
		pane.add(option3);

		SpringUtilities.makeCompactGrid(pane, 5, 2, 6, 6, 6, 6);

		JButton setButton = new JButton(setString);
		setButton.setActionCommand(setString);
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionManager.setQuestion(question.getText(), questionIndex);
				QuestionManager.setAnswer(answer.getText(), questionIndex);
				QuestionManager.setOptions(option1.getText(), option2.getText(), option3.getText(), questionIndex);
				QuestionManager.saveLists();
				QuestionEditor.runStringEditor(QuestionManager.getQuestions());
				frame.dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(setButton);

		JPanel basePanel = new JPanel();
		basePanel.add(pane, BorderLayout.NORTH);
		basePanel.add(buttonPanel, BorderLayout.SOUTH);
		basePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		createAndShowGUI(basePanel);
	}

	private static void createAndShowGUI(JPanel panel) {
		frame = new JFrame("Answer Editor");
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}