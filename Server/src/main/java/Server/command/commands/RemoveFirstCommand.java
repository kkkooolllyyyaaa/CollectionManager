package Server.command.commands;


import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.connection.response.ResponseCreator;
import exceptions.NotOwnerException;
import general.AbstractCommand;

public class RemoveFirstCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public RemoveFirstCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("remove_first", " : удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3)
            throw new RuntimeException("Неизвестная ошибка, не найдены все аргументы команды!");

        String username = args[2];
        ServerStudyGroup sg = collectionManager.getStudyGroupsSortedById().stream().
                filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);

        if (sg == null) {
            responseCreator.addToMsg("There is no any your element");
            return;
        }

        try {
            collectionManager.removeById(sg.getId(), username);
        } catch (NotOwnerException e) {
            responseCreator.addToMsg(e.getMessage());
        }
    }
}
