package chatserver.repository;

import chatserver.domain.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;


/**
 * This class stores messages for users who are currently offline.
 * <p>
 * Uses the thread safe collections because multiple clients may access
 * this store at the same time.
 */
public class MessageStore {

    // Maps user names to queue of pending offline messages
    private final ConcurrentMap<String, Queue<Message>> offlineMessages =
            new ConcurrentHashMap<>();


    /**
     * Stores a message for a recipient who is offline.
     * If no queue exists for the user, one is created.
     */
    public void store(String recipient, Message message) {
        offlineMessages
                .computeIfAbsent(recipient, key -> new ConcurrentLinkedQueue<>())
                .add(message);
    }


    /**
     * Retrieves and removes all stored messages for a user.
     * After this call, the user has no pending offline messages.
     */
    public List<Message> takeAll(String recipient) {

        Queue<Message> queue = offlineMessages.remove(recipient);

        if (queue == null) {
            return List.of();
        }

        return new ArrayList<>(queue);
    }


    // Checks if a user has any pending offline messages.
    public boolean hasMessages(String recipient) {
        Queue<Message> queue = offlineMessages.get(recipient);
        return queue != null && !queue.isEmpty();
    }

}
