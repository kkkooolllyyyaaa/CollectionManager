package Client.commands;


import general.AbstractCommand;

import java.util.Collection;

public class ClientHelpCommand extends AbstractCommand {
    private final ClientCommandReader reader;

    public ClientHelpCommand(ClientCommandReader reader) {
        super("client_help", "Show all client commands name and description");
        this.reader = reader;
    }

    @Override
    public void execute(String[] args) {
        Collection<AbstractCommand> map = reader.getCommands();
        for (AbstractCommand command : map) {
            System.out.printf("%-14s", command.getName());
            System.out.printf("%s", " : " + command.getDescription() + "\n");
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
