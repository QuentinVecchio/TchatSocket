package protocole;

import java.awt.Color;

import java.io.Serializable;

import java.util.Date;

public class Message implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String expediteur;
	private String destinataire;
	private String date;
	private String message = "";
	private Color color;
	
	/**
	 * Constructeur de la classe Message
	 * 
     * @param expediteur : nom de l'expediteur
     * @param destinataire : nom du destinataire
     * @param message : le message
     * @param c : la couleur du message
     */
	public Message(String expediteur, String destinataire, String message, Color c) {
		this.expediteur = expediteur;
		this.destinataire = destinataire;
		this.date = new Date().toString();
		this.message = message;
		this.color = c;
	}
	
	/**
	 * Constructeur de la classe Message a partir d'un String
	 * 
     * @param message : message formaté 
     */
	public Message(String message) {
		String[] parts = message.split(";");
		if(parts.length > 4) {
			this.expediteur = parts[1];
			this.destinataire = parts[2];
			String[] rgb = parts[3].split("_");
			if(rgb.length == 3) {	 
				this.color = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
			} else {
				this.color = Color.BLACK;
			}
			this.date = new Date().toString();
			for(int i=4;i<parts.length;i++) {
				this.message += parts[i];
			}
		} else {
			this.expediteur = "stranger";
			this.destinataire = "all";
			this.date = new Date().toString();
			this.message = "";
			this.color = Color.BLACK;
		}
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
    
    public String toAffiche(){
        return expediteur+" > "+destinataire+": "+message;
    }
	public String toString() {
		return "MESSAGE;" + expediteur + ";" + destinataire + ";" + color.getRed() + "_" + color.getGreen() + "_" + color.getBlue() + ";" + message;
	}
}
