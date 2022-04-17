package Server.command.commands;


import Server.collection.CollectionManager;
import general.AbstractCommand;

public class RemoveFirstCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", " : удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3)
            throw new RuntimeException("Неизвестная ошибка, не найдены все аргументы команды!");
        collectionManager.removeFirstElement(args[2]);
    }
}
