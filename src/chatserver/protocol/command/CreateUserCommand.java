package chatserver.protocol.command;

import java.util.Optional;

import chatserver.domain.User;
import chatserver.protocol.ProtocolMessages;
import chatserver.repository.UserRegistry;
import chatserver.session.SessionContext;


/**
 * This class handles the CRTE command.
 * 
 * Responsibilities:
 * - Creates a new user account
 * - Sends appropriate protocol responses
 * - Determines if the user is the first user / superuser
 */
public class CreateUserCommand implements Command {

	// Access to user storage
	private final UserRegistry userRegistry;
	
	// Inject the UserRegistry dependency
	public CreateUserCommand(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
	
	
	@Override
	public void execute(SessionContext session, String args) {
		
		// Expects <user name> <password>
		String[] parts = args.trim().split("\\s+", 2);
		
		if (parts.length < 2) {
			// Invalid format and we shall treat it as an invalid credentials
			ProtocolMessages.send204(session);
			return;
		}
		
		String username = parts[0];
		String password = parts[1];
		
		// Attempt to create the user
		Optional<User> created = userRegistry.createUser(username, password);
		
		// If user already exists, send error 203
		if (created.isEmpty()) {
			ProtocolMessages.send203(session, username);
			return;
		}
		
		User user = created.get();
		
		// First created user becomes the superuser
		if (user.isSuperuser()) {
			ProtocolMessages.send105(session, username);
		} else {
			ProtocolMessages.send104(session, username);
		}
	}
	
}
