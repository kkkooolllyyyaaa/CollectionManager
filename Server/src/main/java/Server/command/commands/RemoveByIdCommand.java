package Server.command.commands;


import Server.collection.CollectionManager;
import Server.connection.response.ResponseCreator;
import exceptions.NotOwnerException;
import general.AbstractCommand;
import validation.InputChecker;

public class RemoveByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public RemoveByIdCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("remove_by_id", " : удалить элемент коллекции по его id");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3)
            throw new RuntimeException("Неизвестная ошибка, не найдены все аргументы команды!");
        else if (args[1] == null)
            responseCreator.addToMsg("Не указан id");
        else if (InputChecker.checkLong(args[1].trim())) {
            long id = Long.parseLong(args[1]);
            if (collectionManager.containsId(id)) {
                try {
                    collectionManager.removeById(id, args[2]);
                } catch (NotOwnerException e) {
                    responseCreator.addToMsg(e.getMessage());
                }
            } else
                responseCreator.addToMsg("Данный id не найден");
        } else {
            responseCreator.addToMsg("Неверный формат id");
        }
    }
}
