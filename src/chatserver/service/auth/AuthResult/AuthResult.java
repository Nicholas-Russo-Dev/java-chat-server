package chatserver.service.auth.AuthResult;


/**
 * Represents the possible results of an authentication attemp.
 * <p>
 * Used by AuthService to indicate what happened during
 * the login process.
 */
public enum AuthResult {

    // Authentication successful
    SUCCESS,

    // User name does not exist
    USER_NOT_FOUND,

    // Password does not match
    INVALID_PASSWORD,

    // User is already connected in another session
    ALREADY_CONNECTED,

    // This session is already authenticated
    SESSION_ALREADY_AUTHENTICATED;
}
