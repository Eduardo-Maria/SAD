import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class ClientNIO {

	private static ByteBuffer buffer = ByteBuffer.allocate(1024);

	static private String read(SocketChannel channel){
        try{
            buffer.clear();
            int count = channel.read(buffer);
            return new String(buffer.array(), 0, count, StandardCharsets.UTF_8);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
	}
		
	static private void write(SocketChannel channel, String content) throws IOException {
		buffer.clear();
		buffer.put(content.getBytes(StandardCharsets.UTF_8));
		buffer.flip();
		channel.write(buffer);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Insert the host: ");
        String host = in.readLine();
        System.out.print("Insert the port: ");
        String port = in.readLine();
        InetSocketAddress address = new InetSocketAddress(host, Integer.parseInt(port));

        System.out.print("Choose a nickname: ");

		Selector selector = Selector.open();
		SocketChannel socketChannel = SocketChannel.open(address);
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		(new Thread() {
            @Override
            public void run() {
			    SocketChannel client = null;

			    try{
                    while (true) {
                        selector.select();
					    Set<SelectionKey> selected = selector.selectedKeys();
					    Iterator<SelectionKey> iterator = selected.iterator();
					    while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            if (key.isReadable()) {
                                client = ((SocketChannel) key.channel());
                                String mess = read(client);
                                System.out.println(mess);
						    	key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                        selected.clear();
					}
				} catch (IOException e) {
                    if (client != null) {
                        try {
                            client.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
		}).start();

        BufferedReader text = new BufferedReader(new InputStreamReader(System.in));
        
		while (true) {
            write(socketChannel, text.readLine());
        }
	}
}
