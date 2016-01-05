package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import protocole.Message;
import protocole.Protocole;

public class CommunicationThread extends Thread {
	private ClientSocketController controller;
	private Socket echoSocket = null;
	private PrintStream socOut = null;
    private BufferedReader socIn = null;
    
    /**
	 * Constructeur de la classe CommunicationThread
	 * @param controller : Controller du client
	 * @param socket : Socket du client
     * @param socOut : flux d'ecriture sur la socket
     * @param socIn : flux de lecture sur la socket
     */
	public CommunicationThread(ClientSocketController controller, Socket socket, PrintStream socOut, BufferedReader socIn) {
		this.controller = controller;
		this.echoSocket = socket;
		this.socOut = socOut;
		this.socIn = socIn;
	}
	
	/**
	 * Méthode qui gère la déconnexion
	 * 
     */
	public void Disconnection() {
		socOut.println("QUIT;" + controller.GetName() + ";" + controller.GetColor().getRed()+"_"+controller.GetColor().getGreen()+"_"+controller.GetColor().getBlue());
		try {
			echoSocket.close();
			socIn.close();
			socOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode qui gère l'envoie d'un message
	 * 
	 * @param m : Message a envoyer
     */
	public void Send(Message m) {
		socOut.println(m.toString());
	}
	
	/**
	 * Méthode run appelle au lancement du thread
	 * 
     */
	public void run() {
		while (true) {
        	try {
				String line = socIn.readLine();
				Protocole p = new Protocole(line);
				System.out.println(line);
				if(p.GetType() == 0) {
					String[] parts = line.split(";");
					controller.AddClient(parts[1]);
				} else if(p.GetType() == 2) {
					Message m = new Message(line);
					controller.Receive(m);
				} else if (p.GetType() == 1) {
					String[] parts = line.split(";");
					controller.DeleteClient(parts[1]);
				} else if(p.GetType() == 5) {
					controller.Deconnect();
				}
			} catch (IOException e) {
				break;
			}
        }
		System.exit(0);
	}
}
