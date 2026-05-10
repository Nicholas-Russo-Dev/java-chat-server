package chatserver.protocol.command;

import chatserver.domain.Message;
import chatserver.protocol.ProtocolMessages;
import chatserver.service.AuthService;
import chatserver.service.MessageService;
import chatserver.service.auth.AuthResult.AuthResult;
import chatserver.session.SessionContext;

import java.util.List;


/**
 * This class handles the AUTH command.
 * <p>
 * It's Responsibilities are:
 * - Validates user name and password
 * - Connects the user if authentication succeeds
 * - Sends appropriate protocol responses
 * - Delivers any stored offline messages
 */
public class AuthCommand implements Command {

    private final AuthService authService;
    private final MessageService messageService;

    // Inject the required services
    public AuthCommand(AuthService authService, MessageService messageService) {
        this.authService = authService;
        this.messageService = messageService;
    }


    @Override
    public void execute(SessionContext session, String args) {

        // Expects <user name> <password>
        String[] parts = args.trim().split("\\s+", 2);

        // If command format is invalid, treat as invalid credentials
        if (parts.length < 2) {
            ProtocolMessages.send204(session);
            return;
        }

        String username = parts[0];
        String password = parts[1];

        // Ask the AuthService to validate the credentials
        AuthResult result = authService.authenticate(session, username, password);

        switch (result) {

            case SUCCESS:
                // Send success message
                ProtocolMessages.send102(session, username);

                // Deliver any offline messages stored for this user
                List<Message> offline = messageService.takeOfflineMessages(username);

                for (Message msg : offline) {
                    ProtocolMessages.send100(session, msg.getSender(), msg.getText());
                }
                break;

            case USER_NOT_FOUND:
                ProtocolMessages.send200(session, username);
                break;

            case INVALID_PASSWORD:
                ProtocolMessages.send204(session);
                break;

            case ALREADY_CONNECTED:
                ProtocolMessages.send201(session, username);
                break;

            case SESSION_ALREADY_AUTHENTICATED:
                ProtocolMessages.send202(session, session.getUsername());
                break;
        }
    }

}
