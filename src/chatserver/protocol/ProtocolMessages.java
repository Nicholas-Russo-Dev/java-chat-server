package chatserver.protocol;

import chatserver.session.SessionContext;


/**
 * This class contains all protocol messages used by the server.
 * 
 * Each method sends a formatted response back to the client.
 * This keeps protocol formatting separate from the actual software logic
 */
public final class ProtocolMessages {

	// Prevent instantiation
	private ProtocolMessages() {
		
	}
	
	
	// 100, sent when a user receives a message
	public static void send100(SessionContext session, String user, String message) {
		session.sendLine("100 Message from " + user + " follows: \"" + message + "\"");
	}
	
	// 101, confirms a message was sent successfully
	public static void send101(SessionContext session) {
		session.sendLine("101 Message sent.");
	}
	
	// 102, confirms successful authentication
	public static void send102(SessionContext session, String user) {
		session.sendLine("102 Connected as " + user + ".");
	}
	
	// 103, sent when a user disconnects
	public static void send103(SessionContext session) {
		session.sendLine("103 Bye.");
	}
	
	// 104, sent when a normal user account is created
	public static void send104(SessionContext session, String user) {
		session.sendLine("104 User " + user + " created.");
	}
	
	// 105, sent when the first user (superuser) is created
	public static void send105(SessionContext session, String user) {
		session.sendLine("105 User " + user + " created as superuser.");
	}
	
	
	
	// 200, sent when a referenced user does not exist
	public static void send200(SessionContext session, String user) {
		session.sendLine("200 User " + user + " doesn't exist.");
	}
	
	// 201, sent when a user is already connected
	public static void send201(SessionContext session, String user) {
		session.sendLine("201 User " + user + " already connected.");
	}
	
	// 202, sent when the current session is already authenticated
	public static void send202(SessionContext session, String user) {
		session.sendLine("202 Already connected as " + user + ".");
	}
	
	// 203, sent when trying to create a user that already exists
	public static void send203(SessionContext session, String user) {
		session.sendLine("203 User " + user + " already exists.");
	}
	
	// 204, sent when user name or password is invalid
	public static void send204(SessionContext session) {
		session.sendLine("204 Invalid user name or password.");
	}
	
	// 205, sent when an unknown command is entered
	public static void send205(SessionContext session, String keyword) {
		session.sendLine("205 No such command \"" + keyword + "\".");
	}
	
	// 206, sent when a user tries an action without authentication
	public static void send206(SessionContext session) {
		session.sendLine("206 Not connected as a user.");
	}
	
}
