package Client;

import Client.WordModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonListener;

public class ClientView {
	//private ArrayList<ActionListener> listeners = null;
	private JFrame frmDictionaryClient;
	private JTextField word;
	private JTextField meaning;
	private JTextPane message;
	private JButton add = new JButton("Add Word");
	private 	JButton query = new JButton("Query Word");
	private 	JButton remove = new JButton("Remove Word");
	private JButton more = new JButton("More Meaning");
	private JButton clear = new JButton("Clear All");
	
	public String getTextWord() {
		return word.getText();
	}
	
	public void setTextWord() {
		word.setText("");
	}
	
	public String getTextMeaning() {
		return meaning.getText();
	}
	
    public void setTextMeaning(String text){
    		meaning.setText(text);
    }
    
    public void setTextMessage(String text) {
    		message.setText(text);
    }
    
	public ClientView() {
		initialize();
	}

	private void initialize() {
		frmDictionaryClient = new JFrame();
		frmDictionaryClient.setTitle("Dictionary Client");
		frmDictionaryClient.setBounds(100, 100, 450, 300);
		frmDictionaryClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		frmDictionaryClient.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		frmDictionaryClient.getContentPane().add(panel_1, gbc_panel_1);
		
		JLabel lblWelcomeToThe = new JLabel("Welcome to the Dictionary");
		lblWelcomeToThe.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblWelcomeToThe.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblWelcomeToThe);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		frmDictionaryClient.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		word = new JTextField();
		word.setHorizontalAlignment(SwingConstants.CENTER);
		word.setToolTipText("Please input the word here.");
		panel_2.add(word, BorderLayout.NORTH);
		word.setColumns(10);
		
		meaning = new JTextField();
		meaning.setToolTipText("This field can be edited.");
		meaning.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(meaning, BorderLayout.CENTER);
		meaning.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 1;
		frmDictionaryClient.getContentPane().add(panel_3, gbc_panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		query.setToolTipText("Query Button");
		query.setBackground(Color.WHITE);
		panel_3.add(query, BorderLayout.NORTH);
		
		/*
		query.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	for(int i=0;i<listeners.size();++i){
                    listeners.get(i).actionPerformed(e);
                }
            	System.out.println("Button: success");
            }
        });
        */
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		add.setToolTipText("Add Button");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_4.add(add, BorderLayout.NORTH);
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		more.setToolTipText("Continue Add Button");
		more.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_5.add(more, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel_5.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		remove.setToolTipText("Remove Button");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(remove, BorderLayout.NORTH);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		message = new JTextPane();
		message.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_6.add(message, BorderLayout.CENTER);
		message.setAlignmentY(Component.TOP_ALIGNMENT);
		
		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.setToolTipText("Clear Button");
		panel_6.add(btnClearAll, BorderLayout.NORTH);

		ClientController listener = new ClientController(this, new WordModel());
		query.addActionListener(listener);
		add.addActionListener(listener);
		remove.addActionListener(listener);
		more.addActionListener(listener);
		clear.addActionListener(listener);
		
		frmDictionaryClient.setVisible(true);
	}

	public JFrame getFrmDictionaryClient() {
		return frmDictionaryClient;
	}

	public void setFrmDictionaryClient(JFrame frmDictionaryClient) {
		this.frmDictionaryClient = frmDictionaryClient;
	}

	public JTextField getWord() {
		return word;
	}

	public void setWord(JTextField word) {
		this.word = word;
	}

	public JTextField getMeaning() {
		return meaning;
	}

	public void setMeaning(JTextField meaning) {
		this.meaning = meaning;
	}

	public JTextPane getMessage() {
		return message;
	}

	public void setMessage(JTextPane message) {
		this.message = message;
	}

	public JButton getAdd() {
		return add;
	}

	public void setAdd(JButton add) {
		this.add = add;
	}

	public JButton getQuery() {
		return query;
	}

	public void setQuery(JButton query) {
		this.query = query;
	}

	public JButton getRemove() {
		return remove;
	}

	public void setRemove(JButton remove) {
		this.remove = remove;
	}

	public JButton getMore() {
		return more;
	}

	public void setMore(JButton more) {
		this.more = more;
	}

	public JButton getClear() {
		return clear;
	}

	public void setClear(JButton clear) {
		this.clear = clear;
	}
	
}
