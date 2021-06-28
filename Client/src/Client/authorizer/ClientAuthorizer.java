package Client.authorizer;


import java.io.IOException;

public interface ClientAuthorizer {
    void registerClient() throws IOException;

    void authorizeUser() throws IOException;

}
