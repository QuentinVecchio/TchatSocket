package server;

import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import javax.swing.JTextField;

import protocole.Message;

//import oracle.jdeveloper.layout.PaneConstraints;
//import oracle.jdeveloper.layout.PaneLayout;

public class ServerView extends JFrame implements ActionListener , WindowListener  {
    
    ServerSocketController sc;

    Vector<String> Clients = new Vector<String>();
    Vector<String> Message = new Vector<String>();
    private JList MessageListe;
    private JList ClientListe;
    
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
        

        MessageListe= new JList(Message);
        scrollMssageList.setViewportView(MessageListe);
        ClientListe= new JList(Clients);
        scrollClientList.setViewportView(ClientListe);
        
        GeneralePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        MessagePane.setLayout(borderLayout1);
        ClientPane.setLayout(borderLayout2);
        Historique.setLayout(flowLayoutHisto);
        
        SaveHistorique.setText("Save Historique");
        SupHistorique.setText("Sup Historique");
        LoadHistorique.setText("Charger Historique");
        SaveHistorique.addActionListener(this);
        SupHistorique.addActionListener(this);
        LoadHistorique.addActionListener(this);
        
        Historique.setBorder(BorderFactory.createTitledBorder("Gsetion de l'historique"));
        Historique.add(LoadHistorique);
        Historique.add(SaveHistorique);
        Historique.add(SupHistorique);  
        
        scrollMssageList.setBorder(BorderFactory.createTitledBorder("Clientes acctuelement connecte"));
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
        
        scrollClientList.setBorder(BorderFactory.createTitledBorder("Clientes acctuelement connecte"));
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
            sc.saveHistorique();
            System.out.println("Save");
        }else
        if (e.getSource() == LoadHistorique ){
            eraseAllMessages();
            sc.restorHistorique();
        }else
        if (e.getSource() == SupHistorique ){
            eraseAllMessages();
            sc.eraseHistorique();
        }else
        if(e.getSource() == BanisheClient & !ClientListe.isSelectionEmpty()){
            sc.BanishClient((String)ClientListe.getSelectedValue());
        }else
        if (e.getSource() == SendeMessageto & !ClientListe.isSelectionEmpty()){
            sc.Send(new Message("Moderateur",(String)ClientListe.getSelectedValue(),MessageToSende.getText(),Color.BLACK));
            System.out.print(MessageToSende.getText());
        }
}
    public void addMessage(String message){
        Message.add(message);
        MessageListe.setListData(Message);
    }
    public void eraseAllMessages(){
        Message.clear();
        MessageListe.setListData(Message);
    }
    public void addClient(String client){
        Clients.add(client);
        ClientListe.setListData(Clients);
    }
    public void supClient(String client){
        Clients.remove(client);
        ClientListe.setListData(Clients);
    }
	public void windowClosing(WindowEvent e) {
	    int anser = JOptionPane.showConfirmDialog(null,"voulez vous sovgarder l'historique?");
        if (anser == JOptionPane.YES_OPTION){
            sc.saveHistorique();
            System.exit(0);
            
        }else{
            if(anser == JOptionPane.NO_OPTION){
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
