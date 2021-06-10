import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;


public class MyServerSocket extends ServerSocket {
    
    private Map<String, String> users = new HashMap<>();
    private Selector select;
	private ByteBuffer buffer = ByteBuffer.allocate(2048);
	private ServerSocket serverSocket;
    private boolean connected = false;

    public MyServerSocket (int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
		select = Selector.open();
		serverSocketChannel.register(select, SelectionKey.OP_ACCEPT);
    }

    public String getKey(String address){
        for (Map.Entry<String, String> entry: users.entrySet()) {
            if (address.equals(entry.getValue())){
                String key = entry.getKey();
                return key;
            }
        }
        return null;
    }

    public boolean add(SocketChannel channel, String nick) throws IOException{
        if (users.containsKey(nick)) {
            write(channel, "Username used. Try another one");
            return false;
       } else {
           System.out.println("New User Connected: " + nick);
           write(channel, "Welcome to the chat room!\n");
           users.put(nick, channel.getRemoteAddress().toString());
           message(nick + " has joined the room", null);
           return true;
       }
    }

    public void delete(SocketChannel channel, String user) throws IOException {
        users.remove(user);
        write(channel, "User Disconnected ------ Bye: " + user);
        System.out.println("User Disconnected: " + user);
        channel.close();
    }

    public void execute() throws IOException {
		while (true) {
			select.select();

			Iterator<SelectionKey> iterator = select.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				connect(key);
			}
			select.selectedKeys().clear();
		}
	}
	
	private void connect(SelectionKey key) throws IOException {
		ServerSocketChannel server;
		SocketChannel client;

		if (key.isAcceptable()) {
			server = ((ServerSocketChannel) key.channel());
			client = server.accept();
            connected = true;
			client.configureBlocking(false);
			client.register(select, SelectionKey.OP_READ);
			key.interestOps(SelectionKey.OP_ACCEPT);
		} else if (key.isReadable()) {
			client = ((SocketChannel) key.channel());
			try {
                String resp = read(client);
				if (connected) {
					add(client, resp);
                    connected = false;
				} else{
                    String address = client.getRemoteAddress().toString();
				    String nick = getKey(address);
					message(nick + ": " + resp, client);
				}
				key.interestOps(SelectionKey.OP_READ);
			} catch (Exception e) {
				String address = client.getRemoteAddress().toString();
				String nick = getKey(address);
                delete(client, nick);
			}
		}
    }

	private void message(String msg, SocketChannel except) throws IOException {
		for (SelectionKey k : select.keys()) {
			Channel target = k.channel();
			if (target.isOpen() && target instanceof SocketChannel && target != except) {
				write((SocketChannel) target, msg);
			}
		}
	}

	private String read(SocketChannel channel) throws IOException {
		buffer.clear();
		int count = channel.read(buffer);
		return new String(buffer.array(), 0, count, StandardCharsets.UTF_8);
	}

	private void write(SocketChannel channel, String content) throws IOException {
		buffer.clear();
		buffer.put(content.getBytes(StandardCharsets.UTF_8));
		buffer.flip();
		channel.write(buffer);
	}
}