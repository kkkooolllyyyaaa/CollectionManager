package Server.command.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.command.StudyGroupCommand;
import Server.connection.response.ResponseCreator;
import exceptions.InvalidCommandType;
import general.AbstractCommand;
import validation.InputChecker;

public class UpdateCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public UpdateCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("update id {element}", " : обновить значение элемента коллекции, id которого равен заданному", true);
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        throw new InvalidCommandType("update command required StudyGroup instance");
    }

    @Override
    public void execute(String[] args, ServerStudyGroup studyGroup) {
        if (args.length != 3)
            throw new RuntimeException("Неизвестная ошибка, не найдены все аргументы команды!");
        else if (args[1] == null)
            responseCreator.addToMsg("Не указан id");
        else if (InputChecker.checkLong(args[1].trim())) {
            long id = Long.parseLong(args[1]);
            if (collectionManager.containsId(id)) {
                collectionManager.update(id, studyGroup);
            } else
                responseCreator.addToMsg("Данный id не найден");
        } else {
            responseCreator.addToMsg("Неверный формат id");
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return true;
    }
}