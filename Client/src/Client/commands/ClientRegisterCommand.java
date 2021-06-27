package Client.commands;

import Client.authorizer.ClientAuthorizer;
import general.AbstractCommand;

import java.io.IOException;

public class ClientRegisterCommand extends AbstractCommand {
    private final ClientAuthorizer authorizer;

    public ClientRegisterCommand(ClientAuthorizer authorizer) {
        super("register", "User registration at server app", false);
        this.authorizer = authorizer;
    }

    @Override
    public void execute(String[] args) {
        try {
            authorizer.registerClient();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
