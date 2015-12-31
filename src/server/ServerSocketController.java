package server;

import java.awt.Color;
import java.net.ServerSocket;
import java.util.LinkedList;
import protocole.Message;

public class ServerSocketController {
	 	public ServerSocket serverSocket; 
	    private ServerCreatView scv;
	    private ServerView sv;
	    private ServerSocketThread thread;
	    private LinkedList<ClientInformation> clients = new LinkedList<ClientInformation>();
		private LinkedList<Message> messages = new LinkedList<Message>();
		
	public void start() {
    	scv = new ServerCreatView(this);
    }
    
    public void InitServeur(int port) {
        try {
        	serverSocket = new ServerSocket(port);
            scv.Exit();
            thread = new ServerSocketThread(this);
            thread.start();
            System.out.println("Server ready");
            sv = new ServerView();
        } catch (Exception e) {
        	scv.ErrorPort();
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }       
    }
    
    public void Register(String c, ClientThread socket) {
    	String[] parts = c.split(";");
    	if(NameExist(parts[1])) {
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
	    	for(int i=0;i<messages.size();i++) {
	    		socket.Send(messages.get(i));
	    	}
	    	for(int i=0;i<clients.size();i++) {
	    		clients.get(i).GetSocket().Send(m);
	    		if(clients.get(i).GetNom().equals(parts[1]) == false) {
	    			clients.get(i).GetSocket().AddClient(parts[1]);
	    		}
	    	}
	    	messages.add(m);
	    	for(int i=0;i<clients.size();i++) {
	    		socket.AddClient(clients.get(i).GetNom());
	    	}
    	}
    }
    
    public void Disconnection(String c, ClientThread socket) {
    	ClientInformation client = SearchClient(c);
    	if(client != null) {
    		clients.remove(client);
	    	String[] rgb = client.GetColor().split(";");
	    	Color color = Color.BLACK;
	    	if(rgb.length == 3) {
	    		color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
	    	}
	    	Message m = new Message(client.GetNom(), "all", client.GetNom() + " est déconnecté", color);
	    	messages.add(m);
	    	for(int i=0;i<clients.size();i++) {
	    		System.out.println(m.toString());
	    		clients.get(i).GetSocket().Send(m);
	    		clients.get(i).GetSocket().DeleteClient(c);;
	    	}
	    	socket.interrupt();
    	}
    }
    
    public void Send(Message message) {	
    	messages.add(message);
    	for(int i=0;i<clients.size();i++) {
    		clients.get(i).GetSocket().Send(message);
    	}
    }

    private boolean NameExist(String name) {
    	for(int i=0;i<clients.size();i++) {
	    	if(clients.get(i).GetNom().equals(name)) {
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
}

