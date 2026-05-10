package chatserver.session;

import java.io.Closeable;
import java.io.PrintStream;
import java.util.Objects;

import chatserver.domain.Message;
import chatserver.domain.User;


/**
 * This class represents the state of a single client session.
 * 
 * Responsibilities:
 * - Track authentication state
 * - Send protocol messages to the client
 * - Ensure thread safe writes to the output stream
 */
public class SessionContext implements Closeable {

	// Output stream to the client
	private final PrintStream out;
	
	// Currently authenticated user if any
	private volatile User authenticatedUser;
	
	// Ensures multiple threads do not write at the same time
	private final Object writeLock = new Object();
	
	public SessionContext(PrintStream out) {
		this.out = Objects.requireNonNull(out, "out cannot be null");
	}
	
	// Returns true if this session is authenticated
	public boolean isAuthenticated() {
		return authenticatedUser != null;
	}
	
	// Returns the authenticated user name or null if not logged in
	public String getUsername() {
		User user = authenticatedUser;
		return (user == null) ? null : user.getUsername();
	}
	
	// Returns the authenticated User object
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	// Marks this session as authenticated
	public void authenticate(User user) {
		this.authenticatedUser = Objects.requireNonNull(user, "user cannot be null");
	}
	
	
	/**
	 * Sends a single line to the client.
	 * Thread safe to prevent interleaved output.
	 */
	public void sendLine(String line) {
		Objects.requireNonNull(line, "line cannot be null");
		synchronized (writeLock) {
			out.println(line);
			out.flush();
		}
	}
	
	
	
	/**
	 * Sends an incoming message to this client.
	 * Used by MessageService for immediate delivery.
	 */
	public void sendIncomingMessage(Message message) {
		Objects.requireNonNull(message, "message cannot be null");
		sendLine("100 Message from " + message.getSender() + " follows: \"" + message.getText() + "\"");
	}
	
	// Closes the session and underlying output stream.
	@Override
	public void close() {
		synchronized (writeLock) {
			out.flush();
		}
		out.close();
	}
	
}
