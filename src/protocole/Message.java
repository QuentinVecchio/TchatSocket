package protocole;

import java.awt.Color;
import java.util.Date;

public class Message {

	private String expediteur;
	private String destinataire;
	private String date;
	private String message;
	private Color color;
	
	public Message(String expediteur, String destinataire, String message, Color c) {
		this.expediteur = expediteur;
		this.destinataire = destinataire;
		this.date = new Date().toString();
		this.message = message;
		this.color = c;
	}

	public String GetExpediteur() {
		return expediteur;
	}

	public void SetExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	public String GetDestinataire() {
		return destinataire;
	}

	public void SetDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}
	
	public String GetDate() {
		return date;
	}
	
	public String GetMessage() {
		return message;
	}

	public void SetMessage(String message) {
		this.message = message;
	}
	
	public Color GetColor() {
		return this.color;
	}
	
	public void SetColor(Color c) {
		this.color = c;
	}
}
