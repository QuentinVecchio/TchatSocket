package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.RemoteException;

import protocole.Message;
import protocole.Protocole;

public class ClientSocketController extends Thread {
	private Client c;
	private ClientConnectionSocketView connectionView;
	private ClientView view;
	private CommunicationThread thread;
	
	public ClientSocketController() {
		c = new Client();
		connectionView = new ClientConnectionSocketView(this);
	}
	
	public void Start() {
		connectionView.Affiche();
	}
	
	public void Connection() {
		 try {
			Socket echoSocket = new Socket(c.GetHost(),Integer.parseInt(c.GetPort()));
			BufferedReader socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));    
			PrintStream socOut= new PrintStream(echoSocket.getOutputStream());
			socOut.println("CONNECT;"+c.GetName()+";"+c.GetColor().getRed()+"_"+c.GetColor().getGreen()+"_"+c.GetColor().getBlue());
			String line = socIn.readLine();
			Protocole p = new Protocole(line);
			if(p.GetType() == 4){
				view = new ClientView(this);
				connectionView.Exit();
				connectionView=null;
				view.Affiche();
				System.gc();
				thread = new CommunicationThread(this, echoSocket, socOut, socIn);
				thread.start();
			} else {
				connectionView.ErrorName();
			}
		} catch (Exception e) {
			connectionView.ErrorHost();
			e.printStackTrace();
		}
	}

	public void Disconnection() {
		thread.Disconnection();
		view.Exit();
		System.exit(0);
	}
	
	public void Receive(Message m) {	
		view.AddMessage(m);
	}
	
	public void AddClient(String client) {
		view.AddClient(client);
	}
	
	public void DeleteClient(String client) {
		view.DeleteClient(client);
	}
	
	public void Send(Message m) {
		thread.Send(m);
	}
	
	public String GetName() {
		return c.GetName();
	}
	
    public String GetHost()  {
    	return c.GetHost();
    }

    public String GetPort()  {
    	return c.GetPort();
    }
    
    public Color GetColor()  {
    	return c.GetColor();
    }
    
	public void SetName(String name) {
		c.SetName(name);
	}
	
	public void SetHost(String host) {
		c.SetHost(host);
	}
	
	public void SetPort(String port) {
		c.SetPort(port);
	}
	
	public void SetColor(Color color) {
		c.SetColor(color);
	}
	
	public void Deconnect() throws RemoteException {
		this.view.DeconnectError();
	}
}
