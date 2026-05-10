package chatserver.domain;

import java.util.Objects;



/**
 * This class represents a user account in the chat system.
 * 
 * It Stores:
 * - User name
 * - Password
 * - Whether the user is a superuser / first account created
 * 
 * This class validates inputs before storing any data
 * and is used by the authentication system.
 */
public class User {
	
	// The user's unique user name
	private final String username;
	
	// The user's password
	private final String password;
	
	// True if the user is the first created / superuser
	private final boolean superuser;
	
	
	// Creates a new User object
	public User(
			String username,
			String password,
			boolean superuser
			) {
		
		this.username = validate(username, "username");
		this.password = validate(password, "password");
		this.superuser = superuser;
	}
	
	
	/**
	 * Validates the user input before storing it inside of domain.
	 * 
	 * @param value
	 * @param fieldName
	 * @return value to store
	 */
	private String validate(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " cannot be null or blank.");
		}
		return value;
	}
	
	
	// Returns the user name
	public String getUsername() {
		return username;
	}
	
	
	// Returns the password
	public String getPassword() {
		return password;
	}
	
	
	// Returns true or false if the user is a superuser
	public boolean isSuperuser() {
		return superuser;
	}
	
	
	// Users are considered equal if their user names match each other
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof User other)) {
			return false;
		}
		
		return username.equals(other.username);
	}
	
	
	// This method generates a hash code based on the user name.
	// Required when equals() is overridden so that user
	// works correctly in a hash based collection.
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
	
	
	@Override
	public String toString() {
		return "User{" +
			   "username='" + username + '\'' +
			   ", superuser=" + superuser +
			   '}';
	}
	
}
