package server;

import java.net.ServerSocket;

public class ServerSocketController {
	 	private ServerSocket serverSocket; 
	    private ServerCreatView scv;
	    private ServerView sv;
	    private ServerSocketThread thread;
	    
	public void start() {
    	scv = new ServerCreatView(this);
    }
    
    public void InitServeur(int port) {
        try {
        	serverSocket = new ServerSocket(port);
            scv.Exit();
            
        } catch (Exception e) {
        	scv.ErrorPort();
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }       
    }
    
    public void run() {
    	
    }
}
