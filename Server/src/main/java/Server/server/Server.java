package Server.server;


import Server.collection.CollectionManager;
import Server.command.ServerCommandReader;
import Server.command.commands.*;
import Server.connection.ServerConnectionManager;
import Server.connection.request.RequestHandler;
import Server.connection.request.RequestReader;
import Server.connection.response.ResponseCreator;
import Server.connection.response.ResponseSender;
import Server.server.runnable.ThreadProcessor;
import Server.server.runnable.ThreadProcessorFunctional;
import exceptions.CommandIsNotExistException;
import general.IO;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

public class Server implements ServerApp {
    private final CollectionManager collectionManager;
    private final ServerCommandReader commandReader;
    private final ServerConnectionManager connectionManager;
    private final ResponseCreator responseCreator;
    private final RequestReader requestReader;
    private final RequestHandler requestHandler;
    private final ResponseSender responseSender;
    private final int port;
    private boolean isRunning = true;


    public Server(CollectionManager collectionManager,
                  ServerCommandReader commandReader,
                  ServerConnectionManager connectionManager,
                  ResponseCreator responseCreator,
                  RequestReader requestReader,
                  RequestHandler requestHandler,
                  ResponseSender responseSender,
                  int port) {
        this.collectionManager = collectionManager;
        this.commandReader = commandReader;
        this.connectionManager = connectionManager;
        this.responseCreator = responseCreator;
        this.requestReader = requestReader;
        this.requestHandler = requestHandler;
        this.responseSender = responseSender;
        this.port = port;
    }

    /**
     * Начинает работу программы Сервера
     * Обеспечивает слушание одного из клиентов в порядке очереди
     */
    @Override
    public void start() {
        prepareToStart();
        run();
    }

    private void run() {
        ServerSocketChannel serverSocketChannel = connectionManager.openConnection(port);
        while (isRunning) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    ThreadProcessor threadProcessor = new ThreadProcessorFunctional(requestReader, requestHandler, responseSender, socketChannel);
                    new Thread(threadProcessor).start();
                }
            } catch (IOException e) {
                IO.println("Server is stop working");
                exit();
            }
        }
    }

    /**
     * Завершает последующие итерации слушания клиента
     * Другими словами, работу программы
     */
    @Override
    public void exit() {
        isRunning = false;
        System.exit(0);
    }

    /**
     * Начинает работу второго потока - демона, отвечающего за ввод команд на сервере
     * Обеспечивает ввод команд на сервере.
     *
     * @param commandReader - считыватель(обработчик) серверных команд
     */
    private void startConsoleDaemon(ServerCommandReader commandReader) {
        Thread consoleThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    String str = IO.readLine();
                    if (str == null)
                        throw new NoSuchElementException();
                    commandReader.executeServerCommand(str);
                } catch (CommandIsNotExistException ioe) {
                    IO.println(ioe.getMessage());
                } catch (NoSuchElementException | IOException e) {
                    IO.errPrint("You can't input this\nThe work of Server will be stopped");
                    exit();
                    return;
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
    }

    /**
     * Добавляет все команды для работы с коллекцией
     */
    private void addCommands() {
        commandReader.addCommand("add", new AddCommand(collectionManager, responseCreator));
        commandReader.addCommand("clear", new ClearCommand(collectionManager));
        commandReader.addCommand("history", new HistoryCommand(commandReader, responseCreator));
        commandReader.addCommand("info", new InfoCommand(collectionManager));
        commandReader.addCommand("print_ascending", new PrintAscendingCommand(collectionManager, responseCreator));
        commandReader.addCommand("remove_by_id", new RemoveByIdCommand(collectionManager, responseCreator));
        commandReader.addCommand("remove_first", new RemoveFirstCommand(collectionManager, responseCreator));
        commandReader.addCommand("remove_greater", new RemoveGreaterCommand(collectionManager, responseCreator));
        commandReader.addCommand("show", new ShowCommand(collectionManager, responseCreator));
        commandReader.addCommand("show_full", new ShowFullCommand(collectionManager, responseCreator));
        commandReader.addCommand("show_mine", new ShowMineCommand(collectionManager, responseCreator));
        commandReader.addCommand("sum_of_students_count", new SumOfStudentsCountCommand(collectionManager, responseCreator));
        commandReader.addCommand("update", new UpdateCommand(collectionManager, responseCreator));
        commandReader.addCommand("help", new HelpCommand(commandReader.getCommandMap(), responseCreator));
        commandReader.addCommand("my_ids", new MyIdsCommand(collectionManager, responseCreator));
    }

    /**
     * Добавляет все серверные команды
     */
    private void addServerCommands() {
        commandReader.addServerCommand("exit", new ExitCommand(this));
    }

    /**
     * Подготовка к работе программы
     * Чтение коллекции из файла
     * Инициализация полей
     * Добавление команд
     */
    private void prepareToStart() {
        addServerCommands();
        addCommands();
        startConsoleDaemon(commandReader);
    }
}
