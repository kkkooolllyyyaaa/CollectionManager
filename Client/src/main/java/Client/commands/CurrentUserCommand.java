package Client.commands;

import Client.client.Client;
import general.AbstractCommand;

public class CurrentUserCommand extends AbstractCommand {
    public CurrentUserCommand() {
        super("current_user", "Show the active user on the client app");
    }

    @Override
    public void execute(String[] args) {
        if (Client.getCurrentUser() == null) {
            System.out.println("There is no active user on client app");
        } else
            System.out.println(Client.getCurrentUser().getUserName());
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
