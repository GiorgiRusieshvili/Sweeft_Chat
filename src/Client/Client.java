package Client;

import Network.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Connection connection;
    private static ListOfClients listOfClients;
    private static ClientGUI clientGUI;
    private boolean isConnected = false;

    // entry point for client application
    public static void main(String[] args) {
        Client client = new Client();
        listOfClients = new ListOfClients();
        clientGUI = new ClientGUI(client);
        clientGUI.initClientGui();
        while (true) {
            if (client.isConnected()) {
                client.userNameRegister();
                client.receiveMessage();
                client.setConnected(false);
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void connectToServer() {
        if (!isConnected) {
            while (true) {
                try {
                    String addressServer = clientGUI.addressInput();
                    int port = clientGUI.portInput();
                    Socket socket = new Socket(addressServer, port);
                    connection = new Connection(socket);
                    isConnected = true;
                    clientGUI.addMessage("Server: you are connected to the server.\n");
                    break;
                } catch (Exception e) {
                    clientGUI.errorWindow("Error! Server address or port number is incorrect. Try again");
                    break;
                }
            }
        } else clientGUI.errorWindow("you are already connected!");
    }

    public void userNameRegister() {
        while (true) {
            try {
                Message message = connection.receiveMessage();
                if (message.getMessageType() == Message.Category.REQUEST_NAME_USER) {
                    String userName = clientGUI.getUserName();
                    connection.sendMessage(new Message(Message.Category.USER_NAME, userName));
                }
                if (message.getMessageType() == Message.Category.NAME_USED) {
                    clientGUI.errorWindow("This Username is already in use! try another one");
                    String userName = clientGUI.getUserName();
                    connection.sendMessage(new Message(Message.Category.USER_NAME, userName));
                }
                if (message.getMessageType() == Message.Category.NAME_ACCEPTED) {
                    listOfClients.setClients(message.getListOfUsers());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                clientGUI.errorWindow("Error while registering the username! Try to reconnect");
                try {
                    connection.close();
                    isConnected = false;
                    break;
                } catch (IOException ex) {
                    clientGUI.errorWindow("Error while closing the connection");
                }
            }

        }
    }

    public void receiveMessage() {
        while (isConnected) {
            try {
                Message message = connection.receiveMessage();
                if (message.getMessageType() == Message.Category.TEXT_MESSAGE) {
                    clientGUI.addMessage(message.getTextMessage());
                }
                if (message.getMessageType() == Message.Category.USER_ADDED) {
                    listOfClients.addClient(message.getTextMessage());
                    clientGUI.updateUsers(listOfClients.getClients());
                    String user =message.getTextMessage();
                    clientGUI.addMessage("Server: user " +user+ " has joined.\n");
                }
                if (message.getMessageType() == Message.Category.REMOVED_USER) {
                    listOfClients.removeClient(message.getTextMessage());
                    clientGUI.updateUsers(listOfClients.getClients());
                    String user =message.getTextMessage();
                    clientGUI.addMessage("Server: user " +user+ " has left the chat.\n");
                }
            } catch (Exception e) {
                clientGUI.errorWindow("Error while revenge message from the Server.");
                setConnected(false);
                clientGUI.updateUsers(listOfClients.getClients());
                break;
            }
        }
    }

    public void sendMessageOnServer(String text) {
        try {
            connection.sendMessage(new Message(Message.Category.TEXT_MESSAGE, text));
        } catch (Exception e) {
            clientGUI.errorWindow("Error while sending the message");
        }
    }

    public void disableClient() {
        try {
            if (isConnected) {
                connection.sendMessage(new Message(Message.Category.DISABLE_USER));
                listOfClients.getClients().clear();
                isConnected = false;
                clientGUI.updateUsers(listOfClients.getClients());
            } else clientGUI.errorWindow("You are already disconnected .");
        } catch (Exception e) {
            clientGUI.errorWindow("Server: Error while disconnecting.");

        }
    }
}

