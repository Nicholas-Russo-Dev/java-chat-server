package chatserver.domain;

import java.time.Instant;
import java.util.Objects;


/**
 * This class represents a single chat message.
 * <p>
 * It stores:
 * - Who sent the message
 * - The message text
 * - And the time the message was created
 * <p>
 * This class is immutable and is used by the messaging system.
 */
public final class Message {

    // User name of the send
    private final String sender;

    // Text content of the message
    private final String text;

    // Time stamp of when the message was created
    private final Instant timestamp;

    // Creates a new message object
    public Message(String sender, String text, Instant timestamp) {
        this.sender = validate(sender, "sender");
        this.text = validate(text, "text");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    // Ensures a string is not null or blank
    private String validate(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be null or blank.");
        }
        return value;
    }


    // Returns the sender's user name
    public String getSender() {
        return sender;
    }


    // Returns the message text
    public String getText() {
        return text;
    }


    // Returns when the message was created
    public Instant getTimeStamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
