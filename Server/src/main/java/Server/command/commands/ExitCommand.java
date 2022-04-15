package Server.command.commands;


import Server.command.ServerCommand;
import Server.server.Server;
import Server.server.ServerApp;

public class ExitCommand implements ServerCommand {
    private final ServerApp server;

    public ExitCommand(Server server) {
        this.server = server;
    }

    @Override
    public void execute() {
        server.exit();
    }
}
