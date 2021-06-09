import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyServerSocket extends ServerSocket {
    
    private Map<String, Socket> users;

    public MyServerSocket (int port) throws IOException {
        super(port);
        users = new HashMap<String, Socket>();
    }

    public void write (String text, String client) {
        Socket socket;
        PrintWriter out;
        try{
            for (Map.Entry<String, Socket> entry: users.entrySet()) {
                if (client != entry.getKey()){
                    socket = entry.getValue();
                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(text);
                }
            }
        }catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String read (String user){
        Socket socket = users.get(user);
        
        try{
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return read.readLine();

        }catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;
    }

    public String add(Socket socket) throws IOException {

        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String nuser = read.readLine();

        if(!users.containsKey(nuser)) {
            users.put(nuser, socket);
            return nuser;
        }
        return null;
    }

    public void delete(String user) throws IOException {
        users.remove(user);
        write("User Disconnected ------ Bye: " + user, "");
        System.out.println("User Disconnected: " + user);
    }

    public Map<String, Socket> getUsers () {
        return users;
    }
}