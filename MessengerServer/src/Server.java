import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

	private static final int PORT = 4444;
	private static HashSet<String> users = new HashSet<String>();
	private static HashSet<PrintWriter> printWriterSet = new HashSet<PrintWriter>();

	public static void main(String[] args) throws Exception {
		System.out.println("Server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Chat(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Chat extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Chat(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					out.println("YourName?");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (users) {
						if (!users.contains(name)) {
							users.add(name);
							break;
						}
					}
				}
				out.println("Welcome");
				printWriterSet.add(out);
				while (true) {
					String input = in.readLine();
					if (input == null) {
						return;
					}
					for (PrintWriter writer : printWriterSet) {
						writer.println("msg" + name + ": " + input);
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null) {
					users.remove(name);
				}
				if (out != null) {
					printWriterSet.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}