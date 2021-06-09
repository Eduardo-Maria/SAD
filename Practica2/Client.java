import java.io.*;

public class Client {

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Insert the host: ");
        String host = in.readLine();
        System.out.print("Insert the port: ");
        String port = in.readLine();
        System.out.print("Choose a nickname: ");
        String nick = in.readLine();

        MySocket mySocket = new MySocket(host,Integer.parseInt(port),nick);
        
        (new Thread() {
            @Override
            public void run() {
                String resp;
                BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
                
                try {
                    while ((resp = read.readLine()) != null) {
                        mySocket.println(resp);
                    }
                    mySocket.close();
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } 
            }
        }).start();

        String text;   
        while((text = mySocket.readText())!=null){
            System.out.println(text);
        }
    }
}
