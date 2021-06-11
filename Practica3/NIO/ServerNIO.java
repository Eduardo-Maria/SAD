import java.io.*;

public class ServerNIO {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Insert the port: ");
		String port = in.readLine();

		MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(port));
		try {
			serverSocket.execute();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
