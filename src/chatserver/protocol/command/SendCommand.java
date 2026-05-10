package chatserver.protocol.command;

import chatserver.protocol.ProtocolMessages;
import chatserver.service.MessageService;
import chatserver.service.message.SendResult;
import chatserver.session.SessionContext;


/**
 * This class handles the SEND command.
 * <p>
 * It's responsibilities are:
 * - Sends a message from one user to another
 * - Validates authentication and receiver existence
 * - Sends appropriate protocol responses
 */
public class SendCommand implements Command {

    // Used to send and store messages
    private final MessageService messageService;

    // Inject MEssageService dependency
    public SendCommand(MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void execute(SessionContext session, String args) {

        // Expects <receiver> <message text>
        String[] parts = args.trim().split("\\s+", 2);

        // If command format is invalid, treat as not authenticated
        if (parts.length < 2) {
            ProtocolMessages.send206(session);
            return;
        }

        String receiver = parts[0];
        String messageText = parts[1];

        // Ask the MessageService to process the message
        SendResult result = messageService.sendMessage(session, receiver, messageText);

        switch (result) {

            case SUCCESS:
                // Message sent successfully
                ProtocolMessages.send101(session);
                break;

            case RECEIVER_NOT_FOUND:
                // Target user does not exist
                ProtocolMessages.send200(session, receiver);
                break;

            case SENDER_NOT_AUTHENTICATED:
                // Sender is not logged in
                ProtocolMessages.send206(session);
                break;

        }
    }

}
