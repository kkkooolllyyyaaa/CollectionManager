package Server;


import Server.collection.CollectionManager;
import Server.collection.CollectionManagerImpl;
import Server.command.ServerCommandReaderImpl;
import Server.connection.ServerConnectionManagerImpl;
import Server.connection.request.RequestHandler;
import Server.connection.request.RequestHandlerImpl;
import Server.connection.request.RequestReader;
import Server.connection.request.RequestReaderImpl;
import Server.connection.response.ResponseCreator;
import Server.connection.response.ResponseCreatorImpl;
import Server.connection.response.ResponseSender;
import Server.connection.response.ResponseSenderImpl;
import Server.datebase.*;
import Server.server.Server;
import Server.server.ServerApp;
import Server.user_manager.UserManager;
import Server.user_manager.UserManagerImpl;
import general.IO;
import validation.StudyGroupBuilder;
import validation.StudyGroupBuilderImpl;
import validation.StudyGroupValidatorImpl;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                DataBaseConnector.init();
                ResponseCreator responseCreator = new ResponseCreatorImpl();
                StudyGroupBuilder builder = new StudyGroupBuilderImpl(IO.getReader(), false, new StudyGroupValidatorImpl());
                StudyGroupDataBase studyGroupDataBase = new PostgreStudyGroupDataBase(builder);
                UserDataBase userDataBase = new PostgreUserDataBase();
                UserManager userManager = new UserManagerImpl(userDataBase);
                CollectionManager collectionManager = new CollectionManagerImpl(responseCreator, studyGroupDataBase);
                ServerCommandReaderImpl commandReader = new ServerCommandReaderImpl();
                ServerConnectionManagerImpl connectionManager = new ServerConnectionManagerImpl();
                RequestReader requestReader = new RequestReaderImpl();
                ResponseSender responseSender = new ResponseSenderImpl();
                RequestHandler requestHandler = new RequestHandlerImpl(userManager, commandReader, collectionManager, responseCreator);
                ServerApp server = new Server(
                        collectionManager,
                        commandReader,
                        connectionManager,
                        responseCreator,
                        requestReader,
                        requestHandler,
                        responseSender,
                        Integer.parseInt(args[0]));

                server.start();

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Input arguments: port");
    }
}
