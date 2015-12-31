package protocole;

public class Protocole {
	private int type = -1;
	
	public Protocole(String p) {
		if(p != null) {
			String[] parts = p.split(";");
			if(parts[0].equals("CONNECT") && (parts.length == 3 || parts.length == 2)){
				type = 0;
			} else if(parts[0].equals("QUIT") && (parts.length == 3 || parts.length == 2)){
				type = 1;
			} else if(parts[0].equals("MESSAGE")){
				type = 2;
			} else if(parts[0].equals("FALSE") && parts.length == 1){
				type = 3;
			} else if(parts[0].equals("TRUE") && parts.length == 1){
				type = 4;
			}
		}
	}
	
	public int GetType() {
		return type;
	}
}
