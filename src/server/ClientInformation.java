package server;

public class ClientInformation {
	private String nom;
	private String color;
	private ClientThread socket;
	
	public ClientInformation(String nom, String color, ClientThread socket) {
		this.nom = nom;
		this.color = color;
		this.socket = socket;
	}

	public String GetNom() {
		return nom;
	}

	public void SetNom(String nom) {
		this.nom = nom;
	}

	public String GetColor() {
		return color;
	}

	public void SetColor(String color) {
		this.color = color;
	}

	public ClientThread GetSocket() {
		return socket;
	}

	public void SetSocket(ClientThread socket) {
		this.socket = socket;
	}
	
	
	
}
