package Client.commands;


import general.AbstractCommand;

import java.util.TreeMap;

public class ClientHelpCommand extends AbstractCommand {
    private final ClientCommandReader reader;

    public ClientHelpCommand(ClientCommandReader reader) {
        super("client_help", "Show all client commands name and description");
        this.reader = reader;
    }

    @Override
    public void execute(String[] args) {
        TreeMap<String, AbstractCommand> map = reader.getCommandMap();
        for (String key : map.keySet()) {
            AbstractCommand command = map.get(key);
            System.out.printf("%-14s", command.getName());
            System.out.printf("%s", " : " + command.getDescription() + "\n");
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
