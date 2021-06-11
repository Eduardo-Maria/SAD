import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ServerIG implements ActionListener {
    private static JTextField text;
    private static JTextArea messages;
    private static DefaultListModel<String> listModel;

    public ServerIG() {
        JFrame window = new JFrame("Server");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
        messages = new JTextArea(22, 22);
        messages.setEditable(false);
        output.add(new JScrollPane(messages));
        window.add(output, BorderLayout.WEST);
        
        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));
        text = new JTextField(25);
        JButton button = new JButton("Send");
        text.addActionListener(this);
        button.addActionListener(this);
        input.add(text);
        input.add(button);
        window.add(input, BorderLayout.PAGE_END);

        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        window.add(scroll, BorderLayout.EAST);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        Server.writen.put("key", text.getText());
        messages.append(text.getText() + "\n");
        text.setText(null);
    }

    private static void executeGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        ServerIG chat = new ServerIG();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                executeGUI();
            }
        });

        try {
            Thread.sleep(10000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (args.length < 1) 
            return;

        Server server = new Server(messages, listModel);
        try{
            server.main(args);
            
        }catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
}
