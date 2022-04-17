package Server.command.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.command.StudyGroupCommand;
import Server.connection.response.ResponseCreator;
import exceptions.InvalidCommandType;
import exceptions.NotOwnerException;
import general.AbstractCommand;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class RemoveGreaterCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public RemoveGreaterCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("remove_greater {element}", " : удалить из коллекции все элементы, превышающие заданный", true);
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        throw new InvalidCommandType("remove greater command required Study Group instance");
    }

    @Override
    public void execute(String[] args, ServerStudyGroup studyGroup) {
        if (args.length != 3)
            throw new RuntimeException("Неизвестная ошибка, не найдены все аргументы команды!");

        String username = args[2];
        LinkedList<ServerStudyGroup> studyGroups = collectionManager.getStudyGroupsSortedById().stream()
                .filter(x -> x.getUsername().equals(username))
                .collect(Collectors.toCollection(LinkedList::new));

        long cnt = studyGroups.stream()
                .filter(x -> x.getStudentsCount() > studyGroup.getStudentsCount()).count();

        studyGroups.stream()
                .filter(x -> x.getStudentsCount() > studyGroup.getStudentsCount())
                .forEach(x -> {
                    try {
                        collectionManager.removeById(x.getId(), username);
                    } catch (NotOwnerException unexpected) {
                        unexpected.printStackTrace();
                        responseCreator.addToMsg(unexpected.getMessage());
                    }
                });

        responseCreator.addToMsg(cnt + " elements removed at all");
    }
}
