package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import protocole.Message;

public class ClientView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel pseudoLabel = new JLabel("Pseudo : ");
	private JLabel hostLabel = new JLabel("Host : ");
	private JLabel portLabel = new JLabel("Port : ");
	private JLabel pseudoLabel2 = new JLabel("");
	private JLabel hostLabel2 = new JLabel("");
	private JLabel portLabel2 = new JLabel("");
	private JButton deconnectionButton = new JButton();
	private JList<String> clientsList;
	private JEditorPane messagesArea = new JEditorPane();
	private JButton sendButton = new JButton("");
	private JTextField messageTextField = new JTextField("");
	private DefaultListModel<String> listClients;
	private ClientSocketController c;
	private String allMessage = "";
	
	public ClientView(ClientSocketController c) {
		this.c = c;
		this.setSize(800, 600);
		this.setTitle("Tchat - Connected");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		BuildGUILeft();
		BuildGUINorth();
		BuildGUISouth();
		BuildGUICenter();
	}
  
	public void Affiche() {
		this.setVisible(true);
		messageTextField.requestFocus();
	}
	
	public void Exit() {
		this.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == deconnectionButton) {
			c.Disconnection();		
		} else if (e.getSource() == sendButton) {
			Message m = new Message(c.GetName(), "all", messageTextField.getText(),c.GetColor());
			c.Send(m);
			messageTextField.setText("");
			messageTextField.requestFocus();
		}
	}
	
	public void AddClient(String name) {
		listClients.addElement(name);
	}
	
	public void DeleteClient(String client) {
		listClients.removeElement(client);
	}
	
	public void AddMessage(Message m) {
		allMessage += "<span style=\"color : rgb(" + m.GetColor().getRed() + "," + m.GetColor().getGreen() + "," + m.GetColor().getBlue() + ")\">" + m.GetExpediteur() + " > " + m.GetDestinataire() + " : " + m.GetMessage() + "</<span></br>";
		messagesArea.setText(allMessage);
	}
	
	private void BuildGUILeft() {
		JPanel panelLeft = new JPanel();
		listClients = new DefaultListModel<String>();
		clientsList = new JList<String>(listClients);
		clientsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		clientsList.setLayoutOrientation(JList.VERTICAL);
		clientsList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(clientsList);
		listScroller.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		panelLeft.add(listScroller);
		clientsList.setPreferredSize(new Dimension(170,450));
		this.add(panelLeft, BorderLayout.WEST);
	}
	
	private void BuildGUINorth() {
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		panelNorth.add(pseudoLabel);
		gbc.gridx = 1;
		panelNorth.add(pseudoLabel2);
		gbc.gridx = 2;
		panelNorth.add(hostLabel);
		gbc.gridx = 3;
		panelNorth.add(hostLabel2);
		gbc.gridx = 4;
		panelNorth.add(portLabel);
		gbc.gridx = 5;
		panelNorth.add(portLabel2);
		gbc.gridx = 6;
		panelNorth.add(deconnectionButton);
		pseudoLabel.setPreferredSize(new Dimension(100,30));
		hostLabel.setPreferredSize(new Dimension(100,30));
		portLabel.setPreferredSize(new Dimension(100,30));
		pseudoLabel2.setPreferredSize(new Dimension(100,30));
		hostLabel2.setPreferredSize(new Dimension(100,30));
		portLabel2.setPreferredSize(new Dimension(100,30));
		pseudoLabel2.setText(c.GetName());
		hostLabel2.setText(c.GetHost());
		portLabel2.setText(c.GetPort());
		deconnectionButton.addActionListener(this);
		deconnectionButton.setPreferredSize(new Dimension(120,30));
		this.add(panelNorth, BorderLayout.NORTH);
		deconnectionButton.setText("Disconnection");
	}
	
	private void BuildGUISouth() {
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		messageTextField.setPreferredSize(new Dimension(730,30));
		sendButton.setPreferredSize(new Dimension(60,30));
		sendButton.addActionListener(this);
		panelSouth.add(messageTextField,gbc);
		gbc.gridx = 3;
		panelSouth.add(sendButton,gbc);
		this.add(panelSouth, BorderLayout.SOUTH);
		sendButton.setText("Send");
	}
	
	private void BuildGUICenter() {
		JPanel panelCenter = new JPanel();
	    messagesArea.setEditable(false);
	    HTMLEditorKit kit = new HTMLEditorKit();
	    messagesArea.setEditorKit(kit);
	    Document doc = kit.createDefaultDocument();
	    messagesArea.setDocument(doc);
	    this.add(panelCenter, BorderLayout.CENTER);
	    messagesArea.setPreferredSize(new Dimension(570,500));
		JScrollPane scroll = new JScrollPane( messagesArea );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    panelCenter.add(scroll);
	}
}