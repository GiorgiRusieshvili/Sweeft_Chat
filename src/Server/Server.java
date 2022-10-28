package Server;

import Network.Connection;
import Network.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private static ServerGUI serverGui;
    private static ListOfUsers list;
    private static boolean isServerUp = false;

    public static void main(String[] args) {
        Server server = new Server();
        serverGui = new ServerGUI(server);
        list = new ListOfUsers();
        serverGui.initServerGui();

        while (true) {
            if (isServerUp) {
                server.clientListener();
                isServerUp = false;
            }
        }
    }

    //method to start server
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isServerUp = true;
            serverGui.serverMessage("Server has started on port: "+port+"\n");
        } catch (Exception e) {
            serverGui.serverMessage("Could not start the Server!.\n");
        }
    }

    //method to stop server
    public void stopServer() {
        try {
            if (!serverSocket.isClosed() && serverSocket != null) {
                for (Map.Entry<String, Connection> user : list.getUsersCollection().entrySet()) {
                    user.getValue().close();
                }
                serverSocket.close();
                list.getUsersCollection().clear();
                serverGui.serverMessage("Server Is Stopped.\n");
            }
        } catch (Exception e) {
            serverGui.serverMessage("Could Not Stop The Server!\n");
        }
    }

    //method listens in infinite loop for new clients
    public void clientListener() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            } catch (Exception e) {
                serverGui.serverMessage("Connection with server has been lost.\n");
                break;
            }
        }
    }

    //method sends message to all clients
    public void sendMessage(Message message) {
        for (Map.Entry<String, Connection> user : list.getUsersCollection().entrySet()) {
            try {
                user.getValue().sendMessage(message);
            } catch (Exception e) {
                serverGui.serverMessage("Error could not send message to the users\n");
            }
        }
    }


    //thread when clients join the server
    private class ServerThread extends Thread {
        private final Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        private String requestAndAddingUser(Connection connection) {
            while (true) {
                try {
                    connection.sendMessage(new Message(Message.Category.REQUEST_NAME_USER));
                    Message responseMessage = connection.receiveMessage();
                    String userName = responseMessage.getTextMessage();

                    if (responseMessage.getMessageType() == Message.Category.USER_NAME && userName != null && !userName.isEmpty() && !list.getUsersCollection().containsKey(userName)) {
                        list.addUser(userName, connection);
                        Set<String> listUsers = new HashSet<>();
                        for (Map.Entry<String, Connection> users : list.getUsersCollection().entrySet()) {
                            listUsers.add(users.getKey());
                        }
                        connection.sendMessage(new Message(Message.Category.NAME_ACCEPTED, listUsers));
                        sendMessage(new Message(Message.Category.USER_ADDED, userName));
                        return userName;
                    }
                    else connection.sendMessage(new Message(Message.Category.NAME_USED));
                } catch (Exception e) {
                    serverGui.serverMessage("New user Could not be added. name is already in use.\n");
                }
            }
        }

        private void messagingBetweenUsers(Connection connection, String userName) {
            while (true) {
                try {
                    Message message = connection.receiveMessage();
                    if (message.getMessageType() == Message.Category.TEXT_MESSAGE) {
                        String textMessage = String.format("%s: %s\n", userName, message.getTextMessage());
                        sendMessage(new Message(Message.Category.TEXT_MESSAGE, textMessage));
                    }

                    if (message.getMessageType() == Message.Category.DISABLE_USER) {
                        sendMessage(new Message(Message.Category.REMOVED_USER, userName));
                        list.removeUser(userName);
                        connection.close();
                        serverGui.serverMessage(("user disconnected.\n"));
                        break;
                    }
                } catch (Exception e) {
                    serverGui.serverMessage("Could not send the message,"+userName+" maybe disconnected!\n");
                    break;
                }
            }
        }

        @Override
        public void run() {
            serverGui.serverMessage("new user has been connected.\n");
            try {
                Connection connection = new Connection(socket);
                String nameUser = requestAndAddingUser(connection);
                messagingBetweenUsers(connection, nameUser);
            } catch (Exception e) {
                serverGui.serverMessage("error! could not deliver message from user!\n");
            }
        }
    }
}






