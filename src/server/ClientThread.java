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
	
	public void run() {
  		while (true) {
  			try {
  				String line = socIn.readLine();
  				Protocole p = new Protocole(line);
  				if(p.GetType() == 0) {
  					controller.Register(line, this);
  				} else if(p.GetType() == 1) {
  					controller.Disconnection(line, this);
  					clientSocket.close();
  				} else if(p.GetType() == 2) {
  					Message m = new Message(line);
  	  				controller.Send(m);
  				}
			} catch (IOException e) {
				e.printStackTrace();
			}
  		}
  	}
	
	public void AlreadyUse() {
		socOut.println("FALSE");
	}
	
	public void Send(Message m) {
		socOut.println(m.toString());
	}
	
	public void AddClient(String n) {
		socOut.println("CONNECT;" + n);
	}
	
	public void DeleteClient(String n) {
		socOut.println("QUIT;" + n);
	}
}