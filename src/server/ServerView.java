package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import protocole.Message;


public class ServerView extends JFrame implements ActionListener , WindowListener  {
    
	private static final long serialVersionUID = 1L;
	ServerSocketController sc;
	private JEditorPane messagesArea = new JEditorPane();
    private JList<String> ClientListe;
    private DefaultListModel<String> listClients = new DefaultListModel<String>();
    
    private JTabbedPane GeneralePane = new JTabbedPane();
    
    //-------------------onglait Messages----------------//
    private JPanel MessagePane = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane scrollMssageList = new JScrollPane();
    
    private JPanel Historique = new JPanel();
    private FlowLayout flowLayoutHisto = new FlowLayout();
    private JButton SaveHistorique = new JButton();
    private JButton SupHistorique = new JButton();
    private JButton LoadHistorique = new JButton();
    private String allMessage = "";
    
    //-------------------onglait Clients----------------//
    private JPanel ClientPane = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JScrollPane scrollClientList = new JScrollPane();
    
    private JPanel Moderater = new JPanel() ; 
    private BorderLayout flowLayoutMode = new BorderLayout();
    private JButton BanisheClient = new JButton();
    private JButton SendeMessageto = new JButton(); 
    private JTextField  MessageToSende = new JTextField(); 


    
    public ServerView(ServerSocketController asc ) {
        sc = asc;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addWindowListener(this);
    }

    private void jbInit() throws Exception {
        
        scrollMssageList.setViewportView(messagesArea);
        ClientListe= new JList<String>(listClients);
        scrollClientList.setViewportView(ClientListe);
        
        GeneralePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        MessagePane.setLayout(borderLayout1);
        ClientPane.setLayout(borderLayout2);
        Historique.setLayout(flowLayoutHisto);
        
        SaveHistorique.setText("Sauvegarder historique");
        SupHistorique.setText("Supprimer historique");
        LoadHistorique.setText("Charger historique");
        SaveHistorique.addActionListener(this);
        SupHistorique.addActionListener(this);
        LoadHistorique.addActionListener(this);
        
        Historique.setBorder(BorderFactory.createTitledBorder("Gestion de l'historique"));
        Historique.add(LoadHistorique);
        Historique.add(SaveHistorique);
        Historique.add(SupHistorique);  
        
        messagesArea.setEditable(false);
	    HTMLEditorKit kit = new HTMLEditorKit();
	    messagesArea.setEditorKit(kit);
	    Document doc = kit.createDefaultDocument();
	    messagesArea.setDocument(doc);
	    DefaultCaret caret = (DefaultCaret)messagesArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollMssageList.setBorder(BorderFactory.createTitledBorder("Historique des messages"));
        MessagePane.add(scrollMssageList, BorderLayout.CENTER);
        MessagePane.add(Historique, BorderLayout.SOUTH);
        
        BanisheClient.setText("Banire Client");
        SendeMessageto.setText("Envoyer un message");
        BanisheClient.addActionListener(this);
        SendeMessageto.addActionListener(this);

        
        
        Moderater.setBorder(BorderFactory.createTitledBorder("Modération"));
        Moderater.setLayout(flowLayoutMode);
        Moderater.add(BanisheClient,BorderLayout.WEST);
        Moderater.add(SendeMessageto, BorderLayout.EAST);
        Moderater.add(MessageToSende,BorderLayout.CENTER);
        
        scrollClientList.setBorder(BorderFactory.createTitledBorder("Clients actuellement connectés"));
        ClientPane.add(scrollClientList, BorderLayout.CENTER);
        ClientPane.add(Moderater,BorderLayout.SOUTH);
        GeneralePane.addTab("Messages", MessagePane);
        
        GeneralePane.addTab("Clients", ClientPane);
        
        this.getContentPane().add(GeneralePane, null);
        this.setSize(800, 500);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == SaveHistorique ) {
        	FileDialog boiteSauver = new FileDialog(this, "Choix de l'emplacement",FileDialog.SAVE);
        	boiteSauver.setFile(".srv");
        	boiteSauver.setVisible(true);
            sc.saveHistorique(boiteSauver.getDirectory() + boiteSauver.getFile());
        } else if (e.getSource() == LoadHistorique ){
        	FileDialog fileDialog = new FileDialog(this, "Choix du fichier", FileDialog.LOAD);
            fileDialog.setFile(".srv");
            fileDialog.setVisible(true);
            eraseAllMessages();
            sc.restorHistorique(fileDialog.getDirectory() + fileDialog.getFile());
        } else if (e.getSource() == SupHistorique ){
            eraseAllMessages();
            sc.eraseHistorique();
        } else if(e.getSource() == BanisheClient & !ClientListe.isSelectionEmpty()){
            sc.BanishClient((String)ClientListe.getSelectedValue());
        }else if (e.getSource() == SendeMessageto & !ClientListe.isSelectionEmpty()){
            sc.Send(new Message("Moderateur",(String)ClientListe.getSelectedValue(),MessageToSende.getText(),Color.BLACK));
            System.out.print(MessageToSende.getText());
        }
    }
    
    /**
     * ajoute et affiche un message dans la liste des messages affichée 
     * @param m 
     *          nouveau message à afficher
     */
    
    public void AddMessage(Message m) {
		allMessage += "<span style=\"color : rgb(" + m.GetColor().getRed() + "," + m.GetColor().getGreen() + "," + m.GetColor().getBlue() + ")\">" + m.GetExpediteur() + " > " + m.GetDestinataire() + " : " + m.GetMessage() + "</span><br>";
		messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
		messagesArea.setText(allMessage);
	}

    /**
     * Supprime tout les messages dans la liste des messages affichée
     */
    public void eraseAllMessages(){
    	allMessage = "";
    	messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
    	messagesArea.setText(allMessage);
    }
    
    /**
     * ajoute et affiche un client dans la liste des clients affichée.
     * @param client
     * nouveau client à afficher
     */
    public void addClient(String client){
        listClients.addElement(client);
    }

    /**
     * supprime un client de la liste des clients affichée.
     * @param client
     *          client à supprimer 
     */
    public void supClient(String client){
    	listClients.removeElement(client);
    }

    /**
     * propose d'enregistrer le contenue de la conversation avant de fermer 
     * @param e 
     *      WindowEvent
     */
    public void windowClosing(WindowEvent e) {
    	sc.DeconnectAll();
	    int anser = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder l'historique?");
        if (anser == JOptionPane.YES_OPTION){
        	FileDialog boiteSauver = new FileDialog(this, "Choix de l emplacement",FileDialog.SAVE);
        	boiteSauver.setFile(".srv");
        	boiteSauver.setVisible(true);
            sc.saveHistorique(boiteSauver.getDirectory() + boiteSauver.getFile());
            System.exit(0);
            
        }else
        	{ if(anser == JOptionPane.NO_OPTION){
                System.exit(0);
            }
        }
    }

	public void windowClosed(WindowEvent e) {
		
	}

	public void windowOpened(WindowEvent e) {
		
	}

	public void windowIconified(WindowEvent e) {
		
	}

	public void windowDeiconified(WindowEvent e) {
		
	}

	public void windowActivated(WindowEvent e) {
		
	}

	public void windowDeactivated(WindowEvent e) {
		
	}
}
