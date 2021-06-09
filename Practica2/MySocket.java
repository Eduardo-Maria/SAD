import java.net.*;
import java.io.*;

public class MySocket extends Socket {

    private InputStream in;
    private OutputStream out;
    private BufferedReader read;
    private PrintWriter write;
    public String nick;

    public MySocket(String host, int port, String name) throws IOException { 
        super(host,port);
        nick = name;

        in = getInputStream();
        out = getOutputStream();
    
        read = new BufferedReader(new InputStreamReader(in));
        write = new PrintWriter(out,true);

        println(name);       
    }

    public String readText() throws IOException{
        String text = read.readLine();
        return text;
    }

    public void println(String outputstream) throws IOException{
        write.println(outputstream);
    }
}