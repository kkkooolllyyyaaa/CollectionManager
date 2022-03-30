package Client.commands;


import Client.client.ClientApp;
import general.AbstractCommand;

public class ClientExitCommand extends AbstractCommand {
    ClientApp clientApp;

    public ClientExitCommand(ClientApp clientApp) {
        super("exit", "Finish client app work");
        this.clientApp = clientApp;
    }

    @Override
    public void execute(String[] args) {
        clientApp.exit();
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
