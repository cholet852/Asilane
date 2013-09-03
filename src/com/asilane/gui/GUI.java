package com.asilane.gui;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import com.asilane.core.Asilane;

/**
 * @author walane
 * 
 */
public class GUI {

	private JFrame frmAsilane;
	private final Asilane asilane;
	private JComboBox<String> LocaleComboBox;
	private JTextArea textArea;
	private JButton btnRecord;
	private JTextField textField;
	private JLabel lblPosezUneQuestion;

	/**
	 * Create the application.
	 */
	public GUI(final Asilane asilane) {
		this.asilane = asilane;
		initialize();
		frmAsilane.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAsilane = new JFrame();
		frmAsilane.setTitle("Asilane");
		frmAsilane.setResizable(false);
		frmAsilane.setBounds(100, 100, 600, 500);
		frmAsilane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAsilane.getContentPane().setLayout(null);

		final JLabel lblAsilane = new JLabel("Asilane");
		lblAsilane.setHorizontalAlignment(SwingConstants.CENTER);
		lblAsilane.setFont(new Font("Arial", Font.BOLD, 17));
		lblAsilane.setBounds(258, 12, 70, 15);
		frmAsilane.getContentPane().add(lblAsilane);

		btnRecord = new JButton("Record");
		btnRecord.setBounds(224, 51, 147, 36);
		btnRecord.addActionListener(new Controller(this));
		frmAsilane.getContentPane().add(btnRecord);

		LocaleComboBox = new JComboBox<String>();
		LocaleComboBox.addActionListener(new ComboBoxController(this));
		LocaleComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "French", "English" }));
		LocaleComboBox.setBounds(443, 420, 125, 24);
		frmAsilane.getContentPane().add(LocaleComboBox);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setColumns(10);

		// Disable auto scrolling
		final DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		final JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 171, 574, 212);
		frmAsilane.getContentPane().add(scrollPane);

		textField = new JTextField();
		textField.setBounds(393, 113, 193, 19);
		textField.addActionListener(new Controller(this));
		frmAsilane.getContentPane().add(textField);
		textField.setColumns(10);

		lblPosezUneQuestion = new JLabel("Posez une question à l'écrit ou appuyez sur Record :");
		lblPosezUneQuestion.setBounds(12, 115, 375, 15);
		frmAsilane.getContentPane().add(lblPosezUneQuestion);
	}

	/**
	 * @return the lblResponse
	 */
	public JTextArea getTextFieldResponse() {
		return textArea;
	}

	/**
	 * @return the asilane
	 */
	public Asilane getAsilane() {
		return asilane;
	}

	/**
	 * @return the LocaleComboBox
	 */
	public JComboBox<String> getLocaleComboBox() {
		return LocaleComboBox;
	}

	/**
	 * @return the btnRecord
	 */
	public JButton getBtnRecord() {
		return btnRecord;
	}

	/**
	 * @return the textField
	 */
	public JTextField getTextField() {
		return textField;
	}

	/**
	 * @return the lblPosezUneQuestion
	 */
	public JLabel getManualTypeLabel() {
		return lblPosezUneQuestion;
	}
}