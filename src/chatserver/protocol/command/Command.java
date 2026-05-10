package chatserver.protocol.command;

import chatserver.session.SessionContext;

public interface Command {

    /**
     * Executes this command using the provided session and raw argument string.
     *
     * @param session current client session
     * @param args    raw text after the command keyword (may be empty)
     */
    void execute(SessionContext session, String args);

}
