package Client.authorizer;


import Client.client.Client;
import Client.connection.ClientConnectionManager;
import Client.connection.request.RequestSender;
import Client.connection.response.ResponseReader;
import exceptions.BadPasswordException;
import general.*;

import java.io.IOException;
import java.net.ConnectException;

public class ClientAuthorizerImpl implements ClientAuthorizer {
    private final ClientConnectionManager connectionManager;
    private final RequestSender requestSender;
    private final ResponseReader responseReader;
    private final int port;
    private String userName;
    private String password;
    private User user;

    public ClientAuthorizerImpl(ClientConnectionManager connectionManager, RequestSender requestSender, ResponseReader responseReader, int port) {
        this.connectionManager = connectionManager;
        this.requestSender = requestSender;
        this.responseReader = responseReader;
        this.port = port;
    }

    @Override
    public void register() throws IOException {
        try {
            inputRegistrationData();
            user = new User(userName, password);
            Request request = new Request(RequestType.REGISTRATION_REQUEST, user);
            connectionManager.openConnection(port);
            requestSender.sendRequest(connectionManager.getSocketChannel(), request);
            Response response = responseReader.readResponse(connectionManager.getSocketChannel());
            IO.println(response.getMessage());
        } catch (BadPasswordException e) {
            IO.errPrint(e.getMessage());
            IO.println("Please try again");
            register();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection();
        }
    }

    @Override
    public void authorize() throws IOException {
        try {
            inputRegistrationData();
            user = new User(userName, password);
            Client.setCurrentUser(user);
            Request request = new Request(RequestType.AUTHORIZATION_REQUEST, user);
            connectionManager.openConnection(port);
            requestSender.sendRequest(connectionManager.getSocketChannel(), request);
            Response response = responseReader.readResponse(connectionManager.getSocketChannel());
            IO.println(response.getMessage());
        } catch (BadPasswordException e) {
            IO.errPrint(e.getMessage());
            IO.println("Please try again");
            authorize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            IO.println("Server is unavailable");
            System.exit(0);
        } finally {
            connectionManager.closeConnection();
        }
    }

    private void inputRegistrationData() throws BadPasswordException, IOException {
        IO.println("Input username: ");
        userName = IO.readLine();
        IO.println("Input password: ");
        password = IO.readPassword();
        if (userName.length() < 4 || userName.length() > 30 || password.length() < 8 || password.length() > 30) {
            String exceptionString = "";
            if (password.length() < 8 || password.length() > 30)
                exceptionString = exceptionString + "The password must be at least 8 and at max 30 characters long\n";
            if (userName.length() < 4 || userName.length() > 30)
                exceptionString = exceptionString + "The userName must be at least 4 and at max 30 characters long\n";
            throw new BadPasswordException(exceptionString);
        }
    }
}
