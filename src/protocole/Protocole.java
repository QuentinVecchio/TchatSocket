package protocole;

public class Protocole {
	private int type;
	
	public Protocole(String p) {
		String[] parts = p.split(";");
		if(parts[0].equals("CONNECT")){
			type = 0;
		} else if(parts[0].equals("QUIT")){
			type = 1;
		} else if(parts[0].equals("MESSAGE")){
			type = 2;
		} else if(parts[0].equals("FALSE")){
			type = 3;
		} 
	}
	
	public int GetType() {
		return type;
	}
}
