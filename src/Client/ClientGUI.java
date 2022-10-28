package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class ClientGUI {
    private final Client client;
    private JFrame frame = new JFrame("Sweeft Chat");
    private JPanel panel = new JPanel();
    private JTextArea messageArea = new JTextArea(30, 15);
    private JTextArea userListArea = new JTextArea(30, 15);
    private JTextField messageInputField = new JTextField(35);
    private JButton buttonDisconnect = new JButton("Disconnect");
    private JButton buttonConnect = new JButton("Connect");

    //ctr
    public ClientGUI(Client client) {
        this.client = client;
    }

    //method initializes Client Gui
    public void initClientGui() {
        messageArea.setEditable(false);
        userListArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(new JScrollPane(userListArea), BorderLayout.EAST);
        frame.setResizable(false);
        panel.add(messageInputField);
        panel.add(buttonConnect);
        panel.add(buttonDisconnect);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client.isConnected()) {
                    client.disableClient();
                }
                System.exit(0);
            }
        });
        frame.setVisible(true);
        buttonDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.disableClient();
            }
        });
        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.connectToServer();
            }
        });
        messageInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessageOnServer(messageInputField.getText());
                messageInputField.setText("");
            }
        });
    }

    //method adds a message
    public void addMessage(String text) {
        messageArea.append(text);
    }

    //method updates list of clients
    public void updateUsers(Set<String> listUsers) {
        userListArea.setText("");
        if (client.isConnected()) {
            StringBuilder text = new StringBuilder("List of Users:\n");
            for (String user : listUsers) {
                text.append(user).append("\n");
            }
            userListArea.append(text.toString());
        }
    }

    //invokes window for server address input
    public String addressInput() {
        while (true) {
            String addressServer = JOptionPane.showInputDialog(
                    frame, "Enter Address Or IP of the Server:",
                    "server port input",
                    JOptionPane.QUESTION_MESSAGE);
            return addressServer.trim();
        }
    }

    //invokes window for port input
    public int portInput() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame, "Enter the Port:",
                    "server port input",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame, "Port number is not correct! Try again.",
                        "server port input error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //invokes window for username input
    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame, "Enter Username:",
                "Enter Username",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    //invokes error window
    public void errorWindow(String text) {
        JOptionPane.showMessageDialog(
                frame, text,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

