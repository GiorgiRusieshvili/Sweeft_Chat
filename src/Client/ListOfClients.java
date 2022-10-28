package Client;

import java.util.HashSet;
import java.util.Set;

public class ListOfClients {
    private Set<String> clients = new HashSet<>();

    public Set<String> getClients() {
        return clients;
    }

    public void addClient(String client) {
        clients.add(client);
    }

    public void removeClient(String client) {
        clients.remove(client);
    }

    public void setClients(Set<String> clients) {
        this.clients = clients;
    }
}

