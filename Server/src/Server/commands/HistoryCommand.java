package Server.commands;

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
        char lowThan5 = '5';
        if (history.size() < 5)
            lowThan5 = (char) history.size();
        responseCreator.addToMsg("The list of last " + lowThan5 + "commands:");
        for (Object hstr : history)
            responseCreator.addToMsg(((String) hstr).trim());
    }
}
