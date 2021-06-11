import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws InterruptedException, IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Insert the port: ");
        String port = in.readLine();

        MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(port));

        while (true) {
            Socket socket = serverSocket.accept();
            String user = serverSocket.add(socket);

            if (user != null) {
                serverSocket.write("New User Connected ------ " + user, user);
                System.out.println("New User Connected: " + user);

                (new Thread() {
                    @Override
                    public void run() {
                        String conv;
                        try {
                            while ((conv = serverSocket.read(user)) != null) {
                                serverSocket.write(user + ": " + conv, user);
                            }
                            serverSocket.delete(user);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();

            } else {
                OutputStream out = socket.getOutputStream();
                PrintWriter write = new PrintWriter(out, true);
                write.println("Username used. Try another one");

                socket.close();
            }

        }
    }
}
