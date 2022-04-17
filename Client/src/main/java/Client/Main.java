package Client;


import Client.authorizer.ClientAuthorizer;
import Client.authorizer.ClientAuthorizerImpl;
import Client.client.Client;
import Client.client.ClientApp;
import Client.commands.ClientCommandReaderImpl;
import Client.connection.ClientConnectionManager;
import Client.connection.ClientConnectionManagerImpl;
import Client.connection.request.RequestSender;
import Client.connection.request.RequestSenderImpl;
import Client.connection.response.ResponseReader;
import Client.connection.response.ResponseReaderImpl;
import general.IO;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1)
            IO.errPrint("Input arguments: [port]");

        try {
            ClientConnectionManager manager = new ClientConnectionManagerImpl();
            RequestSender sender = new RequestSenderImpl();
            ResponseReader reader = new ResponseReaderImpl();

            int port = Integer.parseInt(args[0]);

            ClientAuthorizer authorizer = new ClientAuthorizerImpl(
                    manager,
                    sender,
                    reader,
                    port
            );

            ClientApp client = new Client(new ClientCommandReaderImpl(),
                    manager,
                    sender,
                    reader,
                    authorizer,
                    port
            );
            IO.println("The work is started:\nEnter 'client_help' for help");

            client.start(port);
        } catch (NumberFormatException e) {
            IO.errPrint(e.getMessage());
        }
    }
}
