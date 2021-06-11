import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;

public class Client {

    private static ByteBuffer buffer = ByteBuffer.allocate(1024);
    public static ConcurrentHashMap<String, String> writen = new ConcurrentHashMap<>();
    private static JTextArea messages;
    private static DefaultListModel<String> listModel;

    public Client(JTextArea msg, DefaultListModel<String> lm) {
        messages = msg;
        listModel = lm;
    }

    static private String read(SocketChannel channel) {
        try {
            buffer.clear();
            int count = channel.read(buffer);
            return new String(buffer.array(), 0, count, StandardCharsets.UTF_8);
        } catch (IOException ex) {
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

                try {
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
                                String[] vector = mess.split(" ");
                                if (vector[1].equals(" has")) {
                                    listModel.addElement(vector[0]);
                                } else if (vector[1].equals("Disconnected")) {
                                    listModel.removeElement(vector[5]);
                                }
                                messages.append(mess);
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