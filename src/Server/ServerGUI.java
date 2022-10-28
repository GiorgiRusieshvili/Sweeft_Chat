package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerGUI {
    private final Server server;
    private final JFrame frame = new JFrame("Server Startup");
    private final JTextArea textWindow = new JTextArea();
    private final JPanel panel = new JPanel();
    private final JButton buttonStart = new JButton("Start the Server");
    private final JButton buttonStop = new JButton("Stop the Server");

    //ctr
    public ServerGUI(Server server) {
        this.server = server;
    }

    //method for gui initialization
    public void initServerGui() {
        frame.setPreferredSize(new Dimension(450, 200));
        frame.setResizable(false);
        frame.add(new JScrollPane(textWindow), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        textWindow.setEditable(false);
        textWindow.setLineWrap(true);
        panel.add(buttonStart);
        panel.add(buttonStop);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.stopServer();
                System.exit(0);
            }
        });
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = portInput();
                server.startServer(port);
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.stopServer();
            }
        });
    }

    //method invokes dialogue window for port input
    public int portInput() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame, "Enter The Port Number:",
                    "server port input",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                return Integer.parseInt(port);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame, "Port number is not correct! Try again .",
                        "server port input error", JOptionPane.ERROR_MESSAGE);
            }
            if(port==null || port.equals("")){
                return -1;
            }
        }
    }

    //method prints new message in the text window
    public void serverMessage(String serverResponse) {
        textWindow.append(serverResponse);
    }
}

