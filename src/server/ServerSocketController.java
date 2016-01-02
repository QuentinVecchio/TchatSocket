package server;

import java.awt.Color;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Vector;

import protocole.Message;

public class ServerSocketController {
            public ServerSocket serverSocket; 
	    private ServerCreatView scv;
	    private ServerView sv;
	    private ServerSocketThread thread;
	    private Vector<ClientInformation> clients = new Vector<ClientInformation>();
            private Vector<Message> messages = new Vector<Message>();
            private Vector<String> PsudoForbiden = new Vector<String>();
		
	public void start() {
    	scv = new ServerCreatView(this);
        scv.setVisible(true);
    }
    public void run (){
        sv = new ServerView(this);
    }
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
                sv.addMessage(m.toAffiche());
                sv.addClient(client.GetNom());
                for(ClientInformation cl: clients) {
	    		socket.AddClient(cl.GetNom());
	    	}
    	}
    }
    
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
                sv.addMessage(m.toAffiche());
            for(ClientInformation c :clients) {
	    		System.out.println(m.toString());
	    		c.GetSocket().Send(m);
	    		c.GetSocket().DeleteClient(name);;
	    	}
	    	client.GetSocket().interrupt();
    	}
    }
    
    public void Send(Message message) {	
        if (NameExist(message.GetExpediteur())){
            messages.add(message);
            sv.addMessage(message.toAffiche());
            for(ClientInformation c : clients) {
                if(c.GetNom().equals(message.GetDestinataire())
                    ||
                    c.GetNom().equals(message.GetExpediteur())
                    ||
                   message.GetDestinataire().equals("all")){
                       c.GetSocket().Send(message);   
                   }
            }
        }
    }
    public void BanishClient(String name){
        ClientInformation toBanish = SearchClient(name);
        Send(new Message("Moderateur", toBanish.GetNom(), "Vous avez �t� bannie", Color.BLACK));
        Disconnection(toBanish.GetNom());
            
        PsudoForbiden.add(name);
    }

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
    private boolean NameCanBeUse(String name) {
        for(ClientInformation c :clients) {
            if(c.GetNom().equals(name)) {
                    return true;
            }
        }
        for (String s :PsudoForbiden){
            if(s.equals(name)) {
                    return true;
            }
        }
        return false;
    }
    private ClientInformation SearchClient(String name) {
    	for(int i=0;i<clients.size();i++) {
    		if(clients.get(i).GetNom().equals(name)) {
	    			return clients.get(i);
	    	}
    	}
    	return null;
    }
    public void saveHistorique(){
        ObjectOutputStream oos = null;
            try {
              final FileOutputStream fichier = new FileOutputStream("donnees.ser");
              oos = new ObjectOutputStream(fichier);
                Integer sizeMessage =(Integer) messages.size();
                Integer sizePsudoForbiden = (Integer) PsudoForbiden.size();
                oos.writeObject(sizeMessage);
                oos.writeObject(sizePsudoForbiden);
                for (Message m : messages){
                    oos.writeObject((Message) m );
                }
                for(String s : PsudoForbiden){
                    oos.writeObject( s );
                }
              oos.flush();
            } catch (final java.io.IOException e) {
              e.printStackTrace();
            } finally {
              try {
                if (oos != null) {
                  oos.flush();
                  oos.close();
                }
              } catch (final IOException ex) {
                ex.printStackTrace();
              }
            }
    }
    public void restorHistorique(){
        ObjectInputStream ois = null;

        try {
            final FileInputStream fichier = new FileInputStream("donnees.ser");
            ois = new ObjectInputStream(fichier);
            Integer sizeMessage =(Integer)ois.readObject();;
            Integer sizePsudoForbiden = (Integer)ois.readObject();
            Vector<Message> messageList = new Vector<Message>();
            for (int i = 0 ; i< sizeMessage; i++){
                messageList.add((Message)ois.readObject());
            }
            Vector<String> PsudoForbidenListe = new Vector<String>();
            for (int i = 0 ; i< sizePsudoForbiden; i++){
                PsudoForbidenListe.add((String)ois.readObject());
            }
            messages = messageList;
            PsudoForbiden = PsudoForbidenListe;
            for (Message m: messages){
                sv.addMessage(m.GetExpediteur()+" > "+m.GetDestinataire()+": "+m.GetMessage());
            }
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
            messages.removeAllElements();
            PsudoForbiden.removeAllElements();
            saveHistorique();
    }
}

