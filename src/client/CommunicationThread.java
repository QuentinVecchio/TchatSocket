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
    
	public CommunicationThread(ClientSocketController controller, Socket socket, PrintStream socOut, BufferedReader socIn) {
		this.controller = controller;
		this.echoSocket = socket;
		this.socOut = socOut;
		this.socIn = socIn;
	}
	
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
	
	public void Send(Message m) {
		socOut.println(m.toString());
	}
	
	public void run() {
		while (true) {
        	try {
				String line = socIn.readLine();
				Protocole p = new Protocole(line);
				if(p.GetType() == 0) {
					String[] parts = line.split(";");
					controller.AddClient(parts[1]);
				} else if(p.GetType() == 2) {
					Message m = new Message(line);
					controller.Receive(m);
				} else if (p.GetType() == 1) {
					String[] parts = line.split(";");
					controller.DeleteClient(parts[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
