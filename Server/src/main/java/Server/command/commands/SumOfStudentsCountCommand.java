package Server.command.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;

public class SumOfStudentsCountCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public SumOfStudentsCountCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("sum_of_students_count", " : вывести сумму значений поля studentsCount для всех элементов коллекции");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        long sum = collectionManager.getStudyGroupsSortedById().stream().
                mapToLong(ServerStudyGroup::getStudentsCount).sum();
        responseCreator.addToMsg("Всего студентов: " + sum);
    }
}