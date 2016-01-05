package server;

import java.io.IOException;
import java.net.Socket;

public class ServerSocketThread extends Thread {

	private ServerSocketController controller;
	
	public ServerSocketThread(ServerSocketController controller) {
		this.controller = controller;
	}
	
	/**
	 * Méthode qui est appellé lors du lancement du thread
     *
     */
	public void run() {
		 while (true) {
 			Socket clientSocket;
			try {
				clientSocket = controller.serverSocket.accept();
				System.out.println("Connexion from:" + clientSocket.getInetAddress());
	 			ClientThread ct = new ClientThread(clientSocket, controller);
	 			ct.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
 		}
	}
}
