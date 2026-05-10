package chatserver.service;

import chatserver.domain.User;
import chatserver.repository.UserRegistry;
import chatserver.service.auth.AuthResult.AuthResult;
import chatserver.session.SessionContext;

import java.util.Optional;


/**
 * This class handles user authentication logic.
 * <p>
 * Responsibilities:
 * - Verify user name and password
 * - Ensure user is not already connected
 * - Update session state upon successful login
 */
public class AuthService {

    // Access to registered users
    private final UserRegistry userRegistry;

    // Used to manage connected users
    private final MessageService messageService;

    public AuthService(UserRegistry userRegistry, MessageService messageService) {
        this.userRegistry = userRegistry;
        this.messageService = messageService;
    }


    /**
     * Attempts to authenticate a user.
     * <p>
     * Returns an AuthResult indicating:
     * - SUCCESS
     * - USER_NOT_FOUND
     * - INVALID_PASSWORD
     * - ALREADY_CONNECTED
     * - SESSION_ALREADY_AUTHENTICATED
     */
    public AuthResult authenticate(SessionContext session, String username, String password) {

        // If this session is already authenticated
        if (session.isAuthenticated()) {
            return AuthResult.SESSION_ALREADY_AUTHENTICATED;
        }


        // Check if the user exists
        Optional<User> userOptional = userRegistry.find(username);
        if (userOptional.isEmpty()) {
            return AuthResult.USER_NOT_FOUND;
        }

        User user = userOptional.get();

        // Validate password
        if (!user.getPassword().equals(password)) {
            return AuthResult.INVALID_PASSWORD;
        }

        // Check if user is already connected in another session
        if (messageService.isConnected(username)) {
            return AuthResult.ALREADY_CONNECTED;
        }

        // Authentication successful
        session.authenticate(user);
        messageService.connect(username, session);

        return AuthResult.SUCCESS;
    }

}
