package chatserver.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import chatserver.protocol.CommandProcessor;
import chatserver.service.MessageService;
import chatserver.session.SessionContext;


/**
 * This class handles communication with a single connected client.
 * 
 * Responsibilities:
 * - Read input from the client
 * - Pass commands to the CommandPrcoessor
 * - Manage session life cycle
 * - Clean up when the client disconnects
 * 
 * Each ClientHandler runs in its own thread.
 */
public class ClientHandler implements Runnable {

	// The client socket connection
	private final Socket socket;
	
	// Responsible for processing client commands
	private final CommandProcessor commandProcessor;
	
	// Used to manage connected users
	private final MessageService messageService;
	
	public ClientHandler(Socket socket,
			CommandProcessor commandProcessor,
			MessageService messageService) {
		
		this.socket = socket;
		this.commandProcessor = commandProcessor;
		this.messageService = messageService;
	}
	
	
	@Override
	public void run() {
		
		try (
			
				// Read input from client
				Scanner input = new Scanner(socket.getInputStream());
				
				// Send output to client
				PrintStream output = new PrintStream(socket.getOutputStream());
				
		) {
			
			// Create a session for this client
			SessionContext session = new SessionContext(output);
			
			// Send welcome message when client connects
			sendWelcome(session);
			
			// Continuously read client input
			while (input.hasNextLine()) {
				
				String line = input.nextLine();
				
				// Process the command
				commandProcessor.process(session, line);
				
				// Stop loop if connection is closed
				if (!socket.isConnected() || socket.isClosed()) {
					break;
				}
			}
			
			// If user was authenticated, remove from active connection
			if (session.isAuthenticated()) {
				messageService.disconnect(session.getUsername());
			}
			
		} catch (IOException e) {
			// Client likely disconnected unexpectedly
			System.err.println("Client connection error: " + e.getMessage());
		} finally {
			// Ensure socket is closed
			try {
				socket.close();
			} catch (IOException ignored) {
				
			}
		}
		
	}
	
	
	
	/**
	 * This method is used as a welcome message that lists the commands
	 * and inputs for each command that the software shall accept
	 */
	private void sendWelcome(SessionContext session) {
		
		session.sendLine("Welcome to the Chat Server");
		session.sendLine("Available Commands:");
		session.sendLine("");
		session.sendLine("CRTE <username> <password> - Create An Account");
		session.sendLine("AUTH <username> <password> - Authenticate");
		session.sendLine("SEND <user> <message>      - Send a message");
		session.sendLine("QUIT                       - Disconnect");
		session.sendLine("");
	}
}
