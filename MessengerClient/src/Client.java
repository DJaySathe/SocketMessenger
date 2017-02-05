import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client {

	BufferedReader in;
	PrintWriter out;

	public class KeyboardInput extends Thread {
		Scanner s = new Scanner(System.in);

		@Override
		public void run() {
			while (true) {
				out.println(s.nextLine());
			}

		}

	}

	private void run() throws IOException {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Server Ip Address:");

		String serverAddress = s.nextLine();
		Socket socket = new Socket(serverAddress, 4444);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in.readLine();
			if (line.startsWith("YourName?")) {
				System.out.println("Plese Enter your name: ");
				out.println(s.nextLine());
			} else if (line.startsWith("Welcome")) {
				System.out.println("Type your message and send:");
				Thread t1= new Thread(new KeyboardInput());
				t1.start();
			} else if (line.startsWith("msg")) {
				System.out.println(line.substring(3));
			}
		}
	}

	/**
	 * Runs the client as an application with a closeable frame.
	 */
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.run();
	}
}