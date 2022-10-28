package Network;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable {

    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    //ctr
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }
    //method receives messages via socket connection
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        synchronized (this.inputStream) {
            return (Message) inputStream.readObject();
        }
    }
    //method sends messages via socket connection
    public void sendMessage(Message message) throws IOException {
        synchronized (this.outputStream) {
            outputStream.writeObject(message);
        }
    }
    //method closes all connections socket, reader and writer
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }

}


