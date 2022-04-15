package Client.authorizer;


import java.io.IOException;

public interface ClientAuthorizer {
    void register() throws IOException;

    void authorize() throws IOException;
}
