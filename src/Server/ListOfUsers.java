package Server;

import Network.Connection;

import java.util.HashMap;
import java.util.Map;

//class stores all users in a collection
public class ListOfUsers {

    private final Map<String,Connection> usersCollection = new HashMap<>();
    public Map<String, Connection> getUsersCollection() {
        return usersCollection;
    }
    public void addUser(String user, Connection connection) {
        usersCollection.put(user, connection);
    }
    public void removeUser(String user) {
        usersCollection.remove(user);
    }
}

