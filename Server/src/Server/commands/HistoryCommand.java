package Server.commands;

import Server.connection.response.ResponseCreator;
import general.*;

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
        char g = '5';
        if (history.size() < 5)
            g = (char) history.size();
        responseCreator.addToMsg("The list of last 5 commands:");
        for (Object hstr : history)
            responseCreator.addToMsg(((String) hstr).trim());
    }
}
