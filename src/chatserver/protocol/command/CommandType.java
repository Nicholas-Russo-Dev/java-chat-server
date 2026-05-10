package chatserver.protocol.command;

import java.util.Arrays;
import java.util.Optional;


/**
 * This Enum represents all of the supported command keywords.
 * <p>
 * Each value matches a valid command that the user can type
 * in the client.
 */
public enum CommandType {

    // Create a new user account
    CRTE,

    // Authenticate an existing user
    AUTH,

    // Send a message to another user
    SEND,

    // Disconnect the user from the server
    QUIT;

    /**
     * This method attempts to convert a string keyword into a CommandType.
     * <p>
     * This method returns:
     * - Optional containing the matching command
     * - Empty Optional if the command does not exist
     */
    public static Optional<CommandType> from(String keyword) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(keyword))
                .findFirst();
    }
}
