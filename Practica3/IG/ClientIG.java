import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class ClientIG implements ActionListener {

    private static JTextField text;
    private static JTextArea messages;
    private static DefaultListModel<String> listModel;

    public ClientIG() {
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
        messages = new JTextArea(22, 22);
        messages.setEditable(false);
        output.add(new JScrollPane(messages));
        frame.add(output, BorderLayout.WEST);
        
        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));
        text = new JTextField(25);
        JButton button = new JButton("Send");
        text.addActionListener(this);
        button.addActionListener(this);
        input.add(text);
        input.add(button);
        frame.add(input, BorderLayout.PAGE_END);

        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        frame.add(scroll, BorderLayout.EAST);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        Client.writen.put("key", text.getText());
        messages.append(text.getText() + "\n");
        text.setText(null);
    }

    private static void executeGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        ClientIG chat = new ClientIG();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                executeGUI();
            }
        });

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (args.length < 2) return;
        Client client = new Client(messages, listModel);
        client.main(args);
    }
}
