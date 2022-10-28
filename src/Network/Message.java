package Network;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable {
    private final Set<String> listOfUsers;
    private final String textMessage;
    private final Category categoryMessage;

    public Message(Category categoryMessage, String textMessage) {
        this.textMessage = textMessage;
        this.categoryMessage = categoryMessage;
        this.listOfUsers = null;
    }

    public Message(Category categoryMessage, Set<String> listOfUsers) {
        this.categoryMessage = categoryMessage;
        this.textMessage = null;
        this.listOfUsers = listOfUsers;
    }

    public Message(Category categoryMessage) {
        this.categoryMessage = categoryMessage;
        this.textMessage = null;
        this.listOfUsers = null;
    }

    public Category getMessageType() {
        return categoryMessage;
    }

    public Set<String> getListOfUsers() {
        return listOfUsers;
    }

    public String getTextMessage() {
        return textMessage;
    }
    public enum Category {
        REQUEST_NAME_USER,
        TEXT_MESSAGE,
        NAME_ACCEPTED,
        USER_NAME,
        NAME_USED,
        USER_ADDED,
        DISABLE_USER,
        REMOVED_USER
    }
}

