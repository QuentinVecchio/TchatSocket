package server;

import java.awt.Color;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.LinkedList;
import protocole.Message;

public class ServerSocketController {
	public ServerSocket serverSocket;
	public String link = null;
	private ServerCreatView scv;
	private ServerView sv;
	private ServerSocketThread thread;
	private LinkedList<ClientInformation> clients = new LinkedList<ClientInformation>();
    private LinkedList<Message> messages = new LinkedList<Message>();
    private LinkedList<String> PseudoForbiden = new LinkedList<String>();
		
    /**
	 * Méthode qui lance la fenetre de lancement du serveur
     *
     */
	public void start() {
    	scv = new ServerCreatView(this);
        scv.setVisible(true);
    }
	
	/**
	 * Méthode qui lance la fenetre de gestion du serveur
     *
     */
    public void run (){
        sv = new ServerView(this);
    }
    
    /**
	 * Méthode qui initialise le serveur
     * @param port : port du serveur
     */
    public void InitServeur(int port) {
        try {
        	serverSocket = new ServerSocket(port);
            scv.Exit();
            thread = new ServerSocketThread(this);
            thread.start();
            System.out.println("Server ready");
        } catch (Exception e) {
        	scv.ErrorPort();
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }       
    }
    
    /**
	 * Méthode qui enregistre un client
     *	@param c : nom du client
     *	@param socket : socket cliente
     */
    public void Register(String c, ClientThread socket) {
    	String[] parts = c.split(";");
    	if(NameCanBeUse(parts[1])) {
    		socket.AlreadyUse();
    	} else {
    		socket.ConnectionAuthorized();
    		ClientInformation client =  new ClientInformation(parts[1], parts[2], socket);
	    	System.out.println("Le client <" + parts[1] + "> est connecté");
	    	clients.add(client);
	    	String[] rgb = parts[2].split("_");
	    	Color color = Color.BLACK;
	    	if(rgb.length == 3) {
	    		color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
	    	}
	    	Message m = new Message(parts[1], "all", parts[1] + " est connecté", color);
            for(Message ms: messages) {
	    		socket.Send(ms);
	    	}
	    	for(ClientInformation cl: clients) {
	    		cl.GetSocket().Send(m);
	    		if(cl.GetNom().equals(parts[1]) == false) {
	    			cl.GetSocket().AddClient(parts[1]);
	    		}
	    	}
	    	messages.add(m);
                sv.AddMessage(m);
                sv.addClient(client.GetNom());
                for(ClientInformation cl: clients) {
	    		socket.AddClient(cl.GetNom());
	    	}
    	}
    }
    
    /**
	 * Méthode qui deconnecte  un client
     *	@param name : nom du client
     */
    public void Disconnection(String name) {
        sv.supClient(name);
    	ClientInformation client = SearchClient(name);
    	if(client != null) {
    		clients.remove(client);
	    	String[] rgb = client.GetColor().split(";");
	    	Color color = Color.BLACK;
	    	if(rgb.length == 3) {
	    		color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
	    	}
	    	Message m = new Message(client.GetNom(), "all", client.GetNom() + " est déconnecté", color);
	    	messages.add(m);
            sv.AddMessage(m);
            for(ClientInformation c :clients) {
	    		System.out.println(m.toString());
	    		c.GetSocket().Send(m);
	    		c.GetSocket().DeleteClient(name);;
	    	}
	    	client.GetSocket().interrupt();
    	}
    }
    
    /**
	 * Méthode qui envoie  un message
     *	@param message : message a envoyé
     */
    public void Send(Message message) {	
        if (NameExist(message.GetExpediteur())){
            messages.add(message);
            sv.AddMessage(message);
            for(ClientInformation c : clients) {
                if(message.GetDestinataire().equals("all")) {
                	c.GetSocket().Send(message);
	           	} else {
	           		if (message.GetDestinataire().equals(c.GetNom())  || message.GetExpediteur().equals(c.GetNom())){
	           			c.GetSocket().Send(message);
	           		}
	           	}
            }
        }
    }
    
    /**
   	 * Méthode qui deconnecte tous clients
     *
     */
    public void DeconnectAll() {
    	for(ClientInformation c :clients) {
    		c.GetSocket().Shutdown();
    	}
    }
    
    /**
   	 * Méthode qui banni un client
     * @param name : nom du client
     */
    public void BanishClient(String name){
        ClientInformation toBanish = SearchClient(name);
        Send(new Message("Moderateur", toBanish.GetNom(), "Vous avez été bannie", Color.BLACK));
        Disconnection(toBanish.GetNom());
            
        PseudoForbiden.add(name);
    }

    /**
   	 * Méthode qui recherche si un nom de client existe
     * @param name : nom du client
     * @return true si le nom existe, false sinon
     */
    private boolean NameExist(String name) {
        for(ClientInformation c :clients) {
	    	if(c.GetNom().equals(name)) {
	    		return true;
	   		}
    	}
        if (name.equals("Moderateur"))
        {
            return true ; 
        }
    	return false;
    }
    
    /**
   	 * Méthode qui recherche si un nom de client peut etre utilisé
     * @param name : nom du client
     * @return true si le nom peut etre utilisé, false sinon
     */
    private boolean NameCanBeUse(String name) {
        for(ClientInformation c :clients) {
            if(c.GetNom().equals(name)) {
                    return true;
            }
        }
        for (String s :PseudoForbiden){
            if(s.equals(name)) {
                    return true;
            }
        }
        return false;
    }
    
    /**
   	 * Méthode qui recherche un client
     * @param name : nom du client
     * @return le client s'il existe, null sinon
     */
    private ClientInformation SearchClient(String name) {
        for(ClientInformation c : clients) {
    		if(c.GetNom().equals(name)) {
	    			return c;
	    	}
    	}
    	return null;
    }
    
    /**
     * Sauvegarde l'historique.
     * @param path : lien vers l'historique
     */
    public void saveHistorique(String path){
    	ObjectOutputStream oos = null;
        try {
        	link = path;
		 	final FileOutputStream fichier = new FileOutputStream(path);
		 	oos = new ObjectOutputStream(fichier);
		 	Historique history = new Historique(messages.size(), PseudoForbiden.size(), messages, PseudoForbiden);
		 	oos.writeObject(history);
		 	oos.flush();
		 	oos.close();
        } catch (final java.io.IOException e) {
        	e.printStackTrace();
        }
    }

    /**
     * charge l'historique préalablement sauvegarder
     * @param path : lien vers l'historique
     */
    public void restorHistorique(String path){
        ObjectInputStream ois = null;
        try {
            final FileInputStream fichier = new FileInputStream(path);
            link = path;
            ois = new ObjectInputStream(fichier);
            Historique history = (Historique)ois.readObject();
            Message mess = new Message("Server","all","Changement d'historique", Color.RED);
            for(ClientInformation c : clients)
            {
            	c.GetSocket().Send(mess);
            }
            messages = history.getMessages();
            sv.eraseAllMessages();
            for (Message m: history.getMessages()){
            	sv.AddMessage(m);
            	for(ClientInformation c : clients)
                {
            		c.GetSocket().Send(mess);
                }
            }
            PseudoForbiden = history.getPseudoForbidden();
        } catch (final java.io.IOException e) {
        	e.printStackTrace();
        } catch (final ClassNotFoundException e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if (ois != null) {
        			ois.close();
        		}
        	} catch (final IOException ex) {
        		ex.printStackTrace();
        	}
        }
    }
       
    public void eraseHistorique(){	
    	messages.remove();
    	PseudoForbiden.remove();
    }
}

