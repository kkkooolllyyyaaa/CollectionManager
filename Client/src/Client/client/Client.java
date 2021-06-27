package Client.client;

import Client.authorizer.ClientAuthorizer;
import Client.commands.*;
import Client.connection.ClientConnectionManager;
import Client.connection.request.RequestSender;
import Client.connection.response.ResponseReader;
import exceptions.CommandIsNotExistException;
import general.*;
import validation.StudyGroupBuilder;
import validation.StudyGroupBuilderImpl;
import validation.StudyGroupValidatorImpl;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.ConnectException;
import java.nio.BufferOverflowException;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;


public class Client implements ClientApp, IOimpl {
    private final ClientCommandReaderImpl commandReader;
    private final ClientConnectionManager connectionManager;
    private final RequestSender requestSender;
    private final ResponseReader responseReader;
    private final ClientAuthorizer authorizer;
    private final int port;
    private final StudyGroupBuilder studyGroupBuilder;
    private static User currentUser;
    private boolean isRunning;

    public Client(ClientCommandReaderImpl commandReader,
                  ClientConnectionManager connectionManager,
                  RequestSender requestSender,
                  ResponseReader responseReader,
                  ClientAuthorizer authorizer,
                  int port) {
        this.commandReader = commandReader;
        this.connectionManager = connectionManager;
        this.requestSender = requestSender;
        this.responseReader = responseReader;
        this.authorizer = authorizer;
        this.port = port;
        studyGroupBuilder = new StudyGroupBuilderImpl(getReader(), false, new StudyGroupValidatorImpl());
        isRunning = true;
        addCommands();
    }

    /**
     * Метод начинает работу программы клиента
     * Обеспечивает ввод пользователя и контролирует общение с сервером
     *
     * @param port
     */
    @Override
    public void start(int port) {
        try {
            println("The work is started:");
            commandReader.executeCommand("client_help", null);
        } catch (CommandIsNotExistException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            String inputString = "";
            try {
                inputString = readLine().trim();
                commandReader.executeCommand(inputString, null);
            } catch (NoSuchElementException | NullPointerException e) {
                errPrint("You can't input this\nThe work of Client will be stopped");
                return;
            } catch (IOException e) {
                errPrint(e.getMessage());
            } catch (CommandIsNotExistException e) {
                try {
                    Response response = communicateWithServer(inputString);
                    if (response == null) {
                        println("Response wasn't received, wait until the server is available");
                        return;
                    }
                    println(response.getMessage());
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     * Останавливает последующие итерации работы программы
     */
    @Override
    public void exit() {
        isRunning = false;
    }

    /**
     * Отправляет запрос серверу
     *
     * @param inputString ввод пользователя
     * @return Response - ответ сервера
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Response communicateWithServer(String inputString) throws IOException, ClassNotFoundException {
        String[] commandAndArgument = inputString.trim().split("\\s", 2);
        SocketChannel socketChannel;
        try {
            socketChannel = connectionManager.openConnection(port);
        } catch (ConnectException e) {
            println("Server is unavailable");
            exit();
            return null;
        }
        Request request = requestSender.createBasicRequest(commandAndArgument[0]);
        if (commandAndArgument.length > 1)
            request.setArg(commandAndArgument[1]);
        request.setUser(currentUser);
        requestSender.sendRequest(socketChannel, request);
        Response response = null;
        try {
            response = responseReader.readResponse(socketChannel);
        } catch (BufferOverflowException e) {
            System.out.println("Server data is too big for buffer");
        }
        connectionManager.closeConnection();
        if (response == null) {
            exit();
            println("Connection refused");
            return null;
        } else if (response.getResponseType().equals(ResponseType.STUDY_GROUP_RESPONSE)) {
            request.setUser(currentUser);
            return reCommunicateWithServer(request);
        }
        return response;
    }

    /**
     * Отправляет запрос серверу, вместе с элементом коллекции
     *
     * @param request
     * @return Response - ответ сервера
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Response reCommunicateWithServer(Request request) throws IOException, ClassNotFoundException {
        String arg = request.getArg();
        SocketChannel socketChannel = connectionManager.openConnection(port);
        StudyGroup studyGroup = studyGroupBuilder.askStudyGroup();
        studyGroup.setUsername(currentUser.getUserName());
        request = requestSender.createExecuteRequest(request.getCommandName(), studyGroup);
        request.setArg(arg);
        request.setUser(getCurrentUser());
        requestSender.sendRequest(socketChannel, request);
        Response response = null;
        try {
            response = responseReader.readResponse(socketChannel);
        } catch (StreamCorruptedException e) {
            return response;
        } catch (BufferOverflowException e) {
            response = new Response();
            response.setMessage("Server data is too big for buffer");
        }
        connectionManager.closeConnection();
        return response;
    }

    /**
     * Добавляет все клиентские команды
     */
    private void addCommands() {
        commandReader.addCommand("client_help", new ClientHelpCommand(commandReader));
        commandReader.addCommand("exit", new ClientExitCommand(this));
        commandReader.addCommand("execute_script", new ExecuteScriptCommand(this, commandReader));
        commandReader.addCommand("auth", new ClientAuthCommand(authorizer));
        commandReader.addCommand("register", new ClientRegisterCommand(authorizer));
        commandReader.addCommand("current_user", new CurrentUserCommand());
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Client.currentUser = currentUser;
    }
}
