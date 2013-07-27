package com.asilane.gui;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.asilane.core.Asilane;

/**
 * @author walane
 * 
 */
public class GUI {

	private JFrame frmAsilane;
	private final Asilane asilane;
	private JComboBox<String> languageComboBox;
	private JTextArea textArea;
	private JButton btnRecord;
	private JTextField textField;

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
		btnRecord.setBounds(227, 101, 147, 36);
		btnRecord.addActionListener(new Controller(this));
		frmAsilane.getContentPane().add(btnRecord);

		languageComboBox = new JComboBox<String>();
		languageComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "French", "English" }));
		languageComboBox.setBounds(443, 420, 125, 24);
		frmAsilane.getContentPane().add(languageComboBox);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(12, 171, 574, 212);
		textArea.setLineWrap(true);
		frmAsilane.getContentPane().add(textArea);
		textArea.setColumns(10);

		textField = new JTextField();
		textField.setBounds(23, 423, 174, 19);
		textField.addActionListener(new Controller(this));
		frmAsilane.getContentPane().add(textField);
		textField.setColumns(10);
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
	 * @return the languageComboBox
	 */
	public JComboBox<String> getLanguageComboBox() {
		return languageComboBox;
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

}