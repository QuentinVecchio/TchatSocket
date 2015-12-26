package client;

import java.awt.Color;

import protocole.Message;

public class ClientSocketController extends Thread {
	private Client c;
	private ClientConnectionSocketView connectionView;
	private ClientView view;
	
	public ClientSocketController() {
		c = new Client();
		connectionView = new ClientConnectionSocketView(this);
	}
	
	public void Start() {
		connectionView.Affiche();
	}
	
	public void Connection() {
		
	}

	public void Disconnection() {
		view.Exit();
		
	}
	
	public void Receive() {	
		
	}
	
	public void AddClient(String client) {
		view.AddClient(client);
	}
	
	public void Send(Message m) {
		
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
}
