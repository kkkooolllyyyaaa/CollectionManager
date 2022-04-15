package Server.command.commands;


import Server.connection.response.ResponseCreator;
import general.*;

import java.util.Formatter;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {
    private final HashMap<String, AbstractCommand> commandMap;
    private final ResponseCreator responseCreator;

    public HelpCommand(HashMap<String, AbstractCommand> commandMap, ResponseCreator responseCreator) {
        super("help", " : вывести справку по доступным командам");
        this.commandMap = commandMap;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        for (String it : commandMap.keySet().stream().sorted().collect(Collectors.toList())) {
            Formatter formatter = new Formatter();
            formatter.format("%-24s", commandMap.get(it).getName());
            formatter.format("%s", " " + commandMap.get(it).getDescription());
            responseCreator.addToMsg(formatter.toString());
        }
    }
}
