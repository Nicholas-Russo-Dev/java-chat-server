package chatserver.protocol;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import chatserver.protocol.command.Command;
import chatserver.protocol.command.CommandType;
import chatserver.session.SessionContext;


/**
 * This class is responsible for processing the incoming client commands.
 * 
 * It,
 * - Parses the command keyword
 * - Finds the matching Command object
 * - Executes the command
 * - Sends error 205 if the command is invalid
 */
public class CommandProcessor {

	// Stores all available commands mapped by their type
	private final Map<CommandType, Command> commandMap =
			new EnumMap<>(CommandType.class);
	
	// Initializes the processor with all supported commands
	public CommandProcessor(Map<CommandType, Command> commands) {
		this.commandMap.putAll(commands);
	}
	
	
	// Processes a single line of input from a client
	public void process(SessionContext session, String line) {
		
		// Ignore empty inputs
		if (line == null || line.isBlank()) {
			return;
		}
		
		
		// Split into command keyword and arguments
		String[] parts = line.trim().split("\\s+", 2);
		
		String keyword = parts[0];
		String args = parts.length > 1 ? parts[1] : "";
		
		// Attempt to convert the keyword into a CommandType
		Optional<CommandType> typeOptional = CommandType.from(keyword);
		
		// If command does not exist, send error 205
		if (typeOptional.isEmpty()) {
			ProtocolMessages.send205(session, keyword);
			return;
		}
		
		CommandType type = typeOptional.get();
		Command command = commandMap.get(type);
		
		// If command is not registered, send error 205 message
		if (command == null) {
			ProtocolMessages.send205(session, keyword);
			return;
		}
		
		// Execute the command with the provided arguments from the user
		command.execute(session, args);
	}
	
}
