package chatserver.protocol.command;

import chatserver.protocol.ProtocolMessages;
import chatserver.service.MessageService;
import chatserver.session.SessionContext;

/**
 * This class handles the QUIT command.
 * 
 * It's responsibilities are:
 * - Disconnect the user from the server
 * - Remove the user from the connected list
 * - Send a goodbye message
 * - Close the client session
 */
public class QuitCommand implements Command {

	// Used to manage connected users
	private final MessageService messageService;
	
	// Inject MEssageService dependency
	public QuitCommand(MessageService messageService) {
		this.messageService = messageService;
	}
	
	
	@Override
	public void execute(SessionContext session, String args) {
		
		// If the user is authenticated, remove them from active connections
		if (session.isAuthenticated()) {
			messageService.disconnect(session.getUsername());
		}
		
		// Send goodbye message
		ProtocolMessages.send103(session);
		
		// Close the client session/connection
		session.close();
	}
	
}
