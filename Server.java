import java.io.IOException;
import java.net.ServerSocket;


public class Server {
	
	Server(ServerSocket serverSocket) throws IOException{
		while(true){
			CreateThread r = new CreateThread(serverSocket.accept());
			r.start();
		}
	}
	
	public static void main(String[] args){
		int port = 5678;
		ServerSocket serverSocket;
		try{
			serverSocket = new java.net.ServerSocket(port);
			Server server = new Server(serverSocket);
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.err.println("IOFEL 1. Humankind is doomed. Thank you for playing.");
			System.exit(1);
		}
	}
}
