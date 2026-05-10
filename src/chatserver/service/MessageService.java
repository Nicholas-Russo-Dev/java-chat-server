package chatserver.service;

import chatserver.domain.Message;
import chatserver.repository.MessageStore;
import chatserver.repository.UserRegistry;
import chatserver.service.message.SendResult;
import chatserver.session.SessionContext;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * This class handles delivery and connection management.
 * <p>
 * Responsibilities:
 * - Track which users are currently connected
 * - Send messages between users
 * - Store messages for offline users
 */
public class MessageService {

    // Access to registered users
    private final UserRegistry userRegistry;

    // Stores offline messages
    private final MessageStore messageStore;

    private final ConcurrentMap<String, SessionContext> connectedUsers =
            new ConcurrentHashMap<>();


    // Maps user name to an active session
    public MessageService(UserRegistry userRegistry, MessageStore messageStore) {
        this.userRegistry = userRegistry;
        this.messageStore = messageStore;
    }


    // Marks a user as connected
    public void connect(String username, SessionContext session) {
        connectedUsers.put(username, session);
    }


    // Removes a user from the connected list
    public void disconnect(String username) {
        if (username != null) {
            connectedUsers.remove(username);
        }
    }


    // Returns true if the user is currently connected
    public boolean isConnected(String username) {
        return connectedUsers.containsKey(username);
    }


    /**
     * Sends a message from one user to another.
     * <p>
     * Returns a SendResult indicating:
     * - SUCCESS
     * - RECEIVER_NOT_FOUND
     * - SENDER_NOT_AUTHENTICATED
     */
    public SendResult sendMessage(SessionContext senderSession,
                                  String receiverUsername,
                                  String text) {

        // Sender must be authenticated
        if (!senderSession.isAuthenticated()) {
            return SendResult.SENDER_NOT_AUTHENTICATED;
        }


        // Receiver must exist
        if (!userRegistry.exists(receiverUsername)) {
            return SendResult.RECEIVER_NOT_FOUND;
        }

        String sender = senderSession.getUsername();

        // Create message object with current time stamp
        Message message = new Message(sender, text, Instant.now());

        // Check if receiver is currently connected
        SessionContext receiverSession = connectedUsers.get(receiverUsername);


        if (receiverSession != null) {
            // Deliver message immediately
            receiverSession.sendIncomingMessage(message);
        } else {
            // Store message for later delivery
            messageStore.store(receiverUsername, message);
        }

        return SendResult.SUCCESS;
    }


    // Retrieves and clears all offline messages for a user
    public List<Message> takeOfflineMessages(String username) {
        return messageStore.takeAll(username);
    }

}
