package chatserver.repository;

import chatserver.domain.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class stores and manages all registered users.
 * <p>
 * Uses a thread safe map because multiple clients may
 * create or authenticate users at the same time.
 */
public class UserRegistry {

    // Maps user name to a User object
    private final ConcurrentHashMap<String, User> users =
            new ConcurrentHashMap<>();


    /**
     * This method creates a new user account
     * The first created user becomes a superuser.
     * <p>
     * Returns:
     * - Optional containing the created user
     * - Empty Optional if the user name already exists
     */
    public Optional<User> createUser(String username, String password) {

        boolean isFirstUser = users.isEmpty();

        User newUser = new User(username, password, isFirstUser);

        User existing = users.putIfAbsent(username, newUser);

        if (existing != null) {
            return Optional.empty(); // User name already exists
        }

        return Optional.of(newUser);

    }


    /**
     * This method attempts to authenticate a user.
     * <p>
     * Returns:
     * - Optional containing the user if credentials are valid
     * - Empty Optional if user name does not exist or password is incorrect
     */
    public Optional<User> authenticate(String username, String password) {

        User user = users.get(username);

        if (user == null) {
            return Optional.empty(); // User doesn't exist
        }

        if (!user.getPassword().equals(password)) {
            return Optional.empty(); // Invalid password
        }

        return Optional.of(user);
    }


    // Returns true if a user with this user name already exists.
    public boolean exists(String username) {
        return users.containsKey(username);
    }


    // Retrieves a user by user name.
    public Optional<User> find(String username) {
        return Optional.ofNullable(users.get(username));
    }


    // Returns true if no users have been created yet.
    public boolean isEmpty() {
        return users.isEmpty();
    }

}
