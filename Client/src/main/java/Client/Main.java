package Client;


import Client.authorizer.ClientAuthorizer;
import Client.authorizer.ClientAuthorizerFunctional;
import Client.client.Client;
import Client.client.ClientApp;
import Client.commands.ClientCommandReaderFunctional;
import Client.connection.ClientConnectionManager;
import Client.connection.ClientConnectionManagerImpl;
import Client.connection.request.RequestSender;
import Client.connection.request.RequestSenderImpl;
import Client.connection.response.ResponseReader;
import Client.connection.response.ResponseReaderImpl;

public class Main {
    public static final void main(String[] args) {
        if (args.length == 1) {
            try {
                ClientConnectionManager manager = new ClientConnectionManagerImpl();
                RequestSender sender = new RequestSenderImpl();
                ResponseReader reader = new ResponseReaderImpl();
                ClientAuthorizer authorizer = new ClientAuthorizerFunctional(manager, sender, reader);
                ClientApp client = new Client(new ClientCommandReaderFunctional(),
                        manager,
                        sender,
                        reader,
                        authorizer,
                        Integer.parseInt(args[0])
                );
                System.out.println("The work is started:\nEnter 'client_help' for help");
                client.start(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } else
            System.err.println("Input arguments: port");
    }
}