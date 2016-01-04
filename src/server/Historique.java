package server;

import java.io.Serializable;
import java.util.LinkedList;
import protocole.Message;

public class Historique implements Serializable {
	private static final long serialVersionUID = 1L;
	private int nbMessage;
	private int nbPseudoForbidden;
	private LinkedList<Message> messages;
	private LinkedList<String> pseudoForbidden;
	
	public Historique(int nbMessage, int nbPseudoForbidden, LinkedList<Message> mess, LinkedList<String> pseudoForbidden) {
		super();
		this.nbMessage = nbMessage;
		this.nbPseudoForbidden = nbPseudoForbidden;
		this.messages = mess;
		this.pseudoForbidden = pseudoForbidden;
	}

	public int getNbMessage() {
		return nbMessage;
	}
	
	public void setNbMessage(int nbMessage) {
		this.nbMessage = nbMessage;
	}
	
	public int getNbPseudoForbidden() {
		return nbPseudoForbidden;
	}
	
	public void setNbPseudoForbidden(int nbPseudoForbidden) {
		this.nbPseudoForbidden = nbPseudoForbidden;
	}
	
	public LinkedList<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(LinkedList<Message> messages) {
		this.messages = messages;
	}
	
	public LinkedList<String> getPseudoForbidden() {
		return pseudoForbidden;
	}
	
	public void setPseudoForbidden(LinkedList<String> pseudoForbidden) {
		this.pseudoForbidden = pseudoForbidden;
	}
	
	
}
