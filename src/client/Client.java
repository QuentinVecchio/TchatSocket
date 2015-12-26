package client;

import java.awt.Color;
import java.util.LinkedList;

import protocole.Message;

public class Client {
	private String name;
	private String host;
	private String port;
	private Color color;
	private LinkedList<Message> messages = new LinkedList<Message>();
	
	public Client() {
		this.name = "";
		this.host = "";
		this.port = "";
		this.color = new Color(0, 0, 0);
	}
	
	public Client(String name, String host, String port) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.color = new Color(0, 0, 0);
	}

	public String GetName() {
		return name;
	}
    
	public String GetHost() {
		return host;
	}
	
	public String GetPort() {
		return port;
	}
	
	public Color GetColor() {
		return color;
	}
	
	public void SetName(String name) {
		this.name = name;
	}
    
	public void SetHost(String host) {
		this.host = host;
	}
	
	public void SetPort(String port) {
		this.port = port;
	}
	
	public void SetColor(Color c) {
		this.color = c;
	}
	
	public void AddMessage(Message message) {
		messages.add(message);
	}
	
    public LinkedList<Message> GetMessages() {
    	return messages;
    }
}

