package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import protocole.Message;
import protocole.Protocole;

public class ClientThread extends Thread {
	private Socket clientSocket;
	private ServerSocketController controller;
	private BufferedReader socIn;
	private PrintStream socOut;
	
	/**
	 * Constructeur de la classe ClientThread
	 * 
     * @param s : socket
     * @param controller : controleur du serveur
     *
     */
	ClientThread(Socket s, ServerSocketController controller) {
		this.clientSocket = s;
		this.controller = controller;
		try {
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			socOut = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	/**
	 * Méthode appelé au lancement du thread
	 * 
     *
     */
	public void run() {
  		while (true) {
  			try {
  				String line = socIn.readLine();
  				if(line != null) {
	  				Protocole p = new Protocole(line);
	  				if(p.GetType() == 0) {
	  					controller.Register(line, this);
	  				} else if(p.GetType() == 1) {
	  					String[] parts = line.split(";");
	  					controller.Disconnection(parts[1]);
	  					this.Disconnection();
	  					clientSocket.close();
	  					break;
	  				} else if(p.GetType() == 2) {
	  					Message m = new Message(line);
	  	  				controller.Send(m);
	  				}
  				}
			} catch (IOException e) {
				e.printStackTrace();
			}
  		}
  	}
	
	/**
	 * Méthode qui gère l'envoie d'un message sous la forme string
	 * @param s : String
     *
     */
	public void SendString(String s) {
		socOut.println(s);
	}
	
	/**
	 * Méthode qui gère l'envoie d'un message dans le cas où le pseudo a déjà été utilisé
	 * 
     */
	public void AlreadyUse() {
		socOut.println("FALSE");
	}
	
	/**
	 * Méthode qui gère la déconnexion
	 * 
     */
	public void Disconnection() {
		try {
			socIn.close();
			socOut.close();
			clientSocket.close();
			this.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Méthode qui gère l'acceptation de la connexion
	 * 
     */
	public void ConnectionAuthorized() {
		socOut.println("TRUE");
	}
	
	/**
	 * Méthode qui gère l'envoie d'un message sous la forme Message
	 * @param m : Message
     *
     */
	public void Send(Message m) {
		socOut.println(m.toString());
	}
	
	/**
	 * Méthode qui gère l'ajout d'un client
	 * @param s : String
     *
     */
	public void AddClient(String n) {
		socOut.println("CONNECT;" + n);
	}
	
	/**
	 * Méthode qui gère la suppression d'un client
	 * @param s : String
     *
     */
	public void DeleteClient(String n) {
		socOut.println("QUIT;" + n);
	}
	
	public void Shutdown() {
		socOut.println("SHUTDOWN;");
	}
}
