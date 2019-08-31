import java.net.*;
import java.io.*;

public class Sender {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	public static void main(String [] args) {
		BufferedReader reader =  
				new BufferedReader(new InputStreamReader(System.in)); 
		Sender s = new Sender();
		try {
			String str = new String();
			while((str = reader.readLine()) != null) {
				s.startConnection("127.0.0.1",778);
				s.sendMessage(str);
				s.stopConnection();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	//function to begin connection to a given ip and port
	public void startConnection(String ip, int port) {
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}catch(Exception e) {
			System.out.println("Error Starting Connection");
			e.printStackTrace();
		}
	}

	//send a message through the open connection
	public String sendMessage(String msg) {
		try {
			assert (msg != null);
			out.println(msg);
			String resp = in.readLine();
			return resp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR";
	}

	//close the connection
	public void stopConnection() {
		try {
			this.sendMessage("."); //send a connection to the reducer handler to stop
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
