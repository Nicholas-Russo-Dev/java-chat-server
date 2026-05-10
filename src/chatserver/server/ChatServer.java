package chatserver.server;

import chatserver.protocol.CommandProcessor;
import chatserver.protocol.command.*;
import chatserver.repository.MessageStore;
import chatserver.repository.UserRegistry;
import chatserver.service.AuthService;
import chatserver.service.MessageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Map;


/**
 * This class is the main server class.
 * <p>
 * Its responsibilities are:
 * - Initializes core services and repositories
 * - Registers all supported commands
 * - Listens for incoming client connections
 * - Creates a new thread for each connected client
 */
public class ChatServer {

    // Port number the server listens on
    private static final int PORT = 7777;

    public static void main(String[] args) {

        // Stores registered users
        UserRegistry userRegistry = new UserRegistry();

        // Stores offline messages
        MessageStore messageStore = new MessageStore();

        // Handles sending messages and managing connections
        MessageService messageService =
                new MessageService(userRegistry, messageStore);

        // Handles authentication logic
        AuthService authService =
                new AuthService(userRegistry, messageService);

        // Map each command type to its implementation
        Map<CommandType, Command> commands =
                new EnumMap<>(CommandType.class);

        commands.put(CommandType.CRTE, new CreateUserCommand(userRegistry));

        commands.put(CommandType.AUTH, new AuthCommand(authService, messageService));

        commands.put(CommandType.SEND, new SendCommand(messageService));

        commands.put(CommandType.QUIT, new QuitCommand(messageService));

        // Responsible for processing client commands
        CommandProcessor commandProcessor =
                new CommandProcessor(commands);


        // Server Socket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("ChatServer running on port " + PORT);

            // Continuously accept new client connections
            while (true) {

                Socket clientSocket = serverSocket.accept();

                // Creates a handler for each connected client
                ClientHandler handler = new ClientHandler(
                        clientSocket,
                        commandProcessor,
                        messageService);

                // Run each client in its own thread
                Thread thread = new Thread(handler);
                thread.start();

            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }

    }

}
