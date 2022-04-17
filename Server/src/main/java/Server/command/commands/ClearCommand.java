package Server.command.commands;


import Server.collection.CollectionManager;
import general.AbstractCommand;

public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManagerImpl) {
        super("clear", " : очистить коллекцию");
        this.collectionManager = collectionManagerImpl;
    }

    @Override
    public void execute(String[] args) {
        collectionManager.clear(args[2]);
    }
}