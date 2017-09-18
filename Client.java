import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	
	public static void main(String[] args){
		try{
			Socket socket = new Socket("127.0.0.1",5678);
			
			// Client connection (input/output) to the created Socket.
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				System.out.println("Enter a matematical expression to calculate: ");
				String expression = stdIn.readLine();
				output.println(expression);
				System.out.println(input.readLine()); // Waits for output from server 
				System.out.println(input.readLine()); // Waits again.
			}
		}
		catch(UnknownHostException ukhe){
			System.err.println("Host not found!");
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.err.println("IOFEL 3");
		}
		
		
		
	}
	
}
