import java.io.*;
import java.net.*;

public class WebServer {

	private static boolean controlSwitch = true;

	public static void main(String[] args) throws IOException {

		int q_len = 6;
		int port = 2540;
		Socket socket;
		
		// create a new sockect server with specific port and length of queue
		ServerSocket serverSocket = new ServerSocket(port, q_len);
		System.out.printf("Food bank server is listening at %d.\n", port);

		SQLiteDB dataBase = new SQLiteDB();
		//dataBase.createNewDatabase("tht");
		dataBase.createNeighborTable();
		dataBase.createOrderTable();
		dataBase.createAddressTable();
		dataBase.createVolunteerTable();
//		dataBase.createPickUpLocationTable();
		
		while(controlSwitch) {
			
			// listening the request
			socket = serverSocket.accept();
			
			// create a new ServerWorker(thread) to handle the request
			new ServerWorker(socket, dataBase).start();
		}
	}
}
