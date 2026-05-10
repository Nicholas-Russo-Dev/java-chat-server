# Java Chat Server

A command-based Java socket chat server that supports account creation, authentication, direct messaging, offline message storage, and offline message delivery.

The server runs locally on port `7777` and can be tested using Telnet or any TCP socket client.

---

## Quickstart

### 1. Clone the Repository

```cmd
git clone https://github.com/Nicholas-Russo-Dev/java-chat-server.git
cd java-chat-server
```

### 2. Open the Project in IntelliJ IDEA

Open IntelliJ IDEA and select:

```text
File → Open
```

Then choose the project folder.

Make sure the `src` folder is marked as a source root:

```text
Right-click src → Mark Directory as → Sources Root
```

### 3. Run the Server

Run the main server class.

The main class should be located near:

```text
chatserver.server.ChatServer
```

If the server starts successfully, the IntelliJ console should display:

```text
ChatServer running on port 7777
```

The server is now running and waiting for client connections.

---

## Code Sample

The main source code is located in the `src/chatserver` directory.

Key areas of the project include:

| Package | Purpose |
|---|---|
| `server` | Starts the socket server and accepts client connections |
| `session` | Handles connected client sessions |
| `protocol` | Parses commands and formats server responses |
| `service` | Contains user and message business logic |
| `repository` | Stores and retrieves user and message data |
| `domain` | Contains core application objects |

---

## Overview

This project demonstrates a basic client/server chat system built with core Java.

The server listens for TCP socket connections on port `7777`. Once a client connects, the server displays a command menu and waits for text-based commands.

Users can create accounts, authenticate, send messages to other users, and disconnect from the server.

If a user receives a message while offline, the server stores the message and delivers it the next time that user logs in.

---

## Features

- Java socket-based server
- Command-based protocol
- User account creation
- Basic user authentication
- Direct user-to-user messaging
- Offline message storage
- Offline message delivery after login
- Multi-client connection handling
- Manual testing with Telnet

---

## Command Line Usage

After starting the server, connect using Telnet:

```cmd
telnet localhost 7777
```

Once connected, the server displays:

```text
Welcome to the Chat Server
Available Commands:

CRTE <username> <password> - Create An Account
AUTH <username> <password> - Authenticate
SEND <user> <message>      - Send a message
QUIT                       - Disconnect
```

---

## Commands

### Create an Account

Creates a new user account.

```text
CRTE <username> <password>
```

Example:

```text
CRTE Nick 1234
```

---

### Authenticate

Authenticates an existing user.

```text
AUTH <username> <password>
```

Example:

```text
AUTH Nick 1234
```

Successful response:

```text
102 Connected as Nick.
```

If the user has offline messages, they are delivered after authentication:

```text
100 Message from Bob follows: "hello"
```

---

### Send a Message

Sends a message to another user.

```text
SEND <user> <message>
```

Example:

```text
SEND Bob hello
```

If the recipient is online, the message is delivered immediately.

If the recipient is offline, the server stores the message and delivers it the next time that user authenticates.

---

### Disconnect

Disconnects the client from the server.

```text
QUIT
```

Example:

```text
QUIT
```

---

## Example Session

```text
Welcome to the Chat Server
Available Commands:

CRTE <username> <password> - Create An Account
AUTH <username> <password> - Authenticate
SEND <user> <message>      - Send a message
QUIT                       - Disconnect

AUTH Nick 1234
102 Connected as Nick.
100 Message from Bob follows: "hello"
```

---

## Testing with Multiple Users

To test communication between two users, open two separate Telnet windows.

### Terminal 1

Connect to the server:

```cmd
telnet localhost 7777
```

Create and authenticate as Nick:

```text
CRTE Nick 1234
AUTH Nick 1234
```

### Terminal 2

Connect to the server:

```cmd
telnet localhost 7777
```

Create and authenticate as Bob:

```text
CRTE Bob 1234
AUTH Bob 1234
```

Bob can send Nick a message:

```text
SEND Nick hello
```

Nick should receive:

```text
100 Message from Bob follows: "hello"
```

---

## Enable Telnet on Windows

Telnet is disabled by default on many Windows machines. If the `telnet` command is not recognized, you need to enable the Telnet Client first.

### Option 1: Enable Telnet with Command Prompt

Open **Command Prompt as Administrator**.

To do this:

1. Press the Windows key.
2. Search for `Command Prompt`.
3. Right-click **Command Prompt**.
4. Select **Run as administrator**.

Then run:

```cmd
dism /online /Enable-Feature /FeatureName:TelnetClient
```

If Command Prompt is not opened as administrator, Windows may show this error:

```text
Error: 740

Elevated permissions are required to run DISM.
Use an elevated command prompt to complete these tasks.
```

This means you need to reopen Command Prompt using **Run as administrator** and run the command again.

After installation, connect to the server with:

```cmd
telnet localhost 7777
```

### Option 2: Enable Telnet from Windows Features

You can also enable Telnet through the Windows Features menu:

1. Press the Windows key.
2. Search for `Turn Windows features on or off`.
3. Open it.
4. Check **Telnet Client**.
5. Click **OK**.

After Windows finishes installing Telnet, connect to the server with:

```cmd
telnet localhost 7777
```

---

## Testing the Server with Telnet

Make sure the Java server is running first.

In IntelliJ IDEA, the console should show:

```text
ChatServer running on port 7777
```

Then open Command Prompt or PowerShell and run:

```cmd
telnet localhost 7777
```

You should see the server welcome message:

```text
Welcome to the Chat Server
Available Commands:

CRTE <username> <password> - Create An Account
AUTH <username> <password> - Authenticate
SEND <user> <message>      - Send a message
QUIT                       - Disconnect
```

You can now type commands directly into the Telnet window.

Example:

```text
AUTH Nick 1234
```

Successful response:

```text
102 Connected as Nick.
```

To disconnect, type:

```text
QUIT
```

---

## Project Structure

```text
src
└── chatserver
    ├── domain
    ├── protocol
    ├── repository
    ├── server
    ├── service
    └── session
```

| Package | Description |
|---|---|
| `domain` | Contains core application objects |
| `protocol` | Handles command parsing and response formatting |
| `repository` | Stores and retrieves user and message data |
| `server` | Starts the socket server and accepts connections |
| `service` | Contains user and message business logic |
| `session` | Handles connected client sessions |

---

## How It Works

The server follows a simple request/response model.

1. The server starts and listens on port `7777`.
2. A client connects using Telnet or another TCP client.
3. The server sends a welcome message and command list.
4. The client sends a text command.
5. The server parses the command.
6. The server performs the requested action.
7. The server sends a response back to the client.

For example:

```text
AUTH Nick 1234
```

The server reads the command, checks the username and password, authenticates the session, and responds:

```text
102 Connected as Nick.
```

If any offline messages exist for the user, they are sent immediately after login.

---

## Technologies Used

- Java
- Java Sockets
- IntelliJ IDEA
- Telnet

---

## Security Notes

This project is intended for local testing and educational use.

The current implementation uses plain text socket communication, so usernames, passwords, and messages are not encrypted in transit. This keeps the project simple and focused on socket communication, command parsing, session handling, and message delivery.

A production-ready version would require additional security features, such as:

- Password hashing
- Encrypted socket communication
- Stronger input validation
- Persistent database storage
- Improved error handling
- Logging

---

## Possible Future Improvements

Possible future improvements include:

- Add password hashing
- Add persistent database storage
- Add a Java client application
- Add private chat sessions
- Add group messaging
- Add message timestamps
- Add unit tests
- Add Maven or Gradle build support
- Add logging
- Add more detailed protocol error responses

---

## Author

Nicholas Russo
