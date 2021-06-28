package Server.server;


import Server.collection.CollectionManagerImpl;
import Server.commands.ServerCommandReaderImpl;
import Server.connection.ServerConnectionManagerImpl;
import Server.connection.request.RequestHandler;
import Server.connection.request.RequestHandlerImpl;
import Server.connection.request.RequestReader;
import Server.connection.request.RequestReaderImpl;
import Server.connection.response.ResponseCreator;
import Server.connection.response.ResponseCreatorImpl;
import Server.connection.response.ResponseSender;
import Server.connection.response.ResponseSenderImpl;
import Server.datebase.DataBaseConnector;
import Server.datebase.PostgreStudyGroupDataBase;
import Server.datebase.PostgreUserDataBase;
import Server.server.runnable.ThreadProcessorImpl;
import Server.user_manager.UserManager;
import Server.user_manager.UserManagerImpl;
import general.IOImpl;
import validation.StudyGroupBuilder;
import validation.StudyGroupBuilderImpl;
import validation.StudyGroupValidatorImpl;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                DataBaseConnector dataBaseConnector = new DataBaseConnector();
                dataBaseConnector.connect();
                ResponseCreator responseCreator = new ResponseCreatorImpl();
                StudyGroupBuilder builder = new StudyGroupBuilderImpl(IOImpl.reader, false, new StudyGroupValidatorImpl());
                PostgreStudyGroupDataBase postgreStudyGroupDataBase = new PostgreStudyGroupDataBase(builder);
                PostgreUserDataBase userDataBase = new PostgreUserDataBase();
                UserManager userManager = new UserManagerImpl(userDataBase);
                CollectionManagerImpl collectionManager = new CollectionManagerImpl(responseCreator, postgreStudyGroupDataBase);
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
