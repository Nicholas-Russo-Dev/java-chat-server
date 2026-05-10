package chatserver.service.message;


/**
 * Represents the possible results of sending a message.
 * <p>
 * Used by MessageService to indicate whether the message
 * was delivered successfully or failed.
 */
public enum SendResult {

    // Message sent successfully
    SUCCESS,

    // Sender is not authenticated
    SENDER_NOT_AUTHENTICATED,

    // Receiver user name does not exist
    RECEIVER_NOT_FOUND;
}
