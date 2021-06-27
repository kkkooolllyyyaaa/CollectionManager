package Client.commands;


import Client.authorizer.ClientAuthorizer;
import general.AbstractCommand;

import java.io.IOException;

public class ClientAuthCommand extends AbstractCommand {
    private final ClientAuthorizer authorizer;

    public ClientAuthCommand(ClientAuthorizer authorizer) {
        super("auth", "User Authorization at server app", false);
        this.authorizer = authorizer;
    }

    @Override
    public void execute(String[] args) {
        try {
            authorizer.authorizeUser();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}
