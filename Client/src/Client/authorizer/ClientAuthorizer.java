package Client.authorizer;


import general.User;

import java.io.IOException;

public interface ClientAuthorizer {
    void registerClient() throws IOException;

    void authorizeUser() throws IOException;

    User getUser();
}
