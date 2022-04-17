package Server.command.commands;

import Server.command.ServerCommandReader;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;
import general.LimitedQueue;

public class HistoryCommand extends AbstractCommand {
    private final ServerCommandReader serverCommandReader;
    private final ResponseCreator responseCreator;

    public HistoryCommand(ServerCommandReader serverCommandReader, ResponseCreator responseCreator) {
        super("history", " : вывести последние 5 команд (без их аргументов)");
        this.serverCommandReader = serverCommandReader;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        LimitedQueue<String> history = serverCommandReader.getHistory();
        responseCreator.addToMsg("The list of last 5 commands:");
        for (String command : history)
            responseCreator.addToMsg(command);
    }
}
