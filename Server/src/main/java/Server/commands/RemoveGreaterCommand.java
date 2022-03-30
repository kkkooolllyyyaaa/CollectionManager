package Server.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import general.*;
import exceptions.*;

public class RemoveGreaterCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater {element}", " : удалить из коллекции все элементы, превышающие заданный", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        throw new InvalidCommandType("remove greater command required Study Group instance");
    }

    @Override
    public void execute(String[] args, ServerStudyGroup studyGroup) {
        collectionManager.removeGreater(studyGroup, args[2]);
    }
}
