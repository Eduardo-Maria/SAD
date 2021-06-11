import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class Server {

    private static JTextArea messages;
    private static DefaultListModel<String> listModel;
	public static ConcurrentHashMap<String, String> writen = new ConcurrentHashMap<>();

    public Server(JTextArea msg, DefaultListModel<String> lm) {
        messages = msg;
        listModel = lm;
    }
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Insert the port: ");
        String port = in.readLine();

        MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(port));
		try{
			serverSocket.execute(messages, listModel, writen);
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
}
